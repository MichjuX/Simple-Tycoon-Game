package org.example.view.console;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.example.model.Player;
import com.googlecode.lanterna.gui2.Label;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;

public class GameView {
    private Player player;
    TerminalScreen screen;
    TextGraphics textGraphics;
    public GameView(TerminalScreen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();

    }
    public void display(String[] options, int selectedOption, double balance, Player player) {
        try {
            screen.clear();

            for (int i = 0; i < options.length; i++) {
                String displayText = (i == selectedOption) ? "> " + options[i] + " - Kup" : "  " + options[i];

                if (i == selectedOption) {
                    textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                } else {
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                textGraphics.putString(1, i+1, displayText); // Rysowanie tekstu na ekranie
            }
            textGraphics.putString(60, 1, "Stan konta: " + balance + "$");
            textGraphics.putString(60, 2, player.getCurrentProfit() + "$/s");
            screen.refresh();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






