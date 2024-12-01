package org.example;

import org.example.controllers.GameController;
import org.example.savegame.SaveController;
import org.example.view.gui.GameView;
import org.example.view.gui.MainMenuView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Restaurant Tycoon");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1920, 1080);
            frame.setLayout(new CardLayout());

            CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();

            MainMenuView mainMenuView = new MainMenuView(
                    () -> startGame(frame, cardLayout, false),
                    () -> startGame(frame, cardLayout, true)
            );

            frame.getContentPane().add(mainMenuView, "MainMenu");
            cardLayout.show(frame.getContentPane(), "MainMenu");

            frame.setVisible(true);
        });
    }

    private static void startGame(JFrame frame, CardLayout cardLayout, boolean loadGame) {
        // Usuń istniejący widok gry, jeśli istnieje
        if (frame.getContentPane().getComponentCount() > 1) {
            frame.getContentPane().remove(1);
        }

        GameController[] gameControllerHolder = new GameController[1];

        // Tworzenie widoku gry
        GameView gameView = new GameView(
                () -> cardLayout.show(frame.getContentPane(), "MainMenu"),
                () -> {
                    GameController gameController = gameControllerHolder[0];
                    if (gameController != null) {
                        SaveController saveController = new SaveController(
                                null, null, gameController.getPlayer(), gameController.getQueueController()
                        );
                        saveController.saveGame();
                    }
                }
        );

        // Tworzenie kontrolera gry
        GameController gameController = new GameController(gameView);
        gameControllerHolder[0] = gameController;

        if (loadGame) {
            gameView.setController(gameController);
            SaveController saveController = new SaveController(
                    null, null, gameController.getPlayer(), gameController.getQueueController()
            );
            saveController.loadGame();
        }

        gameView.setController(gameController);

        // Rozpoczęcie pętli gry
        gameController.startGameLoop();

        // Dodanie widoku gry do okna
        frame.add(gameView, "GameView");
        cardLayout.show(frame.getContentPane(), "GameView");

        frame.revalidate();
        frame.repaint();
    }
}
