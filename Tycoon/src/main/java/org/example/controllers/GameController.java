package org.example.controllers;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.model.Player;
import org.example.view.console.GameView;

import java.io.IOException;


public class GameController {
    private Player player;
    private GameView view;
    private Screen screen;
    String[] options = {"Opcja 1", "Opcja 2", "Wyjście"};
    int selectedOption = 0;

    public GameController(GameView view, Screen screen) {
        this.view = view;
        this.screen = screen;
    }

    public void startGameLoop() {
        while (true) {
            try {
                KeyStroke keyStroke = screen.readInput();
                if (keyStroke.getKeyType() == KeyType.Escape) {
                } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    selectedOption = (selectedOption - 1 + options.length) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    selectedOption = (selectedOption + 1) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    switch (selectedOption) {
                        case 0:
                            System.out.println("Wybrano Opcję 1");
                            break;
                        case 1:
                            System.out.println("Wybrano Opcję 2");
                            break;
                        case 2:
                            System.out.println("Zamykam program...");
                            screen.close();
                    }
                }
                view.display(options, selectedOption);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}







