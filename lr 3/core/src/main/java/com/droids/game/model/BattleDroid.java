
package com.droids.game.model;

public class BattleDroid extends Droid {
    private final int armor = 15; // Броня: зменшує вхідну шкоду на 15

    public BattleDroid(String name) {
        // HP 150, DMG 20
        super(name, 150, 20);
    }
    // Перевизначення методу отримання шкоди
    @Override
    public void receiveDamage(int incomingDamage) {
        int damageAfterArmor = Math.max(0, incomingDamage - armor);
        super.receiveDamage(damageAfterArmor);
    }
}
