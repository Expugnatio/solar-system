package co.com.system.pojo;

import co.com.system.config.SolarSystem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyResume {
  private int dayOfCalculation;
  private boolean allPlanetsAligned;
  private boolean alignedWithSun;
  private boolean sunInPlanetsTriangle;
  private double planetTrianglePerimeter;
  private SolarSystem solarSystemState;
}