package bgu.spl.mics.application.passiveObjects;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private static Ewok[] ewoksArr;
    private static int size = -1;

    private Ewoks() {}

    private static class EwoksHolder {
        private static final Ewoks instance = new Ewoks();
    }

    public static Ewoks getInstance(){
        return EwoksHolder.instance;
    }

    public static void setSize(int newsize){
        if(size == -1) {
            size = newsize;
            ewoksArr = new Ewok[size + 1];
            for (int i = 1; i <= size; i++)
                ewoksArr[i] = new Ewok(i);
        }
    }

    public boolean acquireEwok(int serial){
        if(ewoksArr[serial].available.compareAndSet(true,false)) {
            ewoksArr[serial].acquire();
            return true;
        }
        return false;
    }

    public void releaseEwok(int serial){
        if(!ewoksArr[serial].available.get())
            ewoksArr[serial].release();
    }
}