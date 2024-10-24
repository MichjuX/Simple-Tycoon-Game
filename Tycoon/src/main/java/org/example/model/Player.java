package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double balance;
    private List<Chef> chefs;

    public Player() {
        this.balance = 100; // Starting balance
        chefs = new ArrayList<>();
        chefs.add(new Chef()); // Initially one chef
    }

    public double getBalance() {
        return balance;
    }

    public void increaseBalance(double amount) {
        System.out.println("Before increase, balance: $" + balance); // Debug print
        this.balance += amount;
        System.out.println("After increase, balance: $" + balance); // Debug print
    }


    public void upgradeChef(int index) {
        Chef chef = chefs.get(index);
        double cost = chef.getUpgradeCost();
        if (balance >= cost) {
            chef.upgrade();
            balance -= cost;
        }
    }

    public double calculateTotalIncome() {
        double income = chefs.stream().mapToDouble(Chef::getIncome).sum();
        System.out.println("Calculated total income: $" + income); // Debug print
        return income;
    }


    // Add this method to return the list of chefs
    public List<Chef> getChefs() {
        return chefs;
    }

    public String getChefLevel() {
        return "Chef Level: " + chefs.get(0).getLevel();
    }

    public String getChefIncome() {
        return "Income: $" + chefs.get(0).getIncome() + "/sec";
    }

    public void decreaseBalance(int i) {
        balance -= i;
    }
}

