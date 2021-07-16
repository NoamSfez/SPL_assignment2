package bgu.spl.mics;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.

 * @param <T> */
public class MessageBusImpl implements MessageBus {
	
	private HashMapSafe<MicroService, Queue<Message>> microServices_Queues;
	private HashMapSafe<Class<? extends Event>, Queue<MicroService>> Events;
	private HashMapSafe<Class<? extends Broadcast>, ArrayList<MicroService>> Broadcasts;
	private HashMapSafe<Event, Future> futures;

	//A Singleton Thread Safe
	private static class SingletonMessageBusHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	
	private MessageBusImpl() {
		microServices_Queues= new HashMapSafe<MicroService, Queue<Message>>();
		Events = new HashMapSafe<Class<? extends Event>, Queue<MicroService>>();
		Broadcasts = new HashMapSafe<Class<? extends Broadcast>, ArrayList<MicroService>>();
		futures = new HashMapSafe<Event, Future>();
	}
	
	public static MessageBusImpl getInstance() {
		return SingletonMessageBusHolder.instance;
	}
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (Events) {
			if (!Events.containsKey(type))
				Events.put(type, new ConcurrentLinkedQueue<MicroService>());
			Queue<MicroService> q = Events.get(type);
			synchronized (q) {
				q.add(m);
				q.notifyAll();//in case someone awaits for microservice to subscribe
			}
		}
	}
	
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (Broadcasts) {
			if (!Broadcasts.containsKey(type))
				Broadcasts.put(type, new ArrayList<MicroService>());
			ArrayList<MicroService> bc = Broadcasts.get(type);
			synchronized (bc) {
				bc.add(m);
				System.out.println(m);
				bc.notifyAll();//in case someone awaits for microservice to subscribe
			}
		}
    }
		
	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> f = futures.remove(e);
		f.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		ArrayList<MicroService> subscribedMServices = Broadcasts.get(b.getClass());
		synchronized (subscribedMServices) {
			while(subscribedMServices.isEmpty()) {
				try {
					subscribedMServices.wait();
				} catch (InterruptedException e1) {}
			}
		}
		synchronized (microServices_Queues) {
			for (MicroService m : subscribedMServices) {
				Queue<Message> messageQ = microServices_Queues.get(m);
				System.out.println(m.getName() + " " + m + " " + messageQ);
				synchronized (messageQ) {
					messageQ.add(b);
					messageQ.notifyAll();
				}
			}
		}
			
	}
	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<MicroService> q = Events.get(e.getClass());
		synchronized (q) {
			while(q.isEmpty()) {
				try {
					q.wait();
				} catch (InterruptedException e1) {}
			}
			MicroService m = q.poll();
			q.add(m);//round-robin manner
			if (m==null) //The function pull() return null if the queue is empty
				throw new IllegalArgumentException("No one is registered for this event");
			Future<T> t = new Future<T>();
			futures.put(e, t);
			synchronized (microServices_Queues.get(m)) {
				microServices_Queues.get(m).add(e);
				microServices_Queues.get(m).notifyAll();
			}			
			return t;	
		}
	}

	@Override
	public void register(MicroService m) {
//		System.out.println(m.getName() + " " + m.hashCode());
		Queue<Message> q = new ConcurrentLinkedQueue<Message>();
		microServices_Queues.put(m, q);
	}

	@Override
	public void unregister(MicroService m) {
		synchronized (microServices_Queues) {
			microServices_Queues.remove(m);			
		}
		for (Class<? extends Event> key : Events.keySet()) {
			if(Events.get(key).contains(m))
				Events.get(key).remove(m);
		}
		for (Class<? extends Broadcast> key : Broadcasts.keySet()) {
			if(Broadcasts.get(key).contains(m))
				Broadcasts.get(key).remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		Queue<Message> q = microServices_Queues.get(m);
		synchronized (q) {
			while(q.isEmpty())
				q.wait();
			return q.poll();
		}
	}

}
