package org.example.view.console;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import org.example.service.QueueService;
import org.example.model.Player;
import com.googlecode.lanterna.screen.TerminalScreen;

public class GameView {
    TerminalScreen screen;
    TextGraphics textGraphics;
    private final String[] _prefixList = {"", "k", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "O", "N", "D"};
    public GameView(TerminalScreen screen) {
        this.screen = screen;
        this.textGraphics = screen.newTextGraphics();

    }
    public void display(int selectedOption, double balance, Player player, QueueService queueController) {
        try {
            screen.clear();

            for (int i = 0; i < player.getWorkersCount()+1; i++) {
                String displayText = "";
                if(i==0){
                    displayText = (i ==selectedOption) ? "> "
                            + "Zatrudnij" : " Zatrudnij";
                }
                if(i>0 && i<=player.getWorkersCount()) {
                    if(player.get_workers().get(i-1).getLevel() == 1) {
                        displayText = (i == selectedOption) ? "> "
                                + player.getWorkerName(i-1)
                                + " - Ulepsz lvl "
                                + player.get_workers().get(i-1).getLevel()
                                + " - za "
                                + player.get_workers().get(i-1).getUpgradeCost()
                                + "$"
                                : "  "
                                + player.getWorkerName(i-1)
                                + " lvl: "
                                + player.get_workers().get(i-1).getLevel()
                                + " - przychód: "
                                + player.get_workers().get(i-1).getIncome()
                                + "$/s";
                    }
                    else if(player.get_workers().get(i-1).getLevel() > 1) {
                        displayText = (i == selectedOption) ? "> "
                                + player.getWorkerName(i-1)
                                + " - Ulepsz lvl "
                                + player.get_workers().get(i-1).getLevel()
                                + " - za "
                                + player.get_workers().get(i-1).getUpgradeCost()
                                + "$"
                                : "  "
                                + player.getWorkerName(i-1)
                                + " lvl: "
                                + player.get_workers().get(i-1).getLevel()
                                + " - przychód: "
                                + player.get_workers().get(i-1).getIncome()
                                + "$/s";
                    }
                }
                else {
//                    displayText = (i == selectedOption) ? "> "
//                            + player.getWorkerName(i)
//                            : "  "
//                            + player.getWorkerName(i);
                }
                if (i == selectedOption) {
                    textGraphics.setBackgroundColor(TextColor.ANSI.GREEN);
                    textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                }
                else {
                    textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
//                if(i<player.getWorkersCount() && player.get_workers().get(i).getLevel() > 1) {
//                    textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
//                }
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
                            _prefixList[player.getDisplayedProfitPrefix()]));

            textGraphics.putString(55, 2,
                    String.format("%.2f%s$/klienta",
                            player.getDisplayedProfit(),
                    _prefixList[player.getDisplayedProfitPrefix()]));

            textGraphics.putString(55, 3,
                    String.format("Gotowe dania: %d",
                    queueController.getQueueSize()));
            textGraphics.putString(55, 4,
                    String.format("Klienci w kolecje: %d",
                    queueController.getClientCount()
                    ));
            textGraphics.putString(55, 5,
                    String.format("%s - %d:%d",
                            dayBuilder(player.getCurrentDay())
                            ,player.getCurrentHour()[0]
                            ,player.getCurrentHour()[1]
                    ));
            textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

            screen.refresh();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String dayBuilder(int globalDay){
        String dayString = "";
        int day = globalDay % 7;
        if(day == 0){
            dayString = "Poniedziałek";
        }
        else if(day == 1){
            dayString = "Wtorek";
        }
        else if(day == 2){
            dayString = "Środa";
        }
        else if(day == 3){
            dayString = "Czwartek";
        }
        else if(day == 4){
            dayString = "Piątek";
        }
        else if(day == 5){
            dayString = "Sobota";
        }
        else if(day == 6){
            dayString = "Niedziela";
        }
        return dayString;
    }
}






