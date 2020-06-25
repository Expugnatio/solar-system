package co.com.system.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlignmentResult {
  private boolean planetsAligned;
  private boolean alignedWithSun;
}
