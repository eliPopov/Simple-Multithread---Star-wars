package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.EndBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
private final long duration;
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
       subscribeEvent(BombDestroyerEvent.class,
               c -> {
                   try {
                       Thread.sleep(duration);
                   } catch (InterruptedException ignored){}
                   complete(c, true);
                   System.out.println("Lando has successfully bombed the star destroyer");
                   EndBroadcast ending = new EndBroadcast();
                   sendBroadcast(ending);
               });
       subscribeBroadcast(EndBroadcast.class, c->
       {
           System.out.println(getName()+"will now terminate");
           Diary.getInstance().setLandoTerminate( System.currentTimeMillis(), this);
       });
    }
}
