package org.example.view.console;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import org.example.controllers.QueueController;
import org.example.model.Player;

import java.util.Queue;

public class LeaveGame {
    TerminalScreen screen;
    TextGraphics textGraphics;
    public LeaveGame(TerminalScreen screen) {
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
                            + "Zapisz i wyjdź" : " Zapisz i wyjdź";
                }
                if(i==1) {
                    displayText = (i ==selectedOption) ? "> "
                            + "Wyjdź bez zapisu" : " Wyjdź bez zapisu";
                }
                if(i==2) {
                    displayText = (i ==selectedOption) ? "> "
                            + "Anuluj" : " Anuluj";
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






