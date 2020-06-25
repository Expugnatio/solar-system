package co.com.system.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "time.config")
@Getter
@Setter
public class TimeConfig {

  private int daysToCalculate;
  private int daysOfYear;

}
