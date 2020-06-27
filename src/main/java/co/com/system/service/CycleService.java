package co.com.system.service;

import co.com.system.config.SolarSystem;
import co.com.system.config.TimeConfig;
import co.com.system.dto.SystemPeriodResumeDto;
import co.com.system.enums.DirectionEnum;
import co.com.system.enums.WeatherEnum;
import co.com.system.pojo.SystemDailyResume;
import co.com.system.pojo.Planet;
import co.com.system.pojo.Point;
import co.com.system.repository.WeatherRepository;
import co.com.system.strategy.WeatherCalculatorStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Service
public class CycleService {

  private final TimeConfig timeConfig;
  private final DailyWeatherService dailyWeatherService;
  private final WeatherRepository weatherRepository;

  public SystemPeriodResumeDto calculateWeatherOverPeriodStream(int days) {

    Map<WeatherEnum, List<SystemDailyResume>> daysPerWeather =
        IntStream.range(1, days > 0 ? (days + 1) : timeConfig.getDaysToCalculate())
            .mapToObj(dailyWeatherService::createDailyWeatherResume)
            .collect(Collectors.groupingBy(SystemDailyResume::getWeather));

    SystemPeriodResumeDto systemResumeDto = new SystemPeriodResumeDto();

    systemResumeDto.setRainDays(daysPerWeather.get(WeatherEnum.RAIN).size());

    systemResumeDto.setDroughtDays(daysPerWeather.get(WeatherEnum.DROUGHT).size());

    systemResumeDto.setOptimalPressureTemperatureDays(
        daysPerWeather.get(WeatherEnum.OPTIMAL) != null
            ? daysPerWeather.get(WeatherEnum.OPTIMAL).size()
            : 0);

    systemResumeDto.setUnknownDays(daysPerWeather.get(WeatherEnum.UNKNOWN).size());
    daysPerWeather.values().forEach(list-> systemResumeDto.getSystemDailyResume().addAll(list));

    SystemDailyResume rainMaxPeakDay =
        daysPerWeather.get(WeatherEnum.RAIN).stream()
            .max(
                (o1, o2) ->
                    o1.getPlanetTrianglePerimeter() >= o2.getPlanetTrianglePerimeter() ? 1 : -1)
            .get();

    System.out.println("Max Lluvia: " + rainMaxPeakDay.getPlanetTrianglePerimeter());

    systemResumeDto.setMaxRainDays(
        daysPerWeather.get(WeatherEnum.RAIN).stream()
            .filter(
                resume ->
                    resume.getPlanetTrianglePerimeter()
                        == rainMaxPeakDay.getPlanetTrianglePerimeter())
            .map(resume -> "" + resume.getDayOfCalculation())
            .collect(Collectors.toList()));

    return systemResumeDto;
  }

  public SystemPeriodResumeDto calculateWeatherOverPeriodArray(int days) {

    SystemPeriodResumeDto systemResumeDto = new SystemPeriodResumeDto();
    List<SystemDailyResume> periodResume = new ArrayList<>();
    SystemDailyResume dailyResume;

    double rainMaxPeak = 0;

    for (int day = 1; day <= (days > 0 ? days : timeConfig.getDaysToCalculate()); day++) {

      dailyResume = dailyWeatherService.createDailyWeatherResume(day);
      switch (dailyResume.getWeather()) {
        case RAIN:
          {
            systemResumeDto.setRainDays((systemResumeDto.getRainDays() + 1));
            rainMaxPeak =
                dailyResume.getPlanetTrianglePerimeter() >= rainMaxPeak
                    ? dailyResume.getPlanetTrianglePerimeter()
                    : rainMaxPeak;
            break;
          }
        case DROUGHT:
          systemResumeDto.setDroughtDays((systemResumeDto.getDroughtDays() + 1));
          break;
        case OPTIMAL:
          systemResumeDto.setOptimalPressureTemperatureDays(
              (systemResumeDto.getOptimalPressureTemperatureDays() + 1));
          break;
        default:
          systemResumeDto.setUnknownDays((systemResumeDto.getUnknownDays() + 1));
      }
      periodResume.add(dailyResume);
    }

    final double finalRainMaxPeak = rainMaxPeak;
    System.out.println("Max Lluvia: " + finalRainMaxPeak);
    systemResumeDto.setMaxRainDays(
        periodResume.stream()
            .filter(resume -> resume.getPlanetTrianglePerimeter() == finalRainMaxPeak)
            .map(resume -> "" + resume.getDayOfCalculation())
            .collect(Collectors.toList()));

    systemResumeDto.setSystemDailyResume(periodResume);

    return systemResumeDto;
  }

  public SystemDailyResume getRepositoryWeatherInfo(int day) {

    SystemDailyResume resume = weatherRepository.getRegister(day);
    System.out.println("Weather Registry found in DB");
    return ObjectUtils.isEmpty(resume)
        ? weatherRepository.saveRegister(dailyWeatherService.createDailyWeatherResume(day))
        : resume;
  }
}
