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
                    () -> {
                        if (frame.getContentPane().getComponentCount() > 1) {
                            frame.getContentPane().remove(1);
                        }

                        GameView gameView = new GameView(() -> cardLayout.show(frame.getContentPane(), "MainMenu"));
                        GameController gameController = new GameController(gameView);
                        gameController.startGameLoop();
                        frame.add(gameView, "GameView");
                        cardLayout.show(frame.getContentPane(), "GameView");

                        frame.revalidate();
                        frame.repaint();
                    },
                    () -> {
                        if (frame.getContentPane().getComponentCount() > 1) {
                            frame.getContentPane().remove(1);
                        }

                        GameView gameView = new GameView(() -> cardLayout.show(frame.getContentPane(), "MainMenu"));
                        GameController gameController = new GameController(gameView);

                        SaveController saveController = new SaveController(null, null, gameController.getPlayer(), gameController.getQueueController());
                        saveController.loadGame();

                        gameController.startGameLoop();
                        frame.add(gameView, "GameView");
                        cardLayout.show(frame.getContentPane(), "GameView");

                        frame.revalidate();
                        frame.repaint();
                    }
            );


            frame.getContentPane().add(mainMenuView, "MainMenu");
            cardLayout.show(frame.getContentPane(), "MainMenu");

            frame.setVisible(true);
        });
    }
}

