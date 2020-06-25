package co.com.system.service;

import co.com.system.util.TestUtil;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class CycleServiceTest {

  @InjectMocks
  private CycleService cycleService;



  @Before
  void setup(){
    ReflectionTestUtils.setField(
        cycleService, "timeConfig", TestUtil.buildTimeConfig());
  }


}
