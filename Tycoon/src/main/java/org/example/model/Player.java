package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double balance;
    private double currentProfit = 0;
    private int prefixNumber = 0;
    private String[] prefixList = {"", "k", "M", "B", "T", "Qa", "Qi", "Sx", "Sp", "O", "N", "D"};
    private List<Chef> chefs;

    public Player() {
        this.balance = 100; // Starting balance
        chefs = new ArrayList<>();
        chefs.add(new Chef());
    }

    public List<Object> getBalancePrefix(){
        List<Object> list = new ArrayList<>();
        list.add(prefixNumber);
        list.add(prefixList);
        return list;
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
            prefixNumber++;
            currentProfit /= 1000;
        }
    }
    public double getCurrentProfit(){
        return currentProfit;
    }

}

