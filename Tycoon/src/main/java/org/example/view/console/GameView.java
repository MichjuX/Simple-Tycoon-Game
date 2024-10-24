package org.example.view.console;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.example.model.Player;
import com.googlecode.lanterna.screen.TerminalScreen;

public class GameView {
    TerminalScreen screen;
    TextGraphics textGraphics;
    private String[] prefixList = {"", "k", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "O", "N", "D"};
    public GameView(TerminalScreen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();

    }
    public void display(String[] options, int selectedOption, double balance, Player player) {
        try {
            screen.clear();

            for (int i = 0; i < options.length; i++) {
                String displayText = "";
                if(i<4) {
                    if(player.getWorkers().get(i).getLevel() == 1) {
                        displayText = (i == selectedOption) ? "> " + options[i] + " - Kup za " + player.getWorkers().get(i).getUpgradeCost() + "$" : "  " + options[i];
                    }
                    else if(player.getWorkers().get(i).getLevel() > 1) {
                        displayText = (i == selectedOption) ? "> " + options[i] + " - Ulepsz lvl " + player.getWorkers().get(i).getLevel() + " - za " + player.getWorkers().get(i).getUpgradeCost() + "$" : "  " + options[i];
                    }
                }
                else {
                    displayText = (i == selectedOption) ? "> " + options[i] : "  " + options[i];
                }
                if (i == selectedOption) {
                    textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                } else {
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                textGraphics.putString(1, i+1, displayText); // Rysowanie tekstu na ekranie
            }
            textGraphics.putString(60, 1, String.format("Stan konta: %.2f%s$", balance, prefixList[player.getBalancePrefix()]));


            textGraphics.putString(60, 2, String.format("%.2f%s$/s", player.getCurrentProfit(), prefixList[player.getProfitPrefix()]));

            screen.refresh();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






