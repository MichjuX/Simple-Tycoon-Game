package org.example;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;
import org.example.controllers.GameController;
import org.example.model.Player;
import org.example.view.console.GameMenu;
import org.example.view.console.GameSecondView;
import org.example.view.console.LeaveGame;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import org.example.view.gui.GameView;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        int mode = 1;


        try {
            Font font = new Font("Consolas", Font.PLAIN, 20);
            SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.newInstance(font);

//            if(mode==0) {
//                // Tworzenie terminala
//                DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
//                terminalFactory.setTerminalEmulatorTitle("Restaurant Tycoon");
//                terminalFactory.setTerminalEmulatorFontConfiguration(fontConfig);
//                Terminal terminal = terminalFactory.createTerminal();
//                TerminalScreen screen = new TerminalScreen(terminal);
//                screen.startScreen();
//
//                // Ustawienia menu
//                org.example.view.console.GameView gameView = new org.example.view.console.GameView(screen);
//                GameSecondView gameSecondView = new GameSecondView(screen);
//                GameMenu menu = new GameMenu(screen);
//                LeaveGame leaveGame = new LeaveGame(screen);
//                GameController gameController = new GameController(gameView, gameSecondView, menu, leaveGame, screen);
//                gameController.startGameLoop();
//            } else{

//                // ustawiam okno
//                JFrame frame = new JFrame();
//                frame.setTitle("Restaurant Tycoon");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                JLabel label = new JLabel("Restaurant Tycoon");
//                frame.getContentPane().add(label, BorderLayout.NORTH);
//
//                frame.pack();
//                frame.setResizable(true);
//                frame.setSize(800, 600);
//                frame.setVisible(true);
//
//                // ustawiam logo
//                ImageIcon icon = new ImageIcon("src/main/resources/images/logo.png");
//                frame.setIconImage(icon.getImage());
//
//                // ustawiam menu
//                frame.getContentPane().setBackground(Color.BLACK);


                GameView gameView = new GameView();
                GameController gameController = new GameController(gameView);
                gameController.startGameLoop();


//            }





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


