package co.com.system.handler;

import java.text.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SolarSystemExceptionHandler {

  @ExceptionHandler(ParseException.class)
  public ResponseEntity handleBacksideWrapperException(){
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

}
