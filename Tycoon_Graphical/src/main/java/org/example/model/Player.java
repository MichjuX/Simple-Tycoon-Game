package org.example.model;

import org.example.view.gui.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private double balance;
    private double currentProfit = 4;
    private double displayedProfit = 4;
    private int[] prefixNumber = {0,0,0};
    private List<Worker> _workers;
    private int[] _workersCount = {1,1,0,0};
    private int _currentDay = 1;
    private int[] _currentHour = {0,0};

    public Player() {
        this.balance = 10000; // Starting balance
        _workers = new ArrayList<>();
        _workers.add(new Worker(2, 50, "Kucharz", 0));
        _workers.add(new Worker(2, 50, "Kelner", 0));
    }

    public int getBalancePrefix(){
        return prefixNumber[0];
    }
    public int getProfitPrefix(){
        return prefixNumber[1];
    }
    public int getDisplayedProfitPrefix(){
        return prefixNumber[2];
    }
    public void setDisplayedProfit(double displayedProfit){
        this.displayedProfit = displayedProfit;
    }
    public double getDisplayedProfit(){
        return displayedProfit;
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
        if(calculatePrice(_workers.get(index).getUpgradeCost()) <= balance){
            currentProfit += calculatePrice(_workers.get(index).getIncome());
            displayedProfit += calculateDisplayedPrice(_workers.get(index).getIncome());
            if(displayedProfit*1000>=1000000){
                displayedProfit /= 1000;
                prefixNumber[2]++;
            }
            balance -= calculatePrice(_workers.get(index).getUpgradeCost());
            _workers.get(index).upgrade();
            System.out.println("ulepszono pracownika");
        }
        else{
            System.out.println("Not enough money");
        }

    }
    public List<Worker> get_workers(){
        return _workers;
    }
    public double calculatePrice(double basePrice){
        for (int i = 1; i <= prefixNumber[1]; i++) {
            basePrice /= 1000;
        }
        System.out.println("price:" + basePrice);
        return basePrice;
    }public double calculateDisplayedPrice(double basePrice){
        for (int i = 1; i <= prefixNumber[2]; i++) {
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
                System.out.println("zatrudniam");
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
        if (calculatePrice(price) <= balance) {
            _workersCount[num]++;
            addWorker(new Worker(income, upgrade, name, _workersCount[num]));
            balance -= calculatePrice(price);
            currentProfit+= calculatePrice(income);
            displayedProfit += calculateDisplayedPrice(income);
            if (displayedProfit * 1000 >= 1000000) {
                displayedProfit /= 1000;
                prefixNumber[2]++;
            }
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
//            System.out.println("temp: " + temp);
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

    public void setPrefixNumber(int[] prefixNumber){
        this.prefixNumber = prefixNumber;
    }
    public int[] getPrefixNumber(){
        return prefixNumber;
    }
    public int getCurrentDay(){
        return _currentDay;
    }
    public void nextDay(){
        if(_currentDay==364) {
            _currentDay = 1;
        }
        else {
            _currentDay++;
        }
    }
    public double calculateWorth(Customer customer, GameView gameView){
        if(customer.getDay() == _currentDay){
            gameView.updateSatisfaction(0);
            System.out.println(customer.getDay() + " 0 " + _currentDay);
            return currentProfit;
        }
        else if(customer.getDay() == _currentDay-1){
            gameView.updateSatisfaction(1);
            System.out.println(customer.getDay() + " 1 " + _currentDay);
            return currentProfit/2;
        }
        else{
            gameView.updateSatisfaction(2);
            System.out.println(customer.getDay() + " 2 " + _currentDay);
            return currentProfit/4;
        }
    }

    public int[] getCurrentHour(){
        return _currentHour;
    }
    public void nextHour(){
        if (_currentHour[0] == 23 && _currentHour[1] == 59){
            _currentHour[0] = 0;
            _currentHour[1] = 0;
            nextDay();
        }
        else if(_currentHour[1]<60){
            _currentHour[1]++;
        }
        else{
            _currentHour[1] = 0;
            _currentHour[0]++;
        }
    }

}


