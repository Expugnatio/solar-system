package co.com.system.pojo;

import co.com.system.enums.DirectionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Planet {
  private String name;
  private DirectionEnum direction;
  private int degreesPerDay;
  private int distanceFromSun;
  private Point cartesianLocation;
}
