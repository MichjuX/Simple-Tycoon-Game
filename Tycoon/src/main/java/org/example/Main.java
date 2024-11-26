package org.example;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;
import org.example.controllers.GameController;
import org.example.view.console.GameMenu;
import org.example.view.console.GameSecondView;
import org.example.view.console.GameView;
import org.example.view.console.LeaveGame;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        int mode = 0;


        try {
            Font font = new Font("Consolas", Font.PLAIN, 20);
            SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.newInstance(font);

            if(mode==0) {
                // Tworzenie terminala
                DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
                terminalFactory.setTerminalEmulatorTitle("Restaurant Tycoon");
                terminalFactory.setTerminalEmulatorFontConfiguration(fontConfig);
                Terminal terminal = terminalFactory.createTerminal();
                TerminalScreen screen = new TerminalScreen(terminal);
                screen.startScreen();

                // Ustawienia menu
                GameView gameView = new GameView(screen);
                GameSecondView gameSecondView = new GameSecondView(screen);
                GameMenu menu = new GameMenu(screen);
                LeaveGame leaveGame = new LeaveGame(screen);
                GameController gameController = new GameController(gameView, gameSecondView, menu, leaveGame, screen);
                gameController.startGameLoop();
            } else{
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel label = new JLabel("Restaurant Tycoon");
                frame.getContentPane().add(label, BorderLayout.NORTH);

                frame.pack();
                frame.setVisible(true);
            }





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


