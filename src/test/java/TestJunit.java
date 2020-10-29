import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class TestJunit {

    @Before
    public void setUp(){
        
    }
    @After
    public void tearDown(){


    }
    @Test
    public void test(){
        String goal = "Junit";
        assertEquals("Junit",goal);
    }


}
