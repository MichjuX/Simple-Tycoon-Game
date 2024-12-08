package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void testReduceBalance() {
        player.reduceBalance(1000);
        assertEquals(9000, player.getBalance());
    }

    @Test
    void testInitialState() {
        assertEquals(10000, player.getBalance());
        assertEquals(4, player.getCurrentProfit());
        assertEquals(2, player.getWorkersCount());
        assertEquals("Kucharz", player.getWorkerName(0));
        assertArrayEquals(new int[]{0, 0, 0}, player.getPrefixNumber());
        assertArrayEquals(new int[]{1, 1, 0, 0}, player.getPreciseWorkersCount());
    }

    @Test
    void testIncreaseBalance() {
        player.increaseBalance(500);
        assertEquals(10.5, player.getBalance());

        player.setBalance(999);
        player.increaseBalance(1);
        assertEquals(1, player.getBalance(), 0.001);
        assertEquals(2, player.getBalancePrefix());
    }

}