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
    public void testEmpty(){
        String result = Protocol.getInstance().parseInput("lt");
        assertEquals("No tables found!", result);
    }

    @Test
    public void testEmptyCommand(){
        String result = Protocol.getInstance().parseInput("");
        assertEquals("Input not valid: Empty Command", result);
    }

    @Test
    public void testDelete(){
        String result = Protocol.getInstance().parseInput("dt");
        System.out.println(result);
        assertEquals("No tables found!", result);
    }


}
