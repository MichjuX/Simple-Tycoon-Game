package org.example.service;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.controllers.GameController;
import org.example.model.Player;
import org.example.model.Worker;
import org.example.view.console.GameMenu;
import org.example.view.console.GameSecondView;
import org.example.view.console.LeaveGame;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameService {
    private Player player;
    private org.example.view.gui.GameView _swingView;
    private org.example.view.console.GameView _lanternaView1;
    private GameSecondView _lanternaView2;
    private GameMenu _menu;
    private LeaveGame _leave;
    private Screen screen;
    QueueService queueService;
    private int currentPage = 0;
    private boolean paused = false;
    private int gameTick = 1;
    private int _timeSpeed = 1024;
    private double _timeSpeedFactor = 1;
    private List<Worker> workers;
    private NavigationHandler menuNavigation;
    private NavigationHandler mainNavigation;
    private NavigationHandler hiringNavigation;
    private GameController controller;
    int[] displayedObjects = {0,0};

    // Swing
//    public GameService(org.example.view.gui.GameView view) {
//        this._swingView = view;
//        this.player = new Player();
//        this.queueService = new QueueService(player, _swingView, _lanternaView1);
//    }
    // Lanterna
    public GameService(org.example.view.console.GameView view, GameSecondView view2, GameMenu menu, LeaveGame leave, Screen screen, org.example.view.gui.GameView _swingView) {
        this. _lanternaView1 = view;
        this._lanternaView2= view2;
        this._menu = menu;
        this._leave = leave;
        this.screen = screen;
        this.player = new Player();
        this.queueService = new QueueService(player, _swingView ,_lanternaView1);
        this._swingView = _swingView;

        // Navigation handlers
        this.menuNavigation = new NavigationHandler(3); // Menu ma 3 opcje
        this.mainNavigation = new NavigationHandler(player.getWorkersCount() + 1); // Liczba pracowników + 1
        this.hiringNavigation = new NavigationHandler(view2.getWorkersCount() + 1); // Liczba możliwych zatrudnień + 1
    }

    public void upgradeWorker(int index) {
        player.upgradeWorker(index); // Ulepszenie pracownika
        updateView();
    }

    public void buyWorker(int workerTypeId) {
        player.buy(workerTypeId); // Kupno pracownika
        updateView();
    }
    public void buyDecoration(int price, int decorationId) {
        if (player.buyDecoration(price, decorationId)==0){
            _swingView.displayDecoration(decorationId);
        }

        updateView();
    }
    private void updateView() {
        _swingView.updateBalance(player.getBalance(), player);
        _swingView.updateProfit(player.getDisplayedProfit());
        _swingView.updateWorkerLists(player.get_workers());
        checkForWorkers();

        List<Worker> workers = player.get_workers();
        if (!workers.isEmpty()) {
            for (int i = 0; i < displayedObjects.length; i++) {
                if (displayedObjects[i] == 1) {
                    _swingView.displayObject(i);
                }
            }
        }
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
        _swingView.updateSpeedLabel(_timeSpeedFactor);
    }
    private void checkForWorkers() {

        List<Worker> workers = player.get_workers();
        System.out.println(workers);
        for (Worker worker : workers) {
            switch (worker.getName()) {
                case "Szef kuchni":
                    displayedObjects[0] = 1;
                    break;
                case "Marketingowiec":
                    displayedObjects[1] = 1;
                    break;
            }
        }
    }
    private void paintObjects() {
        int[] decorations = player.getDecorations();
        for (int i = 0; i < decorations.length; i++) {
            if (decorations[i] == 1) {
                _swingView.displayDecoration(i);
            }
        }
    }

    public void startGameLoop() throws IOException {
        checkForWorkers();
        paintObjects();
        updateView();
        updateViewWorkers();
        startGameTick();
        _swingView.updateWorkerLists(player.get_workers());
        mainNavigation.setMaxOptions(player.getWorkersCount() + 1);
        startRefreshThread();




        // Lanterna
        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    KeyStroke keyStroke = screen.readInput();
                    if (keyStroke != null) {
                        handleGameInput(keyStroke);
                    }
                    Thread.sleep(50); // Add a small delay to avoid excessive CPU usage
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        inputThread.start();
    }

    ///////////////////////////////////////////////////////////////
    // Sterowanie lanterną
    private void handleGameInput(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Escape) {
            handlePauseMenu();
        } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            handleNavigation("up");
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            handleNavigation("down");
        } else if (keyStroke.getKeyType() == KeyType.Enter) {
            handleEnterAction();
        }
    }
    private void handleNavigation(String direction) {
        if (currentPage == 0) {
            if ("up".equals(direction)) {
                mainNavigation.moveUp();
            } else {
                mainNavigation.moveDown();
            }
        } else if (currentPage == 1) {
            if ("up".equals(direction)) {
                hiringNavigation.moveUp();
            } else {
                hiringNavigation.moveDown();
            }
        }
    }

    private void handleEnterAction() {
        if (currentPage == 0) {
            int selected = mainNavigation.getSelectedOption();
            if (selected == 0) {
                System.out.println("Zatrudniam");
                currentPage = 1;
            } else if (selected > 0 && selected < player.getWorkersCount() + 1) {
                upgrade(selected - 1);
            } else {
                System.out.print("No to koniec :(");
            }
        } else if (currentPage == 1) {
            int selected = hiringNavigation.getSelectedOption();
            if (selected == 0) {
                System.out.println("Wracam");
                currentPage = 0;
            } else if (selected > 0 && selected < _lanternaView2.getWorkersCount() + 1) {
                player.buy(selected - 1);
                mainNavigation.setMaxOptions(player.getWorkersCount() + 1);
            } else {
                System.out.print("No to koniec :(");
            }
        }
    }
    private void handlePauseMenu() {
        paused = true; // Wstrzymanie gry
        menuNavigation.resetSelection(); // Resetowanie wyboru w menu pauzy
        currentPage = 2; // Ustawienie strony na menu pauzy

        while (paused) {
            try {
                // Wyświetlenie menu pauzy
                _leave.display(menuNavigation.getSelectedOption());

                // Oczekiwanie na wejście użytkownika
                KeyStroke keyStroke = screen.readInput();

                if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    menuNavigation.moveUp(); // Nawigacja w górę
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    menuNavigation.moveDown(); // Nawigacja w dół
                } else if (keyStroke.getKeyType() == KeyType.Escape) {
                    paused = false; // Powrót do gry
                    currentPage = 0; // Powrót do strony gry
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    // Obsługa wybranej opcji w menu pauzy
                    switch (menuNavigation.getSelectedOption()) {
                        case 0: // "Zapisz i wyjdź"
                            controller.handleSaveGame(); // Zapis gry
                            stopGame(); // Zakończenie gry
                            break;
                        case 1: // "Wyjdź bez zapisu"
                            stopGame(); // Zakończenie gry
                            break;
                        case 2: // "Anuluj"
                            paused = false; // Powrót do gry
                            currentPage = 0; // Powrót do strony gry
                            break;
                        default:
                            throw new IllegalStateException("Nieznana opcja: " + menuNavigation.getSelectedOption());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Obsługa błędu odczytu wejścia
            }
        }
    }
    private void stopGame() {
        try {
            screen.stopScreen(); // Zatrzymanie ekranu
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0); // Wyjście z gry
    }
    ///////////////////////////////////////////////////////////////

    private void upgrade(int index) {
        player.upgradeWorker(index);
    }
    private void updateViewWorkers() {
        _swingView.updateWorkerList(player.get_workers());
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
                            queueService.giveDishToClient();
                        }
                        if (speed2 != 0 && gameTick % speed2 == 0) {
                            queueService.addDish(player.getPreciseWorkersCount());
                            _swingView.updateDishes(queueService.getQueueSize());
                        }
                        if (speed3 != 0 && gameTick % speed3 == 0) {
                            queueService.addClient();
                            _swingView.updateDayAndCustomers(player.getCurrentDay(), player.getCurrentHour(), queueService.getClientCount());
                        }
                        if (gameTick % 1 == 0) {
                            player.nextHour();
                            _swingView.updateDayAndCustomers(player.getCurrentDay(), player.getCurrentHour(), queueService.getClientCount());
                        }
                        if (gameTick % 100 == 0) {
                            SwingUtilities.invokeLater(() -> {
                                _swingView.updateBalance(getBalance(), player);
                            });
                        }
                        if (gameTick % 100 == 0) {
                            if (currentPage == 1) {
                                _lanternaView2.display(hiringNavigation.getSelectedOption(), getBalance(), player, queueService);
                            } else if (currentPage == 0) {
                                _lanternaView1.display(mainNavigation.getSelectedOption(), getBalance(), player, queueService);
                            } else {
                                _leave.display(menuNavigation.getSelectedOption());
                            }
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
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

    public QueueService getQueueService() {
        return queueService;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public void getController(GameController gameController) {
        this.controller = gameController;
    }
    private void startRefreshThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100); // Odświeżanie ekranu co 100ms
                    if (currentPage == 1) {
                        _lanternaView2.display(hiringNavigation.getSelectedOption(), getBalance(), player, queueService);
                    } else if (currentPage == 0) {
                        _lanternaView1.display(mainNavigation.getSelectedOption(), getBalance(), player, queueService);
                    } else {
                        _leave.display(menuNavigation.getSelectedOption());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek
                    break;
                }
            }
        }).start();
    }
}
