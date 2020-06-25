package co.com.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PlanetResumeDto {
  private String climate;
  private String droughtDays;
  private String rainDays;
  private String maxRainDay;
  private String optimalPressureTemperatureDays;
}
