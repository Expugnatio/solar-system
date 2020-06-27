package co.com.system.job;

import co.com.system.repository.WeatherRepository;
import co.com.system.service.CycleService;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SolarSystemJob {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  @Autowired private WeatherRepository weatherRepository;

  @Autowired private CycleService cycleService;

  @Scheduled(cron = "${time.job.cron-expression:-}")
  public void generateRegistersJob() {
    cycleService
        .calculateWeatherOverPeriodStream(0)
        .getSystemDailyResume()
        .forEach(weatherRepository::saveRegister);
    System.out.println("Job Executed Succesfully at " + dateFormat.format(new Date()));
  }
}
