package org.example.view.console;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import org.example.controllers.QueueController;
import org.example.model.Player;

import java.util.Queue;

public class GameSecondView {
    TerminalScreen screen;
    TextGraphics textGraphics;
    private final String[] _prefixList = {"", "k", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "O", "N", "D"};
    private final String[] _workerNames = {"Kucharz", "Kelner", "Szef kuchni", "Marketingowiec"};
    private final int[] _workerCosts = {50, 50, 500, 5000};
    public GameSecondView(TerminalScreen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();

    }
    public void display(int selectedOption, double balance, Player player, QueueController queueController) {
        try {
            screen.clear();

            textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
            textGraphics.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
            textGraphics.putString(1, 10, "Kucharz - Gotuje dania");
            textGraphics.putString(1, 11, "Kelner - Podaje dania dla klientów i odbiera płatność");
            textGraphics.putString(1, 12, "Szef kuchni - Zwiększa tępo pracy kucharzy");
            textGraphics.putString(1, 13, "Marketingowiec - Zwiększa tępo przychodzenia klientów");
            textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);

            for (int i = 0; i < _workerNames.length+1; i++) {
                String displayText = "";
                if(i==0){
                    displayText = (i ==selectedOption) ? "> "
                            + "Wróć" : " Wróć";
                }
                if(i>0 && i<=_workerNames.length) {
                        displayText = (i == selectedOption) ? "> "
                                + _workerNames[i-1]
                                + " - Zatrudnij za "
                                + _workerCosts[i-1]
                                + "$" : "  "
                                + _workerNames[i-1] + " - " + _workerCosts[i-1] + "$";
                }
                if (i == selectedOption) {
                    textGraphics.setBackgroundColor(TextColor.ANSI.GREEN);
                    textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                }
                else {
                    textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                if(i<player.getWorkersCount() && player.get_workers().get(i).getLevel() > 1) {
                    textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                }
                if(i<player.getWorkersCount() && player.get_workers().get(i).getLevel() > 1 && i == selectedOption) {
                    textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                }
                textGraphics.putString(1, i+1, displayText);
            }
            textGraphics.setBackgroundColor(TextColor.ANSI.YELLOW);
            textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            textGraphics.putString(55, 1,
                    String.format("Stan konta: %.2f%s$",
                            balance,
                            _prefixList[player.getBalancePrefix()]));

            textGraphics.putString(55, 2,
                    String.format("%.2f%s$/klienta",
                            player.getCurrentProfit(),
                            _prefixList[player.getProfitPrefix()]));

            textGraphics.putString(55, 3,
                    String.format("Gotowe dania: %d",
                            queueController.getQueueSize()));
            textGraphics.putString(55, 4,
                    String.format("Klienci w kolecje: %d",
                            queueController.getClientCount()
                    ));
            textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

            screen.refresh();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getWorkersCount(){
        return _workerNames.length;
    }
}






