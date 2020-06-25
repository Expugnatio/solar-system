package co.com.system.dto;

import co.com.system.pojo.DailyResume;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class PlanetResumeDto {
  private String climate;
  private String droughtDays;
  private String rainDays;
  private String maxRainDay;
  private String optimalPressureTemperatureDays;
  private List<DailyResume> dailyResume;
}
