package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok ewok;

    @BeforeEach
    void setUp() {
        ewok=new Ewok(1);
    }

    @Test
    void acquire() {
        assertTrue(ewok.available.get());
        ewok.acquire();
        assertFalse(ewok.available.get());
    }

    @Test
    void release() {
        assertTrue(ewok.available.get());
    }

    @Test
    void testAll(){
        assertTrue(ewok.available.get());
        ewok.acquire();
        assertFalse(ewok.available.get());
        ewok.release();
        assertTrue(ewok.available.get());
    }
}