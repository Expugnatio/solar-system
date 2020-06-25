package co.com.system.config;

import co.com.system.enums.PlanetEnum;
import co.com.system.pojo.Planet;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "planets.config")
@Getter
@Setter
public class SolarSystem {
  Map<PlanetEnum, Planet> planets;
}
