package co.com.system.config;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NumbersFormat {

  @Bean
  public DecimalFormat getCalculationsDecimalFormat (){
    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
    otherSymbols.setDecimalSeparator('.');
    otherSymbols.setGroupingSeparator(',');
    return new DecimalFormat("0.0000", otherSymbols);
  }

}
