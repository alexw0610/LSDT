import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.File;

import static org.junit.Assert.*;

public class TestJunit {


    @Before
    public void setUp(){

        Protocol.getInstance().deleteAllTables();
        String result = Protocol.getInstance().parseInput("nt TestDeleteTable(int paraA,bool paraB,float paraC)");
        System.out.println(result);
        result = Protocol.getInstance().parseInput("nt TestInsertTable(int paraA,bool paraB,float paraC,char(16) paraD)");
        System.out.println(result);
    }

    @After
    public void tearDown(){

        Protocol.getInstance().deleteAllTables();

    }

    @Test
    public void testEmptyCommand(){
        String result = Protocol.getInstance().parseInput("");
        assertEquals("Input not valid: Empty Command", result);
    }

    @Test
    public void testDeleteNonExisting(){
        String result = Protocol.getInstance().parseInput("dt wrongTable");
        System.out.println(result);
        assertEquals("Error deleting table [wrongTable]. Table does not exist!", result);
    }

    @Test
    public void testDeleteCommandError(){
        String result = Protocol.getInstance().parseInput("dterror");
        System.out.println(result);
        assertEquals("Input not valid: Command doesnt match Regex: "+Protocol.deleteRegex.pattern(), result);
    }

    @Test
    public void testCreateTable(){
        String result = Protocol.getInstance().parseInput("nt TestTable(int paraA,bool paraB,float paraC)");
        System.out.println(result);
        assertEquals("Created Table \"TestTable\"", result);
        assertTrue(new File("./TestTable.adb").exists());
    }

    @Test
    public void testDeleteTable(){
        String result = Protocol.getInstance().parseInput("dt TestDeleteTable");
        System.out.println(result);
        assertEquals("Successfully deleted TestDeleteTable.adb!\n"+
                "Successfully removed table [TestDeleteTable] from index file \n"+
                "Saved old indexfile to index.adb.old as a backup\n"+
                "WARNING: this backup gets overwritten each time a table is dropped!", result);
        assertFalse(new File("./TestTable1.adb").exists());
    }

    @Test
    public void testInsertTable(){
        String result = Protocol.getInstance().parseInput("it TestInsertTable 12 true 0.25 teststring");
        System.out.println(result);
        assertEquals("Saved Data to Table \"TestInsertTable\"!", result);

    }



}
