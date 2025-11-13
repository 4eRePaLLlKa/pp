package com.droids.game.battle;

import com.droids.game.model.Droid;
import java.util.ArrayList;
import java.util.List;

public class OneVsOneBattle {
    // Список для зберігання логу бою
    private List<String> battleLog = new ArrayList<>();

    // Запускає бій між двома дроїдами
    public List<String> startBattle(Droid droid1, Droid droid2) {
        battleLog.clear();
        battleLog.add("=== Бій 1 на 1 розпочато! ===");
        battleLog.add(String.format("Учасники: %s vs %s", droid1.getName(), droid2.getName()));

        int turn = 1;
        // Цикл триває, доки обидва дроїди живі
        while (droid1.isAlive() && droid2.isAlive()) {

            // Логування стану перед ходом
            battleLog.add(String.format("\n--- Хід %d ---", turn));
            battleLog.add(String.format("Статус: %s | %s", droid1.toString(), droid2.toString()));

            Droid attacker, defender;

            // Визначення черговості атаки
            if (turn % 2 != 0) { // Непарний хід: droid1 атакує
                attacker = droid1;
                defender = droid2;
            } else { // Парний хід: droid2 атакує
                attacker = droid2;
                defender = droid1;
            }

            // Виконання атаки
            if (attacker.isAlive()) {
                attacker.attack(defender);
                // Примітка: тут логується базова шкода. Фактична шкода (критична/броня) логується у класах Droid.
                battleLog.add(String.format("%s атакує %s і завдає %d шкоди.",
                    attacker.getName(), defender.getName(), attacker.getDamage()));
            }

            turn++;
        }

        // Визначення переможця та логування результату
        Droid winner = droid1.isAlive() ? droid1 : droid2;
        battleLog.add("\n*** Переможець: " + winner.getName() + " ***");

        return battleLog; // Повернення логу
    }
}
