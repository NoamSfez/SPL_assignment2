package bgu.spl.mics;

import jdk.nashorn.internal.codegen.CompilerConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.*;

public class MessageBusTest {

	MessageBusImpl mb;

	@BeforeEach
	public void setUp(){
		 mb = MessageBusImpl.getInstance();
	}

	@Test
	public void testComplete() throws InterruptedException {
		Event <Integer> e = new Integer_toTestEvent();
		Future <Integer> f = mb.sendEvent(e);
		mb.complete(e, 123);
		assertTrue(f.get()==123);
	}

	@Test /*tests the following methods:
	 register, subscribeBroadcast, sendBroadcast and awaitMessage*/
	public void testScenariBroadcast() throws InterruptedException {
		MicroService lando = new LandoMicroservice(2);
		mb.register(lando);
		MicroService hanHagever = new HanSoloMicroservice();
		mb.register(hanHagever);
		//lando calls the method
		//subscribeBroadcast(AdMatayBroadcast.class, this)
		//from his own subscribeBroadcast method
		mb.subscribeBroadcast(TerminationBroadcast.class, lando);
		//hanHagever calls the method
		//sendBroadcast(AdMatayBroadcast.class)
		//from his own sendBroadcast method
		TerminationBroadcast ad1 = new TerminationBroadcast();
		mb.sendBroadcast(ad1);
		Message ad2 = mb.awaitMessage(lando);
		assertTrue(ad2.equals(ad1));//important! checks if at2(Message) equals to at1(Broadcast) and not the other way.
    }

    @Test/*tests the following methods:
	 register, subscribeEvent, sendEvent and awaitMessage*/
	public void testScenarioEvent() throws InterruptedException {
		MicroService lando = new LandoMicroservice(2);
		mb.register(lando);
		MicroService c3po = new C3POMicroservice();
		mb.register(c3po);
		MicroService hanHagever = new HanSoloMicroservice();
		mb.register(hanHagever);
		mb.subscribeEvent(AttackEvent.class, lando);
		mb.subscribeEvent(AttackEvent.class, c3po);
		AttackEvent at1 = new AttackEvent(new Attack(new ArrayList<Integer>(),11));
		AttackEvent at2 = new AttackEvent(new Attack(new ArrayList<Integer>(),11));
		mb.sendEvent(at1);
		mb.sendEvent(at2);
		Message at3 = mb.awaitMessage(lando);
		Message at4 = mb.awaitMessage(c3po);
		//test also round robbin:
		if (at3.equals(at1))//lando received at1 & c3po at2
			assertEquals(at2, at4);//important! checks if at4(Message) equals to at2(Eevent) and not the other way.
		else if(at3.equals(at2))//c3po received at1 & lando at2
			assertEquals(at1, at4);//important! checks if at4(Message) equals to at1(Eevent) and not the other way.
		else
			fail();
	}
}
