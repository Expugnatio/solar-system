package co.com.system.controller;

import co.com.system.dto.SystemPeriodResumeDto;
import co.com.system.job.SolarSystemJob;
import co.com.system.pojo.SystemDailyResume;
import co.com.system.service.CycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/system/weather")
public class SystemWeatherController {

  @Autowired private CycleService cycleService;
  @Autowired private SolarSystemJob solarSystemJob;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SystemDailyResume> getDayWeather(@RequestParam("day") int day) {
    if(day <= 0){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Value of 'day' must be greater than cero");
    }
    return ResponseEntity.status(200).body(cycleService.getRepositoryWeatherInfo(day));
  }

  @GetMapping(value = "/job", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SystemPeriodResumeDto> executeJob() {
    solarSystemJob.generateRegistersJob();
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/period", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SystemPeriodResumeDto> getPeriodWeather(
      @RequestParam(required = false, name = "days", defaultValue = "0") int days) {
    if(days <= 0){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Value of 'days' must be greater than cero");
    }
    return ResponseEntity.ok(cycleService.calculateWeatherOverPeriodStream(days));
  }
}
