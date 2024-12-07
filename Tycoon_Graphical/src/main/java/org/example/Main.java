package org.example;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import org.example.controllers.GameController;
import org.example.service.GameService;
import org.example.savegame.SaveController;
import org.example.view.console.GameMenu;
import org.example.view.console.GameSecondView;
import org.example.view.console.LeaveGame;
import org.example.view.gui.GameView;
import org.example.view.gui.MainMenuView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Restaurant Tycoon");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Object[] options = {"Tak", "Nie"};
                    int result = JOptionPane.showOptionDialog(
                            frame,
                            "Czy na pewno chcesz wyjść bez zapisu?", "Potwierdzenie",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options, options[1]);

                    if (result == JOptionPane.YES_OPTION) {
                        // Zamknij aplikację
                        System.exit(0);
                    }
                }
            });
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
        // Lanterna
        try {
            // Tworzenie terminala
            Font font = new Font("Consolas", Font.PLAIN, 20);
            SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.newInstance(font);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            terminalFactory.setTerminalEmulatorTitle("Restaurant Tycoon");
            terminalFactory.setTerminalEmulatorFontConfiguration(fontConfig);
            Terminal terminal = terminalFactory.createTerminal();
            TerminalScreen screen = new TerminalScreen(terminal);
            screen.startScreen();

            // Ustawienia menu
            org.example.view.console.GameView lanternaView = new org.example.view.console.GameView(screen);
            GameSecondView gameSecondView = new GameSecondView(screen);
            GameMenu menu = new GameMenu(screen);
            LeaveGame leaveGame = new LeaveGame(screen);


        // Swing
        // Usuń istniejący widok gry, jeśli istnieje
        if (frame.getContentPane().getComponentCount() > 1) {
            frame.getContentPane().remove(1);
        }

        GameService[] gameServiceHolder = new GameService[1];

        // Tworzenie widoku gry
        GameView gameView = new GameView();

        // Tworzenie kontrolera gry
        GameService gameService = new GameService(lanternaView, gameSecondView, menu, leaveGame, screen, gameView);
        gameServiceHolder[0] = gameService;

        if (loadGame) {
            gameView.setService(gameService);
            SaveController saveController = new SaveController(
                    gameService.getPlayer(), gameService.getQueueService()
            );
            saveController.loadGame();
        }

        gameView.setService(gameService);
        GameController gameController = new GameController(
                gameService,
                gameView,
                () -> cardLayout.show(frame.getContentPane(), "MainMenu"),
                () -> {
                    if (gameService != null) {
                        SaveController saveController = new SaveController(
                                gameService.getPlayer(), gameService.getQueueService()
                        );
                        saveController.saveGame();
                    }
                }
        );
        gameView.setController(gameController);

        // Rozpoczęcie pętli gry
        gameService.startGameLoop();
        gameService.getController(gameController);

        // Dodanie widoku gry do okna
        frame.add(gameView, "GameView");
        cardLayout.show(frame.getContentPane(), "GameView");

        frame.revalidate();
        frame.repaint();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
