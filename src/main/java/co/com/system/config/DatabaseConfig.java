package co.com.system.config;

import co.com.system.pojo.SystemDailyResume;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

  private static final Map<Integer, SystemDailyResume> DATABASE = new HashMap<>();

  @Bean
  public Map<Integer, SystemDailyResume> getDataBase(){
    return this.DATABASE;
  }

}
