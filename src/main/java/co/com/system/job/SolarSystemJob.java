package co.com.system.job;

import co.com.system.service.CycleService;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SolarSystemJob {

  @Autowired
  private CycleService cycleService;

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  @Scheduled(cron = "${time.job.cronExpression:-}")
  public void reportCurrentTime() {
    cycleService.calculateWeatherOverPeriod(0);
    //log.info("The time is now {}", dateFormat.format(new Date()));
  }
}
