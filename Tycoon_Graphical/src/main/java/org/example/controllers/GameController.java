package org.example.controllers;

import org.example.model.Worker;
import org.example.service.GameService;
import org.example.view.gui.Callback;
import org.example.view.gui.GameView;

import javax.swing.*;
import java.util.List;


public class GameController implements Callback {
    private Runnable returnToMenuCallback;
    private Runnable saveGameCallback;
    GameService gameService;
    GameView gameView;


    public GameController(GameService gameService, GameView gameView, Runnable returnToMenuCallback, Runnable saveGameCallback) {
        this.returnToMenuCallback = returnToMenuCallback;
        this.saveGameCallback = saveGameCallback;
        this.gameService = gameService;
        this.gameView = gameView;
        this.gameView.setCallback(this);
    }

    @Override
    public void upgradeWorker(int workerIndex) {
        gameService.upgradeWorker(workerIndex);
    }

    @Override
    public void buyWorker(int workerTypeId) {
        gameService.buyWorker(workerTypeId);
    }

    @Override
    public void handleBuyAction(int workerTypeId) {
        gameService.buyWorker(workerTypeId);
    }

    @Override
    public void handleReturnToMenu() {
        Object[] options = {"Tak", "Nie"};
        int choice = JOptionPane.showOptionDialog(null, "Czy na pewno chcesz wyjść bez zapisu?", "Potwierdzenie",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

        if (choice == JOptionPane.YES_OPTION) {
            if (returnToMenuCallback != null) {
                returnToMenuCallback.run();
            }
        }
    }

    @Override
    public void handleSaveGame() {
        if (saveGameCallback != null) {
            saveGameCallback.run();
        }
    }
    @Override
    public void increaseGameSpeed() {
        gameService.changeTimeSpeed("faster");
    }

    @Override
    public void handleWorkerUpgrade(Worker worker) {
        int globalIndex = gameService.getWorkers().indexOf(worker); // Znajduje indeks w globalnej liście
        if (globalIndex != -1) {
            upgradeWorker(globalIndex); // Wywołuje metodę ulepszania
        } else {
            System.out.println("Worker not found in the global list.");
        }
    }


    @Override
    public void decreaseGameSpeed() {
        gameService.changeTimeSpeed("slower");
    }


}
