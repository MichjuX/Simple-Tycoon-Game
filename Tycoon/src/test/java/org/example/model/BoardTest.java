package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Test jednostowy wygenerowany poprzez najechanie na Board, kliknięcie more actions, wybór create test, wybranie JUnit 5
class BoardTest {
    @Test
    void shouldRetrieveTheSameVelue(){
        Game board = new Game();
        board.set(0, 0, CellSymbol.X);
        assert board.get(0, 0) == CellSymbol.X;
    }
    @Test
    void shouldThrowExceptionWhenGettingElementOutOfRange(){
        Game board = new Game();
        board.get(999, 999);
        assertThrows(Game.OutOfRangeException.class, () -> board.set(999, 999, CellSymbol.X));
    }
    @Test
    void shouldThrowExceptionWhenSettingElementOutOfRange(){
        Game board = new Game();
        board.set(999, 999, CellSymbol.X);
        assertThrows(Game.OutOfRangeException.class, () -> board.set(999, 999, CellSymbol.X));
    }

}