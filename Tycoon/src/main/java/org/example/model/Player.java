package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class Player {
    private double balance;
    private double currentProfit = 0;
    private final int[] prefixNumber = {0,0};
    private List<Worker> workers;

    public Player() {
        this.balance = 100; // Starting balance
        workers = new ArrayList<>();
        workers.add(new Worker(1, 10, "Kucharz 1"));
        workers.add(new Worker(1, 10, "Kucharz 2"));
        workers.add(new Worker(1, 10, "Kelner 1"));
        workers.add(new Worker(1, 10, "Kelner 2"));

    }

    public int getBalancePrefix(){
        return prefixNumber[0];
    }
    public int getProfitPrefix(){
        return prefixNumber[1];
    }
    public double getBalance(){
        return balance;
    }
    public void reduceBalance(double amount){
        balance -= amount;
    }

    public void increaseBalance(){
        balance += currentProfit;
        if(balance*1000>=1000000){
            balance /= 1000;
            prefixNumber[0]++;
            currentProfit /= 1000;
            prefixNumber[1]++;
        }
    }
    public double getCurrentProfit(){
        return currentProfit;
    }

    public void increaseProfit(){
        currentProfit *= 2;
    }
    public void addWorker(Worker worker){
        workers.add(worker);
    }
    public void upgradeWorker(int index){
        if(_calculatePrice(workers.get(index).getUpgradeCost()) <= balance){
            currentProfit += _calculatePrice(workers.get(index).getIncome());
            balance -= _calculatePrice(workers.get(index).getUpgradeCost());
            workers.get(index).upgrade();
        }
        else{
            System.out.println("Not enough money");
        }

    }
    public List<Worker> getWorkers(){
        return workers;
    }
    private double _calculatePrice(double basePrice){
        for (int i = 1; i <= prefixNumber[1]; i++) {
            basePrice /= 1000;
        }
        System.out.println("price:" + basePrice);
        return basePrice;
    }
}

