package co.com.system.strategy;

import co.com.system.config.SolarSystem;
import co.com.system.enums.PlanetEnum;
import co.com.system.pojo.SystemDailyResume;
import co.com.system.pojo.Planet;
import co.com.system.pojo.Point;
import co.com.system.service.CoordinatesService;
import org.springframework.stereotype.Component;

@Component
public class RainyClimateStrategy extends WeatherCalculatorStrategy{

  public RainyClimateStrategy(CoordinatesService coordinatesService) {
    super(coordinatesService);
  }

  @Override
  public SystemDailyResume calculateDailyWeather(SolarSystem solarSystem, SystemDailyResume systemDailyResume) {

    Planet ferengi = solarSystem.getPlanets().get(PlanetEnum.FERENGI);
    Planet betasoide = solarSystem.getPlanets().get(PlanetEnum.BETASOIDE);
    Planet vulcano = solarSystem.getPlanets().get(PlanetEnum.VULCANO);

    boolean isSunInsideTriangle =
        checkSunInsidePlanetsTriangle(new Point(0,0), ferengi, betasoide, vulcano);

    systemDailyResume.setSunInPlanetsTriangle(isSunInsideTriangle);
    systemDailyResume.setPlanetTrianglePerimeter(
        coordinatesService.calculateTrianglePerimeter(
            ferengi.getCartesianLocation(),
            betasoide.getCartesianLocation(),
            vulcano.getCartesianLocation()));

    return systemDailyResume;
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

    /*System.out.println(
        "Areas: Planetas: "
            + planetsTriangleArea
            + " ::: PBC: "
            + areaPT2T3
            + " ::: APC: "
            + areaT1PT3
            + " ::: ABP: "
            + areaT1T2P);*/

    return
        (areaPT2T3 + areaT1PT3 + areaT1T2P) <= (planetsTriangleArea +0.01) &&
        (areaPT2T3 + areaT1PT3 + areaT1T2P) >= (planetsTriangleArea -0.01);
  }

}
