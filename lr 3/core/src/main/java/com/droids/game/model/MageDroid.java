
package com.droids.game.model;

import java.util.Random;

public class MageDroid extends Droid {
    private static final Random RANDOM = new Random();
    private final double evasionChance = 0.35; // 35% шанс ухилення

    public MageDroid(String name) {
        super(name, 95, 20);
    }
    // Перевизначення методу отримання шкоди з логікою ухилення
    @Override
    public void receiveDamage(int incomingDamage) {
        boolean evaded = RANDOM.nextDouble() < evasionChance;

        if (evaded) {
            System.out.printf("%s УХИЛИВСЯ від шкоди!\n", this.getName());
            // Шкода не приймається
        } else {
            // Приймаємо повну шкоду
            super.receiveDamage(incomingDamage);
        }
    }
}
