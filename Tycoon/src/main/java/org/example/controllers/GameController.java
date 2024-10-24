package org.example.controllers;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.model.Player;
import org.example.view.console.GameView;

import java.io.IOException;
import java.util.List;

public class GameController {
    private Player player;
    private GameView view;
    private Screen screen;
    String[] options = {
            "Kucharz 1",
            "Kucharz 2",
            "Kelner 1",
            "Kelner 2",
            "Wyjście"};

    int selectedOption = 0;

    public GameController(GameView view, Screen screen) {
        this.view = view;
        this.screen = screen;
        this.player = new Player();
        startBalanceThread(); // Uruchamiamy wątek balansu
        startRefreshThread(); // Uruchamiamy wątek odświeżania ekranu
    }

    public void startGameLoop() {
        while (true) {
            try {
                KeyStroke keyStroke = screen.readInput();
                if (keyStroke.getKeyType() == KeyType.Escape) {
                    break; // Zakończ grę po naciśnięciu ESC
                } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    selectedOption = (selectedOption - 1 + options.length) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    selectedOption = (selectedOption + 1) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    if(selectedOption <= 4){
                        upgrade(selectedOption);
                        System.out.println(player.getWorkers().get(selectedOption).getLevel());
                    } else {
                        return;
                    }
                }
                // Nie wywołujemy tu display, to będzie robione w innym wątku
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void upgrade(int index) {
            player.upgradeWorker(index);
    }

    // Metoda do uruchamiania wątku odświeżającego ekran
    private void startRefreshThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100); // Odświeżamy ekran co 100ms
                    view.display(options, selectedOption, getBalance(), player);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek w przypadku przerwania
                    break;
                }
            }
        }).start();
    }

    // Metoda do uruchamiania wątku aktualizującego saldo
    private void startBalanceThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Uaktualniamy saldo co 1 sekundę
                    incrementBalance(); // Zwiększamy saldo
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek w przypadku przerwania
                    break;
                }
            }
        }).start();
    }



    // Synchronizowany dostęp do salda
    private synchronized void incrementBalance() {
        player.increaseBalance();
    }

    // Synchronizowany dostęp do salda
    public synchronized double getBalance() {
        return player.getBalance();
    }
}
