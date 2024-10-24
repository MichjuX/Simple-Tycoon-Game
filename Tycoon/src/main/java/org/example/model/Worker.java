package org.example.model;

public class Worker {
    private int level;
    private double baseIncome;
    private double upgradeCost;

    public Worker(double baseIncome, double upgradeCost, String name) {
        this.level = 1;
        this.baseIncome = 1; // Starting income

        this.upgradeCost = 50; // Cost to upgrade
    }

    public double getIncome() {
        return baseIncome * level;
    }

    public double getUpgradeCost() {
        return upgradeCost * level; // Cost increases with each upgrade
    }

    public void upgrade() {
        level++;
    }

    public int getLevel() {
        return level;
    }
}
