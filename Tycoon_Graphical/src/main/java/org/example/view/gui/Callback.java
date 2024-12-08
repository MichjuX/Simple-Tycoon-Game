package org.example.view.gui;

import org.example.model.Worker;

public interface Callback {
    void upgradeWorker(int workerIndex);
    void buyWorker(int workerTypeId);

    void handleBuyAction(int workerTypeId);

    void handleReturnToMenu();

    void handleSaveGame();

    void decreaseGameSpeed();

    void increaseGameSpeed();

    void handleWorkerUpgrade(Worker worker);
    void handleBuyDecoration(int price, int decorationId);
}
