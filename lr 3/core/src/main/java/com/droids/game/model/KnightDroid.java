
package com.droids.game.model;

public class KnightDroid extends Droid {
    private final int armor = 5; // Броня: зменшує вхідну шкоду

    public KnightDroid(String name) {
        super(name, 120, 25);
    }
    // Перевизначення методу отримання шкоди з урахуванням броні
    @Override
    public void receiveDamage(int incomingDamage) {
        int damageAfterArmor = Math.max(0, incomingDamage - armor);
        super.receiveDamage(damageAfterArmor);
    }
}
