package org.example.model;

public class Worker {
    private int level;
    private double baseIncome;
    private double upgradeCost;
    private String name;
    private int id;

    public Worker(double baseIncome, double upgradeCost, String name, int id) {
        this.level = 1;
        this.baseIncome = baseIncome; // Starting income

        this.upgradeCost = upgradeCost; // Cost to upgrade
        this.name = name;
        this.id = id;
    }
    public Worker(double baseIncome, double upgradeCost, String name, int id, int level) {
        this.level = 1;
        this.baseIncome = baseIncome; // Starting income

        this.upgradeCost = upgradeCost; // Cost to upgrade
        this.name = name;
        this.id = id;
        this.level = level;
    }

    public double getIncome() {
        return baseIncome * level;
    }

    public double getUpgradeCost() {
        return upgradeCost * level; // Cost increases with each upgrade
    }
    public String getName(){
        return name;
    }

    public void upgrade() {
        level++;
    }

    public int getLevel() {
        return level;
    }
    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return String.format("(Lvl: %d, Profit: %.2f, Upgrade: %.2f)",
                level, getIncome(), getUpgradeCost());
    }
}
