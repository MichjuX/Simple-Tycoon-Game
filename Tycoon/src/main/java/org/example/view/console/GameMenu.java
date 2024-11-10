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

            textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
            textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
            textGraphics.putString(5, 5, "____    ___ ___________  ____ __ __ ____   ____ ____  ______ ");
            textGraphics.putString(5, 6, "|    \\  /  _] ___/      |/    |  |  |    \\ /    |    \\|      | ");
            textGraphics.putString(5, 7, "|  D  )/  [(   \\_|      |  o  |  |  |  D  )  o  |  _  |      |");
            textGraphics.putString(5, 8, "|    /|    _]__  |_|  |_|     |  |  |    /|     |  |  |_|  |_|");
            textGraphics.putString(5, 9, "|    \\|   [_/  \\ | |  | |  _  |  :  |    \\|  _  |  |  | |  | ");
            textGraphics.putString(5, 10, "|  .  \\     \\    | |  | |  |  |     |  .  \\  |  |  |  | |  | ");
            textGraphics.putString(5, 11, "|__|\\_|_____| ___| |__| |__|__|\\__,_|__|\\_|__|__|__|__| |__| ");
            textGraphics.putString(5, 12, " ______ __ __    __  ___   ___  ____                           ");
            textGraphics.putString(5, 13, "|      |  |  |  /  ]/   \\ /   \\|    \\                          ");
            textGraphics.putString(5, 14, "|      |  |  | /  /|     |     |  _  |                         ");
            textGraphics.putString(5, 15, "|_|  |_|  ~  |/  / |  O  |  O  |  |  |                         ");
            textGraphics.putString(5, 16, "  |  | |___, /   \\_|     |     |  |  |                         ");
            textGraphics.putString(5, 17, "  |  | |     \\     |     |     |  |  |                         ");
            textGraphics.putString(5, 18, "  |__| |____/ \\____|\\___/ \\___/|__|__|                    ");

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






