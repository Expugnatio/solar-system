package co.com.system.dto;

import co.com.system.pojo.SystemDailyResume;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SystemPeriodResumeDto {
  private int droughtDays = 0;
  private int optimalPressureTemperatureDays = 0;
  private int rainDays = 0;
  private int unknownDays = 0;
  private List<String> maxRainDays;

  @JsonIgnore
  private List<SystemDailyResume> systemDailyResume = new ArrayList<>();

  public int getMaxRainDaysCount(){
    return this.maxRainDays != null ? this.maxRainDays.size() : 0;
  }
}
