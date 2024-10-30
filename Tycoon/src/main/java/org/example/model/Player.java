package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private double balance;
    private double currentProfit = 2;
    private int[] prefixNumber = {0,0};
    private List<Worker> _workers;
    private int[] _workersCount = {1,0,0,0};

    public Player() {
        this.balance = 10000; // Starting balance
        _workers = new ArrayList<>();
        _workers.add(new Worker(2, 50, "Kucharz", 1));

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


    public int getWorkersCount(){
        return _workers.size();
    }
    public String getWorkerName(int id){
        return _workers.get(id).getName();
    }

    public void increaseBalance(double worth){
        balance += worth;
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
    public void addWorker(Worker worker){
        _workers.add(worker);
    }
    public void upgradeWorker(int index){
        if(_calculatePrice(_workers.get(index).getUpgradeCost()) <= balance){
            currentProfit += _calculatePrice(_workers.get(index).getIncome());
            balance -= _calculatePrice(_workers.get(index).getUpgradeCost());
            _workers.get(index).upgrade();
        }
        else{
            System.out.println("Not enough money");
        }

    }
    public List<Worker> get_workers(){
        return _workers;
    }
    private double _calculatePrice(double basePrice){
        for (int i = 1; i <= prefixNumber[1]; i++) {
            basePrice /= 1000;
        }
        System.out.println("price:" + basePrice);
        return basePrice;
    }
    public void buy(int id){
        switch (id) {
            case 0:
                buyHelper(50, 2, 50, "Kucharz", id);
                break;
            case 1:
                buyHelper(50, 2, 50, "Kelner", id);
                break;
            case 2:
                buyHelper(500, 10, 500, "Szef kuchni", id);
                break;
            case 3:
                buyHelper(5000, 100, 5000, "Marketingowiec", id);
                break;
        }
    }
    private void buyHelper(int price, int income, int upgrade, String name, int num){
        if (_calculatePrice(price) <= balance) {
            _workersCount[num]++;
            addWorker(new Worker(income, upgrade, name, _workersCount[num]));
            balance -= _calculatePrice(price);
            currentProfit+=_calculatePrice(income);
        }
        else{
            System.out.println("Not enough money");
        }
    }
    public int[] getPreciseWorkersCount(){
        return _workersCount;
    }

    public int calculateSpeed(int id, int waitTime){
        Random rn = new Random();
        if(_workersCount[id]==0){
            int temp = Math.abs(rn.nextInt()%waitTime);
            System.out.println("temp: " + temp);
            return temp;

        }
        return Math.abs(rn.nextInt()%waitTime/_workersCount[id]);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCurrentProfit(double currentProfit) {
        this.currentProfit = currentProfit;
    }

    public void setWorkers(List<Worker> workers) {
        _workers = workers;
    }

    public void setWorkersCount(int[] workersCount) {
        _workersCount = workersCount;
    }
    public int[] getPrefixNumber(){
        return prefixNumber;
    }
    public void setPrefixNumber(int[] prefixNumber){
        this.prefixNumber = prefixNumber;
    }
}


