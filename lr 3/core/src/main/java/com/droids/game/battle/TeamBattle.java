package com.droids.game.battle;

import com.droids.game.model.Droid;
import com.droids.game.model.RepairDroid;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Клас для симуляції бою команда на команду
public class TeamBattle {

    private final List<String> battleLog = new ArrayList<>(); // Список для логування
    private final Random random = new Random();
    private List<Droid> teamA;
    private List<Droid> teamB;
    private int round = 1;

    // КОНСТРУКТОРИ
    // Порожній конструктор
    public TeamBattle() {
    }

    // Конструктор для 2D-версії (Покроковий бій)
    public TeamBattle(List<Droid> teamA, List<Droid> teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
        battleLog.add("=== Бій Команда на Команду розпочато! ===");
        battleLog.add(String.format("Учасники: Команда A (%d) vs Команда Б (%d)", teamA.size(), teamB.size()));
    }

    // МЕТОДИ ДЛЯ 2D

    // Виконати один раунд бою
    public boolean takeTurn() {
        if (isFinished()) {
            logFinalResult();
            return false; // Бій завершено
        }

        battleLog.add("\n--- Раунд " + round + " ---");

        teamAttack(teamA, teamB, "Команда А"); // Команда А атакує

        if (!teamB.isEmpty()) {
            teamAttack(teamB, teamA, "Команда Б"); // Команда Б атакує
        }

        round++;
        return true; // Бій триває
    }

    // Перевірка, чи завершено бій
    public boolean isFinished() {
        return teamA.isEmpty() || teamB.isEmpty() || round > 100; // Або одна з команд знищена, або ліміт раундів
    }

    public List<String> getBattleLog() {
        return battleLog;
    }

    // МЕТОД ДЛЯ КОНСОЛІ (МИТТЄВИЙ РЕЖИМ)

    public List<String> startBattle(List<Droid> teamA, List<Droid> teamB) {
        List<String> instantLog = new ArrayList<>();

        List<Droid> aliveTeamA = new ArrayList<>(teamA);
        List<Droid> aliveTeamB = new ArrayList<>(teamB);
        int turn = 1;

        instantLog.add("=== Бій Команда на Команду розпочато (КОНСОЛЬ) ===");

        // Основний цикл миттєвого бою
        while (!aliveTeamA.isEmpty() && !aliveTeamB.isEmpty() && turn < 100) {
            instantLog.add("\n--- Хід " + turn + " ---");

            // Команди атакують по черзі
            performInstantAttack(aliveTeamA, aliveTeamB, instantLog, "Команда А");
            performInstantAttack(aliveTeamB, aliveTeamA, instantLog, "Команда Б");

            turn++;
        }

        // Лог фінального результату
        if (!aliveTeamA.isEmpty()) {
            instantLog.add("\n*** Переможець: Команда А ***");
        } else if (!aliveTeamB.isEmpty()) {
            instantLog.add("\n*** Переможець: Команда Б ***");
        } else {
            instantLog.add("\n*** Нічия ***");
        }

        return instantLog;
    }

    // Допоміжний метод для виконання миттєвої атаки
    private void performInstantAttack(List<Droid> attackers, List<Droid> defenders, List<String> log, String teamName) {
        for (Droid attacker : new ArrayList<>(attackers)) {
            if (!attacker.isAlive() || defenders.isEmpty()) continue;
            // Вибір випадкової цілі
            Droid defender = defenders.get(random.nextInt(defenders.size()));
            attacker.attack(defender);
            log.add(String.format("%s: %s атакує %s.", teamName, attacker.getName(), defender.getName()));
            if (!defender.isAlive()) {
                log.add(String.format("❌ %s знищено!", defender.getName()));
                defenders.remove(defender); // Видалення знищеного дроїда
            }
        }
        attackers.removeIf(droid -> !droid.isAlive()); // Видалення мертвих атакувальників
    }

    // ЛОГІКА ЛІКУВАННЯ (ДЛЯ 2D-версії)

    // Спроба лікування союзника RepairDroid'ом
    private boolean attemptHeal(Droid healer, List<Droid> team, String teamName) {
        if (!(healer instanceof RepairDroid) || team.isEmpty()) {
            return false;
        }

        if (random.nextDouble() > 0.70) { // 30% шанс на лікування
            return false;
        }

        RepairDroid repairDroid = (RepairDroid) healer;

        // Знаходимо всіх живих союзників
        List<Droid> potentialTargets = new ArrayList<>();
        for (Droid d : team) {
            if (d.isAlive()) {
                potentialTargets.add(d);
            }
        }

        if (potentialTargets.isEmpty()) return false;

        Droid target = potentialTargets.get(random.nextInt(potentialTargets.size())); // Випадкова ціль

        repairDroid.heal(target); // Виконання лікування

        battleLog.add(String.format("➕ %s: %s ЛІКУЄ %s (+%d HP). HP %s: %d",
            teamName, repairDroid.getName(), target.getName(),
            repairDroid.getHealingPower(), target.getName(), target.getHealth()));

        return true;
    }

    // Логіка атаки однієї команди по іншій (для 2D-версії)
    private void teamAttack(List<Droid> attackingTeam, List<Droid> defendingTeam, String teamName) {
        List<Droid> attackers = new ArrayList<>(attackingTeam);

        for (Droid attacker : attackers) {
            if (!attacker.isAlive() || defendingTeam.isEmpty()) continue;

            // --- ПЕРЕВІРКА ЛІКУВАННЯ ---
            if (attacker instanceof RepairDroid) {
                if (attemptHeal(attacker, attackingTeam, teamName)) {
                    continue; // Лікування відбулося
                }
            }
            // ---------------------------

            Droid defender = defendingTeam.get(random.nextInt(defendingTeam.size())); // Вибір цілі

            attacker.attack(defender);

            battleLog.add(String.format("%s: %s атакує %s.", teamName, attacker.getName(), defender.getName()));

            // Перевірка, чи ціль знищена
            if (!defender.isAlive()) {
                battleLog.add(String.format("❌ %s знищено!", defender.getName()));
                defendingTeam.remove(defender);
            }
        }
        attackingTeam.removeIf(droid -> !droid.isAlive()); // Очищення від мертвих
    }

    // Логування фінального результату
    private void logFinalResult() {
        if (teamB.isEmpty() && !teamA.isEmpty()) {
            battleLog.add("\n*** Переможець: Команда А ***");
        } else if (teamA.isEmpty() && !teamB.isEmpty()) {
            battleLog.add("\n*** Переможець: Команда Б ***");
        } else if (round > 100) {
            battleLog.add("\n*** Бій завершено за обмеженням раундів (100) ***");
        } else {
            battleLog.add("\n*** Нічия або бій зупинено ***");
        }
    }
}
