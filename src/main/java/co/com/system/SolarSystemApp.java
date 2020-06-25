package co.com.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
//@ComponentScan(basePackages = {"co.com.system"})
public class SolarSystemApp {

  public static void main(String[] args) {
    SpringApplication.run(SolarSystemApp.class, args);
  }
}