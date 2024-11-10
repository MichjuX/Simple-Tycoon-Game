package org.example.controllers;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import org.example.model.Player;
import org.example.savegame.SaveController;
import org.example.view.console.GameMenu;
import org.example.view.console.GameSecondView;
import org.example.view.console.GameView;
import org.example.view.console.LeaveGame;

import java.io.IOException;
import java.util.Random;

public class GameController {
    private SaveController saveController;
    private Player player;
    private GameView _view1;
    private GameSecondView _view2;
    private GameMenu _menu;
    private LeaveGame _leave;
    private QueueController queueController;
    private Screen screen;
    private int currentPage = 0;
    private int _selectedOption = 0;
    private boolean paused = false;

    public GameController(GameView view, GameSecondView view2, GameMenu menu, LeaveGame leave, Screen screen) {
        this._view1 = view;
        this._view2 = view2;
        this._menu = menu;
        this._leave = leave;
        this.screen = screen;
        this.player = new Player();
        this.queueController = new QueueController(player);
        this.saveController = new SaveController(_view1, _view2, player, queueController);
    }

    public void startGameLoop() {
        // Inicjalizacja gry (menu)
        boolean load = false;
        while(true){
            try {
                _menu.display(_selectedOption);
                KeyStroke keyStroke = screen.readInput();
                if(keyStroke.getKeyType() == KeyType.ArrowUp){
                    basic_minus();
                }
                else if(keyStroke.getKeyType() == KeyType.ArrowDown){
                    basic_plus();
                }
                else if(keyStroke.getKeyType() == KeyType.Enter){
                    if (_selectedOption == 0) {
                        System.out.println("Wczytaj grę");
                        load = true;
                        break;
                    }
                    else if (_selectedOption == 1) {
                        System.out.println("Nowa gra");
                        break;
                    }
                    else if (_selectedOption == 2) {
                        System.out.println("Wyjdź");
                        screen.stopScreen();
                        System.exit(0);
                        return;
                    }
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        _selectedOption = 0;
        if(load){
            saveController.loadGame();
        }
        startBalanceThread();
        startRefreshThread();
        startDishThread();
        startClientThread();
        while (true) {
            try {
                KeyStroke keyStroke = screen.readInput();
                if (keyStroke.getKeyType() == KeyType.Escape) { // Spaghetti time :))))) Nie wiem jak to zrobić inaczej
                    _selectedOption = 0;
                    while(true) {
                        currentPage = 2;
                        keyStroke = screen.readInput();
                        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                            basic_minus();
                        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                            basic_plus();
                        } else if (keyStroke.getKeyType() == KeyType.Escape) {
                            currentPage = 0;
                            break;
                        } else if (keyStroke.getKeyType() == KeyType.Enter) {
                            if (_selectedOption == 0) {
                                System.out.println("Zapisz i wyjdź");
                                saveController.saveGame();
                                System.out.printf("Zamykam grę");
                                screen.stopScreen();
                                System.exit(0);
                                break;
                            } else if (_selectedOption == 1) {
                                System.out.println("Wyjdź bez zapisu");
                                screen.stopScreen();
                                System.exit(0);
                                break;
                            } else if (_selectedOption == 2) {
                                System.out.println("Anuluj");
                                currentPage = 0;
                                break;
                            }
                            break;
                        }
                    }
//                    saveController.saveGame();
//                    System.out.printf("Zamykam grę");
//                    screen.stopScreen();
//                    System.exit(0);
                } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    System.out.println("gura");
                    minus();
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    System.out.println("duł");
                    plus();
                }
                else if (keyStroke.getKeyType() == KeyType.Enter) {
                    if(currentPage == 0) { // kiedy jest pierwszy widok (ulepszanie)
                        if (_selectedOption == 0) {
                            System.out.println("zatrudniam");
                            currentPage = 1;
                            System.out.println(currentPage);
                        } else if (_selectedOption > 0 && _selectedOption < player.getWorkersCount() + 1) {
                            upgrade(_selectedOption - 1);
                            System.out.println(player.get_workers().get(_selectedOption - 1).getLevel());
                        } else {
                            System.out.print("no to koniec :(");
                            return;
                        }
                    }
                    else if(currentPage == 1){ // kiedy jest drugi widok (zatrudnianie)
                        if (_selectedOption == 0) {
                            System.out.println("wracam");
                            currentPage = 0;
                            System.out.println(currentPage);
                        } else if (_selectedOption > 0 && _selectedOption < _view2.getWorkersCount() + 1) {
                            player.buy(_selectedOption - 1);
                        } else {
                            System.out.print("no to koniec :(");
                            return;
                        }
                    }
                    else if(currentPage == 2){
                        System.out.println("menu główne");
                        currentPage = 0;
                        System.out.println(currentPage);
                    }
                }
                System.out.println(_selectedOption);
                    // Nie wywołujemy tu display, to będzie robione w innym wątku
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void basic_plus() { // strzałka w dół
        if (_selectedOption < 2) {
            _selectedOption++;
        }
        else {
            _selectedOption = 0;
        }
    }

    private void basic_minus() { // strzałka w góre
        if (_selectedOption > 0) {
            _selectedOption--;
        }
        else {
            _selectedOption = 2;
        }
    }

    private void plus(){ // strzałka w dół
        if(_selectedOption < player.getWorkersCount() && currentPage == 0){
            _selectedOption++;
        }
        else if(currentPage == 1 && _selectedOption < _view2.getWorkersCount()){
            _selectedOption++;
        }
        else {
            _selectedOption = 0;
        }
    }
    private void minus(){ // strzałka w góre
        if(_selectedOption > 0){
            _selectedOption--;
        }
        else if(currentPage == 1){
            _selectedOption = _view2.getWorkersCount();
        }
        else {
            _selectedOption = player.getWorkersCount();
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
                    if(currentPage == 1){
                        _view2.display(_selectedOption, getBalance(), player, queueController);
                    }
                    else if(currentPage == 0){
                        _view1.display(_selectedOption, getBalance(), player, queueController);
                    }
                    else {
                        _leave.display(_selectedOption);
                    }

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
                if(!paused) {
                    try {
                        Thread.sleep(player.calculateSpeed(1, 10000)); // Kelner obsługuje klienta co X sekund
                        queueController.giveDishToClient(); // Obsługujemy zamówienia
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Przerywamy wątek w przypadku przerwania
                        break;
                    }
                }
            }
        }).start();
    }
    private void startDishThread() { // Wątek, który odpowiada za przygotowywanie dań
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(player.calculateSpeed(2, 10000)); // Kucharz tworzy danie co X sekund
                    queueController.addDish(player.getPreciseWorkersCount()); // Dodajemy danie do kolejki
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek w przypadku przerwania
                    break;
                }
            }
        }).start();
    }
    private void startClientThread() {
        Random random = new Random();
        // Wątek, który odpowiada za przychodzenie klentów
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(player.calculateSpeed(3, 5000)); // Kelner obsługuje klienta od 5 do 10 sekund
                    queueController.addClient(); // Dodajemy danie do kolejki
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Przerywamy wątek w przypadku przerwania
                    break;
                }
            }
        }).start();
    }



    // Synchronizowany dostęp do salda
//    private synchronized void incrementBalance() {
//        player.increaseBalance();
//    }

    // Synchronizowany dostęp do salda
    public synchronized double getBalance() {
        return player.getBalance();
    }
}
