package org.example.view.console;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import org.example.controllers.QueueController;
import org.example.model.Player;

import java.util.Queue;

public class GameMenu {
    TerminalScreen screen;
    TextGraphics textGraphics;
    public GameMenu(TerminalScreen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();

    }
    public void display(int selectedOption) {
        try {
            screen.clear();

            for (int i = 0; i < 3; i++) {
                String displayText = "";
                if(i==0){
                    displayText = (i ==selectedOption) ? "> "
                            + "Wczytaj grę" : " Wczytaj grę";
                }
                if(i==1) {
                    displayText = (i ==selectedOption) ? "> "
                            + "Nowa gra" : " Nowa gra";
                }
                if(i==2) {
                    displayText = (i ==selectedOption) ? "> "
                            + "Wyjdź z gry" : " Wyjdź z gry";
                }
                if (i == selectedOption) {
                    textGraphics.setBackgroundColor(TextColor.ANSI.GREEN);
                    textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                }
                else {
                    textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                textGraphics.putString(1, i+1, displayText);
            }

            screen.refresh();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






