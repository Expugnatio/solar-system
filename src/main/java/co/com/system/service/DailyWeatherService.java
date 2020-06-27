package co.com.system.service;

import co.com.system.config.SolarSystem;
import co.com.system.enums.DirectionEnum;
import co.com.system.enums.WeatherEnum;
import co.com.system.pojo.Planet;
import co.com.system.pojo.Point;
import co.com.system.pojo.SystemDailyResume;
import co.com.system.strategy.WeatherCalculatorStrategy;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DailyWeatherService {

  private final SolarSystem solarSystem;
  private final CoordinatesService coordinatesService;
  private final List<WeatherCalculatorStrategy> weatherCalculatorStrategies;

  public SystemDailyResume createDailyWeatherResume(int day) {
    System.out.println("Calculating Weather");
    SystemDailyResume systemDailyResume = new SystemDailyResume();
    systemDailyResume.setDayOfCalculation(day);

    initCoordenatesPerDay(day);

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

  private Point calculatePlanetCartesianCoordinates(Planet planet, int days) {
    int translatedDegrees = (days * planet.getDegreesPerDay());
    int calculatedDegrees = translatedDegrees > 360 ? translatedDegrees % 360 : translatedDegrees;
    calculatedDegrees =
        planet.getDirection() == DirectionEnum.COUNTERCLOCKWISE
            ? calculatedDegrees
            : (360 - calculatedDegrees);

    return coordinatesService.calculateCoordinates(planet.getDistanceFromSun(), calculatedDegrees);
  }

}
