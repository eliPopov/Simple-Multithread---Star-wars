package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private static MessageBusImpl bus;
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> messageQueues;
	private ConcurrentHashMap<Class<? extends Message>,Queue<MicroService>> eventSubscribers;
	private ConcurrentHashMap<Class<? extends Broadcast>,LinkedList<MicroService>> broadcastSubscribers;
	private HashMap<Event<?>,Future> futures;

	private MessageBusImpl() {
		messageQueues = new ConcurrentHashMap<>();
		eventSubscribers = new ConcurrentHashMap<>();
		broadcastSubscribers = new ConcurrentHashMap<>();
		futures = new HashMap<>();
	}

	private static class BusHolder {
		private static final MessageBusImpl instance = new MessageBusImpl();
	}

	public static MessageBusImpl getInstance() {
		return BusHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(messageQueues.containsKey(m)) { // checks if the microService registered
			if(!eventSubscribers.containsKey(type))
				eventSubscribers.put(type, new LinkedBlockingQueue<>());
			eventSubscribers.get(type).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(messageQueues.containsKey(m)) { // checks if the microService registered
			if(!broadcastSubscribers.containsKey(type))
				broadcastSubscribers.put(type,new LinkedList<>());
			broadcastSubscribers.get(type).add(m);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		futures.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if(!broadcastSubscribers.containsKey(b.getClass())) return;
		LinkedList<MicroService> subsList = broadcastSubscribers.get(b.getClass());
		for(MicroService microService : subsList) {
			messageQueues.get(microService).add(b);
			synchronized (microService){microService.notify();}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e){
		if(!eventSubscribers.containsKey(e.getClass())) throw new IllegalArgumentException("There are no subscribers for the event");
		Queue<MicroService> subsQueue = eventSubscribers.get(e.getClass());
		if(messageQueues.contains(subsQueue.peek()))
			messageQueues.get(subsQueue.peek()).add(e); // adding the event to the first microService in the queue
		synchronized (subsQueue.peek()) {
			subsQueue.peek().notify();
		}
		subsQueue.add(subsQueue.poll()); // move the front element to the back
		futures.put(e, new Future<>());
		return futures.get(e);
	}

	@Override
	public void register(MicroService m) {
		messageQueues.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		messageQueues.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m){
		synchronized (m) {
			while (messageQueues.get(m).isEmpty()) {
				try {
					m.wait();
				} catch (InterruptedException ignored) {
				}
			}
			return messageQueues.get(m).poll();
		}
	}
}