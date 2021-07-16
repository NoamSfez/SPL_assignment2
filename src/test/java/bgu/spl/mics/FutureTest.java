package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void testResolve() throws InterruptedException{
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertEquals(future.get(), str);
    }
    @Test
    public void testGet() throws InterruptedException {
    	assertFalse(future.isDone());
    	String str= "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
		assertEquals(str, future.get());
    			
    }
    @Test
    public void testIsDone() {
    	assertFalse(future.isDone());
    	String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
    }
    
    @Test
    public void testGet2() throws InterruptedException {
    	assertFalse(future.isDone());
    	assertNull(future.get(500,TimeUnit.MILLISECONDS));
        assertFalse(future.isDone());
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
		assertEquals(str, future.get(1, TimeUnit.SECONDS));
    }
    
    
    
    
    
}
