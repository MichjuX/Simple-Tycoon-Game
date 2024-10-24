package org.example.model;

public class Chef {
    private int level;
    private double baseIncome;
    private double upgradeCost;

    public Chef() {
        this.level = 1;
        this.baseIncome = 10; // Starting income
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
