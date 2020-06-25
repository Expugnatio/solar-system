package co.com.system.controller;

import co.com.system.dto.PlanetResumeDto;
import co.com.system.pojo.DailyResume;
import co.com.system.service.CycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/weather")
public class SystemWeatherController {

  @Autowired
  private CycleService cycleService;

  /*@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DailyResume> getDayWeather(@RequestParam("dia") int day){
    return ResponseEntity.status(200).body(cycleService.calculateWeatherOverPeriod(day));
  }*/

  @GetMapping(value = "/period", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PlanetResumeDto> executeJobWeather(@RequestParam(required = false, name = "dias", defaultValue = "0") int days){
    return ResponseEntity.ok(cycleService.calculateWeatherOverPeriod(days));
  }

}
