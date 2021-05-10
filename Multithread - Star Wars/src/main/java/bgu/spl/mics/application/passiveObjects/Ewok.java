package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {

    int serialNumber;
    AtomicBoolean available;

    public Ewok(int serialNumber){
        this.serialNumber = serialNumber;
        available = new AtomicBoolean(true);
    }

    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
        available.set(false);
    }

    /**
     * release an Ewok
     */
    public synchronized void release() {
        available.set(true);
    }
}