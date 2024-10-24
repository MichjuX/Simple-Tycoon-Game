package org.example.controllers;

import org.example.model.Player;
import org.example.view.console.GameView;

import java.io.IOException;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.gui2.BasicWindow;

public class GameController {
    private Player player;
    private GameView view;
    private MultiWindowTextGUI gui;
    private Screen screen;
    private BasicWindow window;

    public GameController(Player player, GameView view, Screen screen) {
        this.player = player;
        this.view = view;
        this.gui = gui;
        this.screen = screen;
    }

    public void startGameLoop() throws IOException {

    }
}







