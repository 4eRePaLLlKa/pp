package com.droids.game.model;

public class RepairDroid extends Droid {
    private int healingPower;

    public RepairDroid(String name) {
        super(name, 80, 5);
        this.healingPower = 20; // Сила лікування
    }

    // Нова здібність: лікування
    public void heal(Droid target) {
        if (this.isAlive()) {
            target.setHealth(target.getHealth() + healingPower);
        }
    }

    public int getHealingPower() {
        return healingPower;
    }
}
