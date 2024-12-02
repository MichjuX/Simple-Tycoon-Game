package org.example.service;

import org.example.model.Player;
import org.example.view.gui.GameView;
import org.example.view.gui.UpgradeCallback;

import javax.swing.*;

public class GameService implements UpgradeCallback {
    private Player player;
    private GameView _view;
    QueueService queueController;
    private int currentPage = 0;
    private boolean paused = false;
    private int gameTick = 1;
    private int _timeSpeed = 1024;
    private double _timeSpeedFactor = 1;

    public GameService(org.example.view.gui.GameView view) {
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
        _view.updateProfit(player.getDisplayedProfit());
        _view.updateWorkerLists(player.get_workers());
    }
    public void changeTimeSpeed(String change){

        if (change.equals("slower")){
            if(_timeSpeed<1024){
                _timeSpeed*=2;
                _timeSpeedFactor/=2;
            }
        }
        else if(change.equals("faster")){
            if(_timeSpeed>1){
                _timeSpeed/=2;
                _timeSpeedFactor*=2;
            }
        }
        System.out.println("changeTimeSpeed " + change + " " + _timeSpeed + " " + _timeSpeedFactor);
        _view.updateSpeedLabel(_timeSpeedFactor);
    }


//    private void startBalanceThread() {
//        new Thread(() -> {
//            while (true) {
//                if (!paused) {
//                    try {
//                        // Kelner obsługuje klienta co X milisekund
//                        Thread.sleep(player.calculateSpeed(1, waitTime));
//                        queueController.giveDishToClient(); // Obsługujemy zamówienia klientów
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt(); // Przerywamy wątek
//                        break;
//                    }
//                }
//            }
//        }).start();
//    }

//    private void startBalanceUpdateThread() {
//        new Thread(() -> {
//            while (true) {
//                try {
//                    // Odświeżaj co 100ms
//                    Thread.sleep(100);
//                    SwingUtilities.invokeLater(() -> {
//                        _view.updateBalance(getBalance(), player);
//                    });
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt(); // Przerywamy wątek
//                    break;
//                }
//            }
//        }).start();
//    }
    public void startGameLoop() {
            updateView();
            updateViewWorkers();
            startGameTick();
            _view.updateWorkerLists(player.get_workers());
    //    startGameThread();

    }


    private void upgrade(int index) {
        player.upgradeWorker(index);
    }
    private void updateViewWorkers() {
        _view.updateWorkerList(player.get_workers());
    }
    private final int waitTime = 500;
    private void startGameTick(){
        new Thread(() -> {
            while (true) {
                if (!paused) {
                    try {
                        Thread.sleep(_timeSpeed);
                        if (gameTick < 100000) {
                            gameTick++;
                        } else {
                            gameTick = 1;
                        }

                        int speed1 = player.calculateSpeed(1, waitTime);
                        int speed2 = player.calculateSpeed(2, waitTime);
                        int speed3 = player.calculateSpeed(3, waitTime);

                        if (speed1 != 0 && gameTick % speed1 == 0) {
                            queueController.giveDishToClient();
                        }
                        if (speed2 != 0 && gameTick % speed2 == 0) {
                            queueController.addDish(player.getPreciseWorkersCount());
                            _view.updateDishes(queueController.getQueueSize());
                        }
                        if (speed3 != 0 && gameTick % speed3 == 0) {
                            queueController.addClient();
                            _view.updateDayAndCustomers(player.getCurrentDay(), player.getCurrentHour(), queueController.getClientCount());
                        }
                        if (gameTick % 1 == 0) {
                            player.nextHour();
                            _view.updateDayAndCustomers(player.getCurrentDay(), player.getCurrentHour(), queueController.getClientCount());
                        }
                        if (gameTick % 100 == 0) {
                            SwingUtilities.invokeLater(() -> {
                                _view.updateBalance(getBalance(), player);
                            });
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }).start();
    }
    private void startGameThread() {
        new Thread(() -> {
            while (true) {
                try {
                    // Dodajemy krótką pauzę, aby uniknąć ciągłego działania pętli.
                    Thread.sleep(10);

                    // Synchronizacja gameTick
                    int currentTick;
                    synchronized (this) {
                        currentTick = gameTick;
                    }

                    int speed1 = player.calculateSpeed(1, waitTime);
                    int speed2 = player.calculateSpeed(2, waitTime);
                    int speed3 = player.calculateSpeed(3, waitTime);

                    if (speed1 != 0 && currentTick % speed1 == 0) {
                        System.out.println("Speed1: " + speed1);
                        queueController.giveDishToClient();
                    }
                    if (speed2 != 0 && currentTick % speed2 == 0) {
                        System.out.println("Speed2: " + speed2);
                        queueController.addDish(player.getPreciseWorkersCount());
                        _view.updateDishes(queueController.getQueueSize());
                    }
                    if (speed3 != 0 && currentTick % speed3 == 0) {
                        System.out.println("Speed3: " + speed3);
                        queueController.addClient();
                        _view.updateDayAndCustomers(player.getCurrentDay(), player.getCurrentHour(), queueController.getClientCount());
                    }
                    if (currentTick % 10 == 0) {
                        System.out.println("Game tick: " + currentTick);
                        player.nextHour();
                        _view.updateDayAndCustomers(player.getCurrentDay(), player.getCurrentHour(), queueController.getClientCount());
                    }
                    if (currentTick % 100 == 0) {
                        SwingUtilities.invokeLater(() -> {
                            _view.updateBalance(getBalance(), player);
                        });
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

//    private void startDishThread() {
//        new Thread(() -> {
//            while (true) {
//                try {
//                    // Kucharz przygotowuje danie co X milisekund
//                    Thread.sleep(player.calculateSpeed(2, waitTime));
//                    queueController.addDish(player.getPreciseWorkersCount()); // Dodajemy danie do kolejki
//                    _view.updateDishes(queueController.getQueueSize());
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt(); // Przerywamy wątek
//                    break;
//                }
//            }
//        }).start();
//    }
//
//
//    private void startClientThread() {
//        new Thread(() -> {
//            while (true) {
//                try {
//                    // Klient przychodzi co X milisekund
//                    Thread.sleep(player.calculateSpeed(3, waitTime));
//                    queueController.addClient(); // Dodajemy klienta do kolejki
//                    _view.updateDayAndCustomers(player.getCurrentDay(),player.getCurrentHour(),queueController.getClientCount());
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt(); // Przerywamy wątek
//                    break;
//                }
//            }
//        }).start();
//    }
//     private void startDaysThread(){
//
//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(1);
//                    player.nextHour();
//                    _view.updateDayAndCustomers(player.getCurrentDay(),player.getCurrentHour(),queueController.getClientCount());
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt(); // Przerywamy wątek
//                    break;
//                }
//            }
//        }).start();
//     }


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
