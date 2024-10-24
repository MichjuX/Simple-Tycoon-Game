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
    public GameView(TerminalScreen screen) {
        this.screen = screen;

    }
    public void display(String[] options, int selectedOption){
        try {
            screen.clear();
            TextGraphics textGraphics = screen.newTextGraphics();

            for (int i = 0; i < options.length; i++) {
                String displayText = (i == selectedOption) ? "> " + options[i] : "  " + options[i];

                // Ustawiamy kolor dla wybranej opcji
                if (i == selectedOption) {
                    textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                } else {
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                textGraphics.putString(1, i, displayText); // Rysowanie tekstu na ekranie
            }
            screen.refresh();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






