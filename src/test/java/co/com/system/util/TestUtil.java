package co.com.system.util;

import co.com.system.config.TimeConfig;

public class TestUtil {

  public static TimeConfig buildTimeConfig(){
    TimeConfig timeConfig = new TimeConfig();
    timeConfig.setDaysOfYear(365);
    timeConfig.setDaysToCalculate(3650);
    return timeConfig;
  }

}
