package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private static Diary diary;
    private AtomicInteger totalAttacks = new AtomicInteger();
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    public static Diary getInstance() {
        if (diary == null)
            diary = new Diary();
        return diary;
    }

    public void setTotalAttacks(int newAttacks, MicroService m) { // only HANSOLO or C3PO is allowed to set it
        if (m.getClass().equals(HanSoloMicroservice.class) || m.getClass().equals(C3POMicroservice.class))
            this.totalAttacks.set(totalAttacks.get()+newAttacks);
    }

    public void setHanSoloFinish(long HanSoloFinish, MicroService m) {
        if (m.getClass().equals(HanSoloMicroservice.class))
            this.HanSoloFinish = HanSoloFinish;
    }

    public void setC3POFinish(long C3POFinish, MicroService m) {
        if (m.getClass().equals(C3POMicroservice.class))
            this.C3POFinish = C3POFinish;
    }

    public void setR2D2Deactivate(long R2D2Deactivate, MicroService m) {
        if (m.getClass().equals(R2D2Microservice.class))
            this.R2D2Deactivate = R2D2Deactivate;
    }

    public void setLeiaTerminate(long LeiaTerminate, MicroService m) {
        if (m.getClass().equals(LeiaMicroservice.class))
            this.LeiaTerminate = LeiaTerminate;
    }

    public void setHanSoloTerminate(long HanSoloTerminate, MicroService m) {
        if (m.getClass().equals(HanSoloMicroservice.class))
            this.HanSoloTerminate = HanSoloTerminate;
    }

    public void setC3POTerminate(long C3POTerminate, MicroService m) {
        if (m.getClass().equals(C3POMicroservice.class))
            this.C3POTerminate = C3POTerminate;
    }

    public void setR2D2Terminate(long R2D2Terminate, MicroService m) {
        if (m.getClass().equals(R2D2Microservice.class))
            this.R2D2Terminate = R2D2Terminate;
    }

    public void setLandoTerminate(long LandoTerminate, MicroService m) {
        if (m.getClass().equals(LandoMicroservice.class))
            this.LandoTerminate = LandoTerminate;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "totalAttacks=" + totalAttacks +
                ", HanSoloFinish=" + HanSoloFinish +
                ", C3POFinish=" + C3POFinish +
                ", R2D2Deactivate=" + R2D2Deactivate +
                ", LeiaTerminate=" + LeiaTerminate +
                ", HanSoloTerminate=" + HanSoloTerminate +
                ", C3POTerminate=" + C3POTerminate +
                ", R2D2Terminate=" + R2D2Terminate +
                ", LandoTerminate=" + LandoTerminate +
                '}';
    }

    public AtomicInteger getNumberOfAttacks() {
        return totalAttacks;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }
    public long getC3POTerminate() {
        return C3POTerminate;
    }
    public long getR2D2Terminate() {
        return R2D2Terminate;
    }
    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public void resetNumberAttacks() {
        setTotalAttacks(0,new HanSoloMicroservice());
    }
}
