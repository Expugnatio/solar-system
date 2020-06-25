package co.com.system.service;

import co.com.system.config.SolarSystem;
import co.com.system.config.TimeConfig;
import co.com.system.dto.PlanetResumeDto;
import co.com.system.enums.DirectionEnum;
import co.com.system.pojo.DailyResume;
import co.com.system.pojo.Planet;
import co.com.system.pojo.Point;
import co.com.system.strategy.WeatherCalculatorStrategy;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CycleService {

  private final TimeConfig timeConfig;
  private final SolarSystem solarSystem;
  private final CoordinatesService coordinatesService;
  private final List<WeatherCalculatorStrategy> weatherCalculatorStrategies;

  public PlanetResumeDto calculateWeatherOverPeriod(int days) {

    List<DailyResume> periodResume = new ArrayList<>();

    for (int day = 1; day <= (days > 0 ? days : timeConfig.getDaysToCalculate()) ; day++){
      initCoordenatesPerDay(day);

      DailyResume dailyResume = new DailyResume();
      dailyResume.setDayOfCalculation(day);

      for(WeatherCalculatorStrategy strategy : weatherCalculatorStrategies)
        dailyResume = strategy.calculateDailyWeather(solarSystem, dailyResume);
      periodResume.add(dailyResume);
    }

    /*for (DailyResume d : periodResume) {
      System.out.println(
          "Resumen - Dia: "
              + d.getDayOfCalculation() + " ::: "
              + d.isAllPlanetsAligned() + " xxx "
              + d.isAlignedWithSun() + " ;;; "
              + d.isSunInPlanetsTriangle() + " ``` "
              + d.getPlanetTrianglePerimeter()
      );
    }*/

    PlanetResumeDto planetResumeDto = new PlanetResumeDto();
    planetResumeDto.setDailyResume(periodResume);

    return planetResumeDto;
  }

  private void initCoordenatesPerDay(int day){
    solarSystem
        .getPlanets().forEach((planetEnum, planet) ->
        planet.setCartesianLocation(calculatePlanetCartesianCoordinates(planet, day)));
  }

  public Point calculatePlanetCartesianCoordinates(Planet planet, int days) {
    int translatedDegrees = (days * planet.getDegreesPerDay());
    int calculatedDegrees = translatedDegrees > 360 ? translatedDegrees % 360 : translatedDegrees;
    calculatedDegrees =
        planet.getDirection() == DirectionEnum.COUNTERCLOCKWISE
            ? calculatedDegrees
            : (360 - calculatedDegrees);

    /*System.out.println(
        "Planeta: "
            + planet.getName()
            + " Dia: "
            + days
            + " - X "
            + coordinatesService.calculateXCoordinate(
                planet.getDistanceFromSun(), calculatedDegrees)
            + " - Y "
            + coordinatesService.calculateYCoordinate(
                planet.getDistanceFromSun(), calculatedDegrees));*/

    return new Point(
        coordinatesService.calculateXCoordinate(planet.getDistanceFromSun(), calculatedDegrees),
        coordinatesService.calculateYCoordinate(planet.getDistanceFromSun(), calculatedDegrees));
  }
}
