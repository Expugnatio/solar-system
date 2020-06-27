package co.com.system.strategy;

import co.com.system.config.SolarSystem;
import co.com.system.enums.PlanetEnum;
import co.com.system.pojo.AlignmentResult;
import co.com.system.pojo.SystemDailyResume;
import co.com.system.pojo.Planet;
import co.com.system.pojo.Point;
import co.com.system.service.CoordinatesService;
import org.springframework.stereotype.Component;

@Component
public class DroughtClimateStrategy extends WeatherCalculatorStrategy{

  public DroughtClimateStrategy(CoordinatesService coordinatesService) {
    super(coordinatesService);
  }

  @Override
  public SystemDailyResume calculateDailyWeather(SolarSystem solarSystem, SystemDailyResume systemDailyResume) {

    AlignmentResult alignmentResult = checkSolarSystemAlignment(
        solarSystem.getPlanets().get(PlanetEnum.FERENGI),
        solarSystem.getPlanets().get(PlanetEnum.BETASOIDE),
        solarSystem.getPlanets().get(PlanetEnum.VULCANO));

    systemDailyResume.setAllPlanetsAligned(alignmentResult.isPlanetsAligned());
    systemDailyResume.setAlignedWithSun(alignmentResult.isAlignedWithSun());

    return systemDailyResume;
  }

  private AlignmentResult checkSolarSystemAlignment(Planet... planets) {
    boolean coordinatesCoolinear = true;
    boolean sunAlignment = false;

    // Verifies at least three planets to 'draw' at least two lines
    if (planets.length >= 3) {

      for (int i = 0; i < planets.length && coordinatesCoolinear; i += 3) {
        coordinatesCoolinear =
            coordinatesService.checkCoolinearPoints(
                planets[i].getCartesianLocation(),
                planets[i + 1].getCartesianLocation(),
                planets[i + 2].getCartesianLocation());
      }

      // If the planets are aligned, then check if they are also aligned to the sun
      if (coordinatesCoolinear) {
        sunAlignment =
            coordinatesService.checkCoolinearPoints(
                new Point(0,0),
                planets[0].getCartesianLocation(),
                planets[1].getCartesianLocation());
      }
    } else {
      coordinatesCoolinear = sunAlignment;
    }

    return AlignmentResult.builder()
        .planetsAligned(coordinatesCoolinear)
        .alignedWithSun(sunAlignment)
        .build();
  }
}
