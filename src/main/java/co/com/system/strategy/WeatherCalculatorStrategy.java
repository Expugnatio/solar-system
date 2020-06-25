package co.com.system.strategy;

import co.com.system.config.SolarSystem;
import co.com.system.pojo.DailyResume;
import co.com.system.service.CoordinatesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public abstract class WeatherCalculatorStrategy {

  protected CoordinatesService coordinatesService;

  public abstract DailyResume calculateDailyWeather(SolarSystem solarSystem, DailyResume dailyResume);
}
