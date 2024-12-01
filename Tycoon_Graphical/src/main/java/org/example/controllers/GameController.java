package org.example.controllers;

import org.example.model.Player;
import org.example.savegame.SaveController;
import org.example.service.QueueService;
import org.example.view.gui.GameView;
import org.example.view.gui.MainMenuView;
import org.example.view.gui.UpgradeCallback;

import javax.swing.*;

public class GameController implements UpgradeCallback {
    private SaveController saveController;
    private Player player;
    private GameView _view;
    private MainMenuView _mainMenuView;
    QueueService queueController;
    private int currentPage = 0;
    private boolean paused = false;

    public GameController(org.example.view.gui.GameView view) {
        this._view = view;
        this.player = new Player();
        this.queueController = new QueueService(player, _view);
        _view.setUpgradeCallback(this);
    }
    @Override
    public void upgradeWorker(int index) {
        player.upgradeWorker(index); // Ulepszenie pracownika
        updateView();
    }

    @Override
    public void buyWorker(int workerTypeId) {
        player.buy(workerTypeId); // Kupno pracownika
        updateView();
    }
    private void updateView() {
        _view.updateBalance(player.getBalance(), player);
        _view.updateProfit(player.getCurrentProfit());
        _view.updateWorkerLists(player.get_workers());
    }


    public void startGameLoop() {
        startBalanceThread();
        startDishThread();
        startClientThread();
        startBalanceUpdateThread();
        updateViewWorkers();
        startDaysThread();
        _view.updateWorkerLists(player.get_workers());
//        while (true) {
//
//
//
//        }
    }

    private void upgrade(int index) {
        player.upgradeWorker(index);
    }
    private void updateViewWorkers() {
        _view.updateWorkerList(player.get_workers());
    }

    private final int waitTime = 5000;
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
                        _view.updateBalance(getBalance(), player);
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
                    _view.updateDishes(queueController.getQueueSize());
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
                    _view.updateDayAndCustomers(player.getCurrentDay(),queueController.getClientCount());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek
                    break;
                }
            }
        }).start();
    }
     private void startDaysThread(){

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    player.nextDay();
                    _view.updateDayAndCustomers(player.getCurrentDay(),queueController.getClientCount());
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

    public Player getPlayer() {
        return player;
    }

    public QueueService getQueueController() {
        return queueController;
    }
}
