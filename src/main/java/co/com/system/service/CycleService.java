package co.com.system.service;

import co.com.system.config.SolarSystem;
import co.com.system.config.TimeConfig;
import co.com.system.dto.PlanetResumeDto;
import co.com.system.enums.DirectionEnum;
import co.com.system.enums.PlanetEnum;
import co.com.system.pojo.AlignmentResult;
import co.com.system.pojo.DailyResume;
import co.com.system.pojo.Planet;
import co.com.system.pojo.Point;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CycleService {

  private final TimeConfig timeConfig;
  private final SolarSystem solarSystem;
  private final CoordinatesService coordinatesService;

  private static final Point SUN_COORDINATES = new Point(0, 0);

  public PlanetResumeDto calculateWeatherOverPeriod(int days) {

    List<DailyResume> periodResume =
        IntStream.range(0, days > 0 ? days : timeConfig.getDaysToCalculate())
            .boxed()
            .map(
                day -> {
                  DailyResume dailyResume = calculateWeatherPerDay(day);
                  return DailyResume.builder()
                      .dayOfCalculation(day)
                      .allPlanetsAligned(dailyResume.isAllPlanetsAligned())
                      .alignedWithSun(dailyResume.isAlignedWithSun())
                      .build();
                })
            .collect(Collectors.toList());

    for (DailyResume d : periodResume) {
      System.out.println(
          "Resumen - Dia: "
              + d.getDayOfCalculation() + " ::: "
              + d.isAllPlanetsAligned() + " xxx "
              + d.isAlignedWithSun() + " ;;; "
              + d.isSunInPlanetsTriangle() + " ``` "
              + d.getPlanetTrianglePerimeter()
      );
    }

    List<DailyResume> alignedDays =
        periodResume.stream()
            .filter(dailyResume -> dailyResume.isAllPlanetsAligned())
            .collect(Collectors.toList());

    return new PlanetResumeDto();
  }

  public DailyResume calculateWeatherPerDay(int day) {

    solarSystem
        .getPlanets()
        .values()
        .forEach(
            planet ->
                planet.setCartesianLocation(calculatePlanetCartesianCoordinates(planet, day)));

    Planet ferengi = solarSystem.getPlanets().get(PlanetEnum.FERENGI);
    Planet betasoide = solarSystem.getPlanets().get(PlanetEnum.BETASOIDE);
    Planet vulcano = solarSystem.getPlanets().get(PlanetEnum.VULCANO);

    AlignmentResult alignmentResult = checkSolarSystemAlignment(ferengi, betasoide, vulcano);

    boolean isSunInsideTriangle =
        checkSunInsidePlanetsTriangle(SUN_COORDINATES, ferengi, betasoide, vulcano);

    return DailyResume.builder()
        .allPlanetsAligned(alignmentResult.isPlanetsAligned())
        .alignedWithSun(alignmentResult.isAlignedWithSun())
        .sunInPlanetsTriangle(isSunInsideTriangle)
        .planetTrianglePerimeter(
            coordinatesService.calculateTrianglePerimeter(
                ferengi.getCartesianLocation(),
                betasoide.getCartesianLocation(),
                vulcano.getCartesianLocation()))
        .build();
  }

  public AlignmentResult checkSolarSystemAlignment(Planet... planets) {
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
                SUN_COORDINATES,
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

  public boolean checkSunInsidePlanetsTriangle(Point p, Planet t1, Planet t2, Planet t3) {
    double planetsTriangleArea =
        coordinatesService.calculateTriangleArea(
            t1.getCartesianLocation(), t2.getCartesianLocation(), t3.getCartesianLocation());

    double areaPT2T3 =
        coordinatesService.calculateTriangleArea(
            p, t2.getCartesianLocation(), t3.getCartesianLocation());
    double areaT1PT3 =
        coordinatesService.calculateTriangleArea(
            t1.getCartesianLocation(), p, t3.getCartesianLocation());
    double areaT1T2P =
        coordinatesService.calculateTriangleArea(
            t1.getCartesianLocation(), t2.getCartesianLocation(), p);

    System.out.println(
        "Areas: Planetas: "
            + planetsTriangleArea
            + " ::: PBC: "
            + areaPT2T3
            + " ::: APC: "
            + areaT1PT3
            + " ::: ABP: "
            + areaT1T2P);
    return planetsTriangleArea == (areaPT2T3 + areaT1PT3 + areaT1T2P);
  }

  public Point calculatePlanetCartesianCoordinates(Planet planet, int days) {
    int translatedDegrees = (days * planet.getDegreesPerDay());
    int calculatedDegrees = translatedDegrees > 360 ? translatedDegrees % 360 : translatedDegrees;
    calculatedDegrees =
        planet.getDirection() == DirectionEnum.COUNTERCLOCKWISE
            ? calculatedDegrees
            : (360 - calculatedDegrees);

    System.out.println(
        "Planeta: "
            + planet.getName()
            + " Dia: "
            + days
            + " - X "
            + coordinatesService.calculateXCoordinate(
                planet.getDistanceFromSun(), calculatedDegrees)
            + " - Y "
            + coordinatesService.calculateYCoordinate(
                planet.getDistanceFromSun(), calculatedDegrees));

    return new Point(
        coordinatesService.calculateXCoordinate(planet.getDistanceFromSun(), calculatedDegrees),
        coordinatesService.calculateYCoordinate(planet.getDistanceFromSun(), calculatedDegrees));
  }
}
