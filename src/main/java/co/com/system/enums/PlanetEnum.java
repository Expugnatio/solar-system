package co.com.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlanetEnum {
  FERENGI ("Ferengi"),
  BETASOIDE("Betasoide"),
  VULCANO("Vulcano");

  String name;
}
