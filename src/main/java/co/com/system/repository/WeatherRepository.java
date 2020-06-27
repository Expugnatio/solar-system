package co.com.system.repository;

import co.com.system.pojo.SystemDailyResume;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherRepository {

  @Autowired
  private Map<Integer, SystemDailyResume> database;

  public SystemDailyResume getRegister(int day){
    return database.get(day);
  }

  public SystemDailyResume saveRegister(SystemDailyResume resume){
    return database.put(resume.getDayOfCalculation(), resume);
  }
}
