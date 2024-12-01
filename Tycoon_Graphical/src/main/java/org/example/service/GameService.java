package org.example.service;

import org.example.model.Player;

public class GameService {
    private Player player;
    private QueueService queueController;

    public GameService(Player player, QueueService queueController) {
        this.player = player;
        this.queueController = queueController;
    }

    public void upgradeWorker(int index) {
        player.upgradeWorker(index);
    }

    public void processDishCreation(int waitTime) {
        queueController.addDish(player.getPreciseWorkersCount());
    }

    public void serveClient(int waitTime) {
        queueController.giveDishToClient();
    }

    public void addClientToQueue() {
        queueController.addClient();
    }

    public double getBalance() {
        return player.getBalance();
    }
}
