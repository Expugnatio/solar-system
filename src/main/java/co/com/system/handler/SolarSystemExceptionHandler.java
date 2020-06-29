package co.com.system.handler;

import java.text.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class SolarSystemExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity handleResponseStatusExceptions(ResponseStatusException e){
    return ResponseEntity.status(e.getStatus()).body(e.getReason());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleGeneralExceptions( Exception e){
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error, Sorry for the inconveniences");
  }

}
