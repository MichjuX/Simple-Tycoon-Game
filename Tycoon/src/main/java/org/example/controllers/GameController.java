package org.example.controllers;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.model.Player;
import org.example.savegame.SaveController;
import org.example.service.NavigationHandler;
import org.example.view.console.GameMenu;
import org.example.view.console.GameSecondView;
import org.example.view.console.GameView;
import org.example.view.console.LeaveGame;

import java.io.IOException;

public class GameController {
    private SaveController saveController;
    private Player player;
    private GameView _view1;
    private GameSecondView _view2;
    private GameMenu _menu;
    private LeaveGame _leave;
    QueueController queueController;
    private Screen screen;
    private int currentPage = 0;
    private boolean paused = false;

    private NavigationHandler menuNavigation;
    private NavigationHandler mainNavigation;
    private NavigationHandler hiringNavigation;

    public GameController(GameView view, GameSecondView view2, GameMenu menu, LeaveGame leave, Screen screen) {
        this._view1 = view;
        this._view2 = view2;
        this._menu = menu;
        this._leave = leave;
        this.screen = screen;
        this.player = new Player();
        this.queueController = new QueueController(player);
        this.saveController = new SaveController(_view1, _view2, player, queueController);

        // Navigation handlers
        this.menuNavigation = new NavigationHandler(3); // Menu główne ma 3 opcje
        this.mainNavigation = new NavigationHandler(player.getWorkersCount() + 1); // Liczba pracowników + 1
        this.hiringNavigation = new NavigationHandler(view2.getWorkersCount() + 1); // Liczba możliwych zatrudnień + 1
    }

    public void startGameLoop() {
        boolean load = false;
        while (true) {
            try {
                _menu.display(menuNavigation.getSelectedOption());
                KeyStroke keyStroke = screen.readInput();

                if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    menuNavigation.moveUp();
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    menuNavigation.moveDown();
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    int selected = menuNavigation.getSelectedOption();
                    if (selected == 0) {
                        System.out.println("Wczytaj grę");
                        load = true;
                        break;
                    } else if (selected == 1) {
                        System.out.println("Nowa gra");
                        break;
                    } else if (selected == 2) {
                        System.out.println("Wyjdź");
                        screen.stopScreen();
                        System.exit(0);
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (load) {
            saveController.loadGame();
        }
        menuNavigation.resetSelection();
        startBalanceThread();
        startRefreshThread();
        startDishThread();
        startClientThread();

        // Główna pętla gry
        while (true) {
            try {
                KeyStroke keyStroke = screen.readInput();
                handleGameInput(keyStroke);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
            } else if (selected > 0 && selected < _view2.getWorkersCount() + 1) {
                player.buy(selected - 1);
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
                            saveController.saveGame(); // Zapis gry
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


    private void upgrade(int index) {
        player.upgradeWorker(index);
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


    private void startRefreshThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100); // Odświeżanie ekranu co 100ms
                    if (currentPage == 1) {
                        _view2.display(hiringNavigation.getSelectedOption(), getBalance(), player, queueController);
                    } else if (currentPage == 0) {
                        _view1.display(mainNavigation.getSelectedOption(), getBalance(), player, queueController);
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