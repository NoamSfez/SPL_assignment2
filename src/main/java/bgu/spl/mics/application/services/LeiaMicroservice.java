package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	
	public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    protected void initialize() throws InterruptedException{
    	//Step 0: Initialization
    	subscribeBroadcast(TerminationBroadcast.class, (t)->{
    		diary.setLeiaTerminate(System.currentTimeMillis());
    		terminate();
    		});
    	Thread.sleep(1000);
    	//Step 1: Attacks
    	for (Attack a : attacks)
    		sendEvent(new AttackEvent(a));
		endStep();
    	
    	//Step 2: Deactivation
    	sendEvent(new DeactivationEvent());
		endStep();
		
		//Step3: Bomb Destroyer
		sendEvent(new BombDestroyerEvent());
		endStep();
		
		//Step 4: Termination
		sendBroadcast(new TerminationBroadcast());
    }

	private void endStep() throws InterruptedException {
		boolean flag = true;
		for (Future<?> f : futures) {
			if (!f.get().equals(true)) {
				flag= false;
				System.out.println(this.getName() + " " + f.get());
			}
		}
		if (!flag)
    		throw new IllegalArgumentException("You Lose");
		futures.clear();
	}
}
