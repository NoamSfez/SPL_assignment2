package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

	public HanSoloMicroservice() {
        super("Han");
    }

    @Override
    protected void initialize() {
    	subscribeEvent(AttackEvent.class, (t)->{
        	diary.incrementTotalAttacks();
    		try {			
    			Ewoks es = Ewoks.getInstance();
    			for (Ewok e : es.aquireSerials(t.getSerialsAttack())) {
    				Thread.sleep(t.getDurationAttack());
        			//e.doStuff
				}
    			complete(t, true);
        		diary.setHanSoloFinish(System.currentTimeMillis());
			} catch (InterruptedException e) {
				throw e;
			}    		
	    });
    	
    	subscribeBroadcast(TerminationBroadcast.class, (o)->{
    		diary.setHanSoloTerminate(System.currentTimeMillis());
    		this.terminate();
    		});
    }

	
}
