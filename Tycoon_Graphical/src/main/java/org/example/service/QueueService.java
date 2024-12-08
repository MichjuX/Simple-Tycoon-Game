package org.example.service;

import org.example.model.Customer;
import org.example.model.Dish;
import org.example.model.Player;
import org.example.view.gui.GameView;

import java.util.ArrayDeque;
import java.util.Queue;

public class QueueService {
    private Queue<Dish> dishesQueue = new ArrayDeque<>();
    private Queue<Customer> customerQueue = new ArrayDeque<>();
    private int clientCount = 0;
    private Player player;
    private GameView gameView;
    private org.example.view.console.GameView lanternaView;

    public QueueService(Player player, GameView gameView, org.example.view.console.GameView lanternaView){
        this.player = player;
        this.gameView = gameView;
        this.lanternaView = lanternaView;
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
//                Dish element = dishesQueue.poll(); // poll() zwraca i usuwa element z początku kolejki
//                System.out.println("Usunięto: " + element);
                clientCount--;

                Customer customer = customerQueue.peek();
                if(customer != null){
                    player.increaseBalance(player.calculateWorth(customer, gameView, lanternaView));
                    customerQueue.remove();
//                    System.out.println("Klient zjadł danie o wartości: " + player.calculateWorth(customer, gameView));
                }
            }
        }
    }
    public void addClient(){
        clientCount++;
        Customer customer = new Customer(player.getCurrentDay());
        customerQueue.add(customer);
    }
    public void setClientCount(int clientCount){
        this.clientCount = clientCount;
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
    public Queue<Customer> getCustomerQueue(){
        return customerQueue;
    }
}
