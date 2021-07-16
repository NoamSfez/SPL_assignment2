package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

	private long duration;
	
	public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
    	subscribeEvent(BombDestroyerEvent.class, (t)->{
    		try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				System.out.println("initialize lando");
			}
    		complete(t, true);
    	});
    	
    	subscribeBroadcast(TerminationBroadcast.class, (o)->{
    		diary.setLandoTerminate(System.currentTimeMillis());
    		this.terminate();
    		});
    }
}
