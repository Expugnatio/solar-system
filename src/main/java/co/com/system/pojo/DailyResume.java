package co.com.system.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DailyResume {
  private int dayOfCalculation;
  private boolean allPlanetsAligned;
  private boolean alignedWithSun;
  private boolean sunInPlanetsTriangle;
  private double planetTrianglePerimeter;
}