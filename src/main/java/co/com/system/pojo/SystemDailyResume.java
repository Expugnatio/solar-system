package co.com.system.pojo;

import co.com.system.config.SolarSystem;
import co.com.system.enums.WeatherEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SystemDailyResume {

  private int dayOfCalculation;
  private WeatherEnum weather;

  @JsonIgnore
  private boolean allPlanetsAligned;
  @JsonIgnore
  private boolean alignedWithSun;
  @JsonIgnore
  private boolean sunInPlanetsTriangle;
  @JsonIgnore
  private double planetTrianglePerimeter;
  @JsonIgnore
  private SolarSystem solarSystemState;
}