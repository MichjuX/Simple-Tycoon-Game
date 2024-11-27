package org.example.controllers;

import org.example.model.Dish;
import org.example.model.Player;

import java.util.ArrayDeque;
import java.util.Queue;

public class QueueController {
    private Queue<Dish> dishesQueue = new ArrayDeque<>();
    private int clientCount = 0;
    private Player player;

    public QueueController(Player player){
        this.player = player;
    }

    public void addDish(int[] cooks){
        for(int i=0; i<cooks[0]; i++){
            dishesQueue.add(new Dish(player.getCurrentProfit()));
//            System.out.println("Dodano: danie o wartości: " + player.getCurrentProfit());
        }
//        System.out.println("Queue size: " + dishesQueue.size());

    }

    public void giveDishToClient(){
        // Pobieranie i usuwanie elementu z początku kolejki (FIFO)
        for(int i=0; i<player.getPreciseWorkersCount()[1]; i++){
            if(!dishesQueue.isEmpty() && clientCount>0){
                Dish element = dishesQueue.poll(); // poll() zwraca i usuwa element z początku kolejki
//                System.out.println("Usunięto: " + element);
                clientCount--;
                player.increaseBalance(element.getWorth());
            }
        }
    }
    public void addClient(){
        clientCount++;
    }
    public int getQueueSize(){
        return dishesQueue.size();
    }
    public int getClientCount(){
        return clientCount;
    }
    public Queue<Dish> getQueue(){
        return dishesQueue;
    }
}
