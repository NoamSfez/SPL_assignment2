package bgu.spl.mics.application.messages;
import java.util.List;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

public class AttackEvent implements Event<Boolean> {
	
	private Attack attack;
	
	public AttackEvent(Attack attack) {
		this.attack = attack;
	}
	
	public long getDurationAttack() {
		return attack.getDuration();
	}
	
	public List<Integer> getSerialsAttack(){
		return attack.getSerials();
	}
	
}
