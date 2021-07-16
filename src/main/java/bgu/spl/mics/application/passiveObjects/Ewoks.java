package bgu.spl.mics.application.passiveObjects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.//
 */
public class Ewoks {
	
	private ArrayList<Ewok> ewoks;
	
	private static class SingletonHolder{
		private static Ewoks instance = new Ewoks();//only one thread can access this instance at a time.
	}

	private Ewoks() {
		ewoks = new ArrayList<Ewok>();
	}
	
	public void setEwoks(int quantity) {
		synchronized (ewoks) {
			for(int i = 1;i<=quantity;i++)
				ewoks.add(new Ewok(i));
		}
	}
	
	public static Ewoks getInstance() {
		return SingletonHolder.instance;
	}
		
	public List<Ewok> aquireSerials(List<Integer> Serials) throws InterruptedException {
		List<Ewok> myResource = new ArrayList<Ewok>(Serials.size());
		Collections.sort(Serials);//prevents deadlock of 2 microservices wait for eachother's ewoks.
		synchronized (ewoks) {
			for (Ewok ewok : ewoks) {
				if(Serials.contains(ewok.getSerialNumber())) {
					while(!ewok.getAvailable()) {
						ewoks.wait();
					}
					ewok.acquire();	
					myResource.add(ewok);
				}
			}
			return myResource;
		}
	}
	
	
	
}
