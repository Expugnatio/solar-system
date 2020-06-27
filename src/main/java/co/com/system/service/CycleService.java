package co.com.system.service;

import co.com.system.config.SolarSystem;
import co.com.system.config.TimeConfig;
import co.com.system.dto.SystemPeriodResumeDto;
import co.com.system.enums.DirectionEnum;
import co.com.system.enums.WeatherEnum;
import co.com.system.pojo.SystemDailyResume;
import co.com.system.pojo.Planet;
import co.com.system.pojo.Point;
import co.com.system.strategy.WeatherCalculatorStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CycleService {

  private final TimeConfig timeConfig;
  private final SolarSystem solarSystem;
  private final CoordinatesService coordinatesService;
  private final List<WeatherCalculatorStrategy> weatherCalculatorStrategies;

  public SystemPeriodResumeDto calculateWeatherOverPeriodStream(int days) {

    Map<WeatherEnum, List<SystemDailyResume>> daysPerWeather =
        IntStream.range(0, days > 0 ? days : timeConfig.getDaysToCalculate())
            .mapToObj(this::createDailyWeatherResume)
            .collect(Collectors.groupingBy(SystemDailyResume::getWeather));

    SystemPeriodResumeDto systemResumeDto = new SystemPeriodResumeDto();

    systemResumeDto.setRainDays(daysPerWeather.get(WeatherEnum.RAIN).size());

    systemResumeDto.setDroughtDays(daysPerWeather.get(WeatherEnum.DROUGHT).size());

    systemResumeDto.setOptimalPressureTemperatureDays(
        daysPerWeather.get(WeatherEnum.OPTIMAL) != null
            ? daysPerWeather.get(WeatherEnum.OPTIMAL).size()
            : 0);

    systemResumeDto.setUnknownDays(daysPerWeather.get(WeatherEnum.UNKNOWN).size());

    SystemDailyResume rainMaxPeakDay =
        daysPerWeather.get(WeatherEnum.RAIN).stream()
            .max(
                (o1, o2) ->
                    o1.getPlanetTrianglePerimeter() >= o2.getPlanetTrianglePerimeter() ? 1 : -1)
            .get();

    System.out.println("Max Lluvia: "+rainMaxPeakDay.getPlanetTrianglePerimeter());

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

      dailyResume = createDailyWeatherResume(day);
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
    System.out.println("Max Lluvia: "+finalRainMaxPeak);
    systemResumeDto.setMaxRainDays(
        periodResume.stream()
            .filter(resume -> resume.getPlanetTrianglePerimeter() == finalRainMaxPeak)
            .map(resume -> "" + resume.getDayOfCalculation())
            .collect(Collectors.toList()));

    systemResumeDto.setSystemDailyResume(periodResume);

    /*for (SystemDailyResume d : periodResume) {
      System.out.println(
          "Resumen - Dia: "
              + d.getDayOfCalculation() + " ::: "
              + d.isAllPlanetsAligned() + " xxx "
              + d.isAlignedWithSun() + " ;;; "
              + d.isSunInPlanetsTriangle() + " ``` "
              + d.getPlanetTrianglePerimeter()
      );
    }*/

    return systemResumeDto;
  }

  public SystemDailyResume createDailyWeatherResume(int day) {
    initCoordenatesPerDay(day);
    SystemDailyResume systemDailyResume = new SystemDailyResume();
    systemDailyResume.setDayOfCalculation(day);

    for (WeatherCalculatorStrategy strategy : weatherCalculatorStrategies)
      systemDailyResume = strategy.calculateDailyWeather(solarSystem, systemDailyResume);

    WeatherEnum weather = WeatherEnum.UNKNOWN;

    if (systemDailyResume.isAllPlanetsAligned()) {
      if (systemDailyResume.isAlignedWithSun()) {
        weather = WeatherEnum.DROUGHT;
      } else {
        weather = WeatherEnum.OPTIMAL;
      }
    } else if (systemDailyResume.isSunInPlanetsTriangle()) {
      weather = WeatherEnum.RAIN;
    }

    systemDailyResume.setWeather(weather);
    return systemDailyResume;
  }

  private void initCoordenatesPerDay(int day) {
    solarSystem
        .getPlanets()
        .forEach(
            (planetEnum, planet) ->
                planet.setCartesianLocation(calculatePlanetCartesianCoordinates(planet, day)));
  }

  public Point calculatePlanetCartesianCoordinates(Planet planet, int days) {
    int calculatedDegrees = days > 360 ? days % 360 : days;
    calculatedDegrees =
        planet.getDirection() == DirectionEnum.COUNTERCLOCKWISE
            ? calculatedDegrees
            : (360 - calculatedDegrees);

    return new Point(
        coordinatesService.calculateXCoordinate(planet.getDistanceFromSun(), calculatedDegrees),
        coordinatesService.calculateYCoordinate(planet.getDistanceFromSun(), calculatedDegrees));
  }
}
