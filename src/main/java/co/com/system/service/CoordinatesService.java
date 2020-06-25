package co.com.system.service;

import co.com.system.pojo.Point;
import java.text.DecimalFormat;
import java.util.function.BiFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoordinatesService {

  @Autowired private DecimalFormat calculationsDecimalFormat;

  public double calculateXCoordinate(int radius, int degree) {
    return Double.parseDouble(
        calculationsDecimalFormat.format(radius * Math.cos(Math.toRadians(degree))));
  }

  public double calculateYCoordinate(int radius, int degree) {
    return Double.parseDouble(
        calculationsDecimalFormat.format(radius * Math.sin(Math.toRadians(degree))));
  }

  public boolean checkCoolinearPoints(Point p1, Point p2, Point p3) {
    // the Slope of a line gets calculated by: m = (y2 - y1)/(x2 - x1)
    // three dots are aligned if the slope AB == BC == AC
    // so (y2 - y1)/(x2 - x1) == (y3 - y2)/(x3 - x2)
    // turning into: (y2 - y1)(x3 - x2) == (y3 - y2)(x2 - x1) to avoid division over 0

    double slope1 = (p2.getY() - p1.getY()) * (p2.getX() - p1.getX());

    double slope2 = (p3.getY() - p2.getY()) * (p2.getX() - p1.getX());

    System.out.println("pendientes: " + slope1 + " ::: " + slope2);
    System.out.println("________________________________________");
    return slope1 == slope2;
  }

  public double calculateTriangleArea(Point... t) {
    // The triangle area formed by coordinates is given by the determinant of the points divided by
    // 2 (in this case it is not necessary and could affect the float accuracy)
    // ((x1*y2 + x2*y3 + x3y1) – (x1y3 – x2y1 – x3y2))/2
    return ((t[0].getX() * t[1].getY()) + (t[1].getX() * t[2].getY()) + (t[2].getX() * t[0].getY()))
        - ((t[0].getX() * t[2].getY()) - (t[1].getX() * t[0].getY()) - (t[2].getX() * t[1].getY()));
  }

  public double calculateTrianglePerimeter(Point... points) {
    // The triangle perimeter formed by coordinates A B C is given by the sum of distances
    // of AB, BC and CA [Distance AB = Sqrt( (x2 - x1)(x2 - x1) + (y2 - y1)(y2 - y1) ])
    double distanceAB, distanceBC, distanceCA;

    BiFunction<Point, Point, Double> pointsDistance =
        (point1, point2) -> {
          double x = (point2.getX() - point1.getX());
          double y = (point2.getY() - point1.getY());
          return Math.sqrt((x * x) + (y * y));
        };

    distanceAB = pointsDistance.apply(points[0], points[1]);
    distanceBC = pointsDistance.apply(points[1], points[2]);
    distanceCA = pointsDistance.apply(points[2], points[0]);

    return distanceAB + distanceBC + distanceCA;
  }
}
