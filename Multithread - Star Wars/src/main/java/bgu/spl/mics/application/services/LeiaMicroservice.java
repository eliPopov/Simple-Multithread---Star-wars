package bgu.spl.mics.application.services;


import java.util.LinkedList;
import java.util.List;
import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.EndBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class LeiaMicroservice extends MicroService {
	private final Attack[] attacks;

	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(EndBroadcast.class, c -> {
            System.out.println(getName() + " will now terminate");
            Diary.getInstance().setLeiaTerminate(System.currentTimeMillis(), this);
        });
        try { //waiting for han solo and C3PO to subscribe.
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
        List<Future<Boolean>> attackResults = new LinkedList<Future<Boolean>>();
        for(Attack attack:attacks){
            AttackEvent attackEvent = new AttackEvent(attack.getSerials(), attack.getDuration());
            attackResults.add(sendEvent(attackEvent));
        }
        System.out.println("Leia has sent the attacks");
        try {
            AttackCounter.getInstance().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The attacks have been finished");
        DeactivationEvent deactivationEvent = new DeactivationEvent();
        sendEvent(deactivationEvent); //letting R2D2 know that the attacks are finished.
        System.out.println("Deactivation event has been sent");
    }
}
