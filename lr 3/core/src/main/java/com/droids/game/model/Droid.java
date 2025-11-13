package com.droids.game.model;

public class Droid {
    private final String name; // Ім'я дроїда
    private int health; // Поточне здоров'я
    private final int maxHealth;
    private int damage; // Сила атаки

    // Конструктор
    public Droid(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
    }

    // Дроїд атакує ціль
    public void attack(Droid target) {
        if (this.isAlive()) {
            target.receiveDamage(this.damage);
        }
    }

    // Отримання шкоди
    public void receiveDamage(int incomingDamage) {
        if (incomingDamage > 0) {
            setHealth(getHealth() - incomingDamage);
        }
    }

    // Перевірка, чи живий дроїд
    public boolean isAlive() {
        return health > 0;
    }


    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    // Встановлює здоров'я, мінімум 0
    public void setHealth(int health) {
        this.health = Math.max(0, health);
    }

    // Повертає тип (назву класу)
    public String getType() {
        return this.getClass().getSimpleName();
    }

    // Перевизначення для зручного виведення
    @Override
    public String toString() {
        return String.format("%s (HP: %d, DMG: %d)", name, health, damage);
    }
}
