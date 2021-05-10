package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusTest {
    CountDownLatch attackCounter = new CountDownLatch(0);
    private MessageBusImpl messageBus;
    private HanSoloMicroservice han;

    @BeforeEach
    void setUp() {
        messageBus = MessageBusImpl.getInstance();
        han = new HanSoloMicroservice();
        // tests register
        messageBus.register(han);
    }

    @Test
    void complete() {
        Future<Boolean> f1 = new Future<>();
        List<Integer> serials = new ArrayList<>();
        AttackEvent e1 = new AttackEvent(serials, 2);
        han.complete(e1, true);
        assertTrue(f1.isDone());
    }

    @Test
    void sendBroadcast() {
        // testing to see if two different microservices which subscribe to the same type
        // of broadcast receive the broadcast from 'awaitMessage'
        HanSoloMicroservice n1 = new HanSoloMicroservice();
        HanSoloMicroservice solo = new HanSoloMicroservice();
        testBroadcast broadcast = new testBroadcast("hello");
        // also tests subscribeBroadcast
        han.subscribeBroadcast(broadcast.getClass(), (callback)->{});
        solo.subscribeBroadcast(broadcast.getClass(), (callback)->{});
        n1.sendBroadcast(broadcast);
        Broadcast b1=new testBroadcast(),b2=new testBroadcast();
        // also tests awaitMessage
        b1 = (Broadcast)messageBus.awaitMessage(han);
        b2 = (Broadcast)messageBus.awaitMessage(solo);
        assertEquals(broadcast,b1);
        assertEquals(broadcast,b2);
    }

    @Test
    void sendEvent() {
        HanSoloMicroservice solo = new HanSoloMicroservice();
        Future<Boolean> f1 = new Future<>();
        List<Integer> serials = new ArrayList<>();
        AttackEvent e1 = new AttackEvent(serials,1);
        // also tests subscribeEvent
        han.subscribeEvent(e1.getClass(), (callback)->{});
        solo.sendEvent(e1);
        AttackEvent e2=null;
        // also tests awaitMessage
        e2 = (AttackEvent)messageBus.awaitMessage(han);
        assertEquals(e1, e2);
    }
}