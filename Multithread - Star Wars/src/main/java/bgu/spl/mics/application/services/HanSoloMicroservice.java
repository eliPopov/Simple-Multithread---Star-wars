package bgu.spl.mics.application.services;

import bgu.spl.mics.AttackCounter;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.EndBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import java.util.List;


/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService{
    private int attackCount;
    private Ewoks ewoks = Ewoks.getInstance();


    public HanSoloMicroservice() {
        super("Han Solo");
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class,
                c -> {
                    boolean acquired;
                    List<Integer> ewokList = c.getEwokList();
                    for(Integer serialNum:ewokList){
                        do{
                            acquired = ewoks.acquireEwok(serialNum);
                        }while(!acquired);
                    }
                    try {
                        System.out.println(getName()+" handling attack");
                        Thread.sleep(c.getDuration());
                    } catch (InterruptedException ignored) {}
                    complete(c, true);
                    System.out.println(getName()+" completed");
                    AttackCounter.getInstance().countDown();
                    for(Integer serialNum:ewokList)
                        ewoks.releaseEwok(serialNum);
                    System.out.println("attack handled");
                    attackCount++;
                    Diary.getInstance().setHanSoloFinish(System.currentTimeMillis(), this);
                    Diary.getInstance().setTotalAttacks(attackCount, this);
                }
        );
        subscribeBroadcast(EndBroadcast.class,
                c-> {
            System.out.println(getName()+"will now terminate");
            Diary.getInstance().setHanSoloTerminate(System.currentTimeMillis(), this);
            this.terminate();
        });
    }
}
