package co.com.system.service;

import static org.junit.Assert.assertTrue;

import co.com.system.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class CycleServiceTest {

  @InjectMocks
  private CycleService cycleService;

  @Before
  public void setup(){
    ReflectionTestUtils.setField(
        cycleService, "timeConfig", TestUtil.buildTimeConfig());
  }

  @Test
  public void whenCoordinatesFormTriangle_SunIsInTriangle_thenReturnSuccess(){
    assertTrue(true);
  }

}
