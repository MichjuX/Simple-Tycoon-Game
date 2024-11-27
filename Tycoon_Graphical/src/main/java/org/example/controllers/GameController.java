package org.example.controllers;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.model.Player;
import org.example.savegame.SaveController;
import org.example.service.NavigationHandler;
import org.example.view.gui.GameView;
import org.example.view.gui.UpgradeCallback;

import javax.swing.*;
import java.io.IOException;

public class GameController implements UpgradeCallback {
    private SaveController saveController;
    private Player player;
    private GameView _view1;
    QueueController queueController;
    private Screen screen;
    private int currentPage = 0;
    private boolean paused = false;

    public GameController(org.example.view.gui.GameView view) {
        this._view1 = view;
        this.screen = screen;
        this.player = new Player();
        this.queueController = new QueueController(player);
        _view1.setUpgradeCallback(this);
    }
    @Override
    public void upgradeWorker(int index) {
        player.upgradeWorker(index); // Ulepszenie pracownika
        _view1.updateBalance(player.getBalance(), player); // Aktualizacja salda
        _view1.updateProfit(player.getCurrentProfit()); // Aktualizacja profitu
        _view1.updateWorkerLists(player.get_workers()); // Aktualizacja list pracowników
    }

    @Override
    public void buyWorker(int workerTypeId) {
        player.buy(workerTypeId); // Kupno pracownika
        _view1.updateBalance(player.getBalance(), player); // Aktualizacja salda
        _view1.updateProfit(player.getCurrentProfit()); // Aktualizacja profitu
        _view1.updateWorkerLists(player.get_workers()); // Aktualizacja list pracowników
    }


    public void startGameLoop() {

        startBalanceThread();
        startDishThread();
        startClientThread();
        startBalanceUpdateThread();
        updateViewWorkers();
        _view1.updateWorkerLists(player.get_workers());
        // Główna pętla gry
        while (true) {



        }
    }

    private void upgrade(int index) {
        player.upgradeWorker(index);
    }
    private void updateViewWorkers() {
        _view1.updateWorkerList(player.get_workers());
    }

    private int waitTime = 5000;
    private void startBalanceThread() {
        new Thread(() -> {
            while (true) {
                if (!paused) {
                    try {
                        // Kelner obsługuje klienta co X milisekund
                        Thread.sleep(player.calculateSpeed(1, waitTime));
                        queueController.giveDishToClient(); // Obsługujemy zamówienia klientów
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Przerywamy wątek
                        break;
                    }
                }
            }
        }).start();
    }

    private void startBalanceUpdateThread() {
        new Thread(() -> {
            while (true) {
                try {
                    // Odświeżaj co 100ms
                    Thread.sleep(100);
                    SwingUtilities.invokeLater(() -> {
                        _view1.updateBalance(getBalance(), player);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek
                    break;
                }
            }
        }).start();
    }

    private void startDishThread() {
        new Thread(() -> {
            while (true) {
                try {
                    // Kucharz przygotowuje danie co X milisekund
                    Thread.sleep(player.calculateSpeed(2, waitTime));
                    queueController.addDish(player.getPreciseWorkersCount()); // Dodajemy danie do kolejki
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek
                    break;
                }
            }
        }).start();
    }


    private void startClientThread() {
        new Thread(() -> {
            while (true) {
                try {
                    // Klient przychodzi co X milisekund
                    Thread.sleep(player.calculateSpeed(3, waitTime));
                    queueController.addClient(); // Dodajemy klienta do kolejki
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek
                    break;
                }
            }
        }).start();
    }


    public synchronized double getBalance() {
        return player.getBalance();
    }
}
