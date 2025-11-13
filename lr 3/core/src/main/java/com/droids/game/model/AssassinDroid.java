
package com.droids.game.model;

import java.util.Random;

public class AssassinDroid extends Droid {
    private static final Random RANDOM = new Random();
    private final double criticalChance = 0.5; // 50% шанс криту
    private final int criticalMultiplier = 2; // Шкода x2

    public AssassinDroid(String name) {
        super(name, 90, 40);
    }
    // Перевизначення методу атаки з логікою критичного удару
    @Override
    public void attack(Droid target) {
        if (this.isAlive()) {
            int damage = this.getDamage();
            boolean isCritical = RANDOM.nextDouble() < criticalChance; // Випадкове число від 0 до 1

            if (isCritical) {
                damage *= criticalMultiplier;
                System.out.printf("%s ЗАВДАЄ КРИТИЧНИЙ УДАР! Шкода: %d\n", this.getName(), damage);
            }
            target.receiveDamage(damage);
        }
    }
}
