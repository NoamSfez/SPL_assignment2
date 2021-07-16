package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import bgu.spl.mics.application.passiveObjects.Ewok;


public class EwokTest {
	
	private Ewok ewok;

	@BeforeEach
	public void setUp() throws Exception {
		ewok = new Ewok(613);
	}

	@Test
	public void testAcquire() {
		assertTrue(ewok.getAvailable());
		ewok.acquire();
		assertFalse(ewok.getAvailable());
	}
	public void testRelease() {
		ewok.acquire();
		ewok.release();
		assertTrue(ewok.getAvailable());
	}
	public void testGetSerialNumber() {
		assertTrue(ewok.getSerialNumber()==613);
	}
	public void testGetAvailable() {
		ewok.acquire();
		assertFalse(ewok.getAvailable());
		ewok.release();
		assertTrue(ewok.getAvailable());
	}
}
