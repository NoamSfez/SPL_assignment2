package bgu.spl.mics.application.services;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	
	public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize(){
    	subscribeEvent(AttackEvent.class, (t)->{
        	diary.incrementTotalAttacks();
    		try {
    			List<Ewok> es = Ewoks.getInstance().aquireSerials(t.getSerialsAttack());
    			Thread.sleep(t.getDurationAttack());
//    			for (Ewok e : es.aquireSerials(t.getSerialsAttack())) {
////        			e.doStuff()
//				}
    			complete(t, true);
        		diary.setC3POFinish(System.currentTimeMillis());
			} catch (InterruptedException e) {
//				System.out.println("attack c3po");
				throw e;
			}
	    });
    	subscribeBroadcast(TerminationBroadcast.class, (o)->{
    		diary.setC3POTerminate(System.currentTimeMillis());
    		this.terminate();
    		});
    }
    
}
