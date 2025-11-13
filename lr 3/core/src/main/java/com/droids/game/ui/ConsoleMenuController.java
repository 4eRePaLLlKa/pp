package com.droids.game.ui;

import com.droids.game.battle.OneVsOneBattle;
import com.droids.game.battle.TeamBattle;
import com.droids.game.io.BattleFileManager;
import com.droids.game.model.AssassinDroid;
import com.droids.game.model.Droid;
import com.droids.game.model.BattleDroid;
import com.droids.game.model.RepairDroid;
import com.droids.game.model.KnightDroid;
import com.droids.game.model.MageDroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Клас для керування консольним інтерфейсом гри
public class ConsoleMenuController {

    private final List<Droid> allDroids = new ArrayList<>(); // Список усіх створених дроїдів
    private final Scanner scanner = new Scanner(System.in);
    private List<String> lastBattleLog = new ArrayList<>(); // Зберігає лог останнього бою

    //ГОЛОВНИЙ ЦИКЛ МЕНЮ

    public void start() {
        // Додаємо стартових дроїдів
        allDroids.add(new BattleDroid("Iron-Bender-1"));
        allDroids.add(new RepairDroid("Heal-Bot-A"));

        int choice;
        do {
            printMenu();
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Очищення буфера
                handleChoice(choice);
            } else {
                System.out.println("❌ Некоректний ввід. Спробуйте ще раз.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 7);
    }

    private void printMenu() {
        System.out.println("\n--- 🤖 Меню Гри DroidBattle ---");
        System.out.println("1. Створити дроїда");
        System.out.println("2. Показати список створених дроїдів");
        System.out.println("3. Запустити бій 1 на 1");
        System.out.println("4. Запустити бій команда на команду");
        System.out.println("5. Записати проведений бій у файл");
        System.out.println("6. Відтворити проведений бій зі збереженого файлу");
        System.out.println("7. Вийти з програми");
        System.out.print("▶️ Виберіть опцію: ");
    }

    // Обробка вибору в меню
    private void handleChoice(int choice) {
        switch (choice) {
            case 1: createDroid(); break;
            case 2: showDroids(); break;
            case 3: startOneVsOneBattle(); break;
            case 4: startTeamBattle(); break;
            case 5: saveBattleLog(); break;
            case 6: replayBattle(); break;
            case 7: System.out.println("👋 До побачення!"); break;
            default: System.out.println("⚠️ Невідома команда.");
        }
    }

    // Функція 1: Створення дроїда
    private void createDroid() {
        System.out.println("\n--- Створення Дроїда ---");
        // [Опис типів]
        System.out.println("Виберіть тип дроїда:");
        System.out.println("1. BattleDroid  | HP: 150, DMG: 20 | Унікальна здібність: ARMOR = 15");
        System.out.println("2. RepairDroid  | HP: 80, DMG: 5  | Унікальна здібність: HEAL = 15");
        System.out.println("3. KnightDroid  | HP: 120, DMG: 25 | Унікальна здібність: ARMOR = 5");
        System.out.println("4. AssassinDroid| HP: 90, DMG: 40  | Унікальна здібність: CRIT = 50%");
        System.out.println("5. MageDroid    | HP: 95, DMG: 20  | Унікальна здібність: EVASION = 35%");
        System.out.print("Ввід: ");

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введіть ім'я дроїда: ");
        String name = scanner.nextLine();

        Droid newDroid = null;
        // Створення екземпляра відповідно до вибору
        if (typeChoice == 1) {
            newDroid = new BattleDroid(name);
        } else if (typeChoice == 2) {
            newDroid = new RepairDroid(name);
        } else if (typeChoice == 3) {
            newDroid = new KnightDroid(name);
        } else if (typeChoice == 4) {
            newDroid = new AssassinDroid(name);
        } else if (typeChoice == 5) {
            newDroid = new MageDroid(name);
        } else {
            System.out.println("Невідомий тип дроїда.");
            return;
        }

        allDroids.add(newDroid);
        System.out.println("✅ Створено: " + newDroid.toString());
    }

    // Функція 2: Показати список
    private void showDroids() {
        if (allDroids.isEmpty()) {
            System.out.println("🤷 Список дроїдів порожній.");
            return;
        }
        System.out.println("\n--- Список Дроїдів ---");
        for (int i = 0; i < allDroids.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, allDroids.get(i).toString());
        }
    }

    // Функція 3: Бій 1 на 1
    private void startOneVsOneBattle() {
        if (allDroids.size() < 2) {
            System.out.println("⚠️ Потрібно мінімум два дроїда для бою.");
            return;
        }
        showDroids();
        System.out.print("Виберіть номер першого дроїда: ");
        int index1 = scanner.nextInt() - 1;
        System.out.print("Виберіть номер другого дроїда: ");
        int index2 = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index1 >= 0 && index1 < allDroids.size() && index2 >= 0 && index2 < allDroids.size() && index1 != index2) {
            // Створюємо копії дроїдів для бою, щоб не змінювати оригінали
            Droid original1 = allDroids.get(index1);
            Droid original2 = allDroids.get(index2);
            Droid fighter1 = createBattleCopy(original1, " (Боєць А)");
            Droid fighter2 = createBattleCopy(original2, " (Боєць Б)");

            OneVsOneBattle battle = new OneVsOneBattle();
            lastBattleLog = battle.startBattle(fighter1, fighter2); // Запуск і збереження логу

            // Вивід логу на консоль
            lastBattleLog.forEach(System.out::println);
        } else {
            System.out.println("Некоректний вибір.");
        }
    }

    private Droid createBattleCopy(Droid original, String suffix) {
        String newName = original.getName() + suffix;

        // Використовуємо instanceof для створення правильного підтипу
        if (original instanceof BattleDroid) {
            return new BattleDroid(newName);
        } else if (original instanceof RepairDroid) {
            return new RepairDroid(newName);
        } else if (original instanceof KnightDroid) {
            return new KnightDroid(newName);
        } else if (original instanceof AssassinDroid) {
            return new AssassinDroid(newName);
        } else if (original instanceof MageDroid) {
            return new MageDroid(newName);
        } else {
            // Fallback для базового Droid
            return new Droid(newName, original.getMaxHealth(), original.getDamage());
        }
    }

    private List<Droid> selectTeam(String teamName) {
        List<Droid> teamDroids = new ArrayList<>();
        System.out.printf("\n--- Формування %s ---\n", teamName);

        while (true) {
            showDroids(); // Показ списку
            System.out.printf("▶️ Виберіть номер дроїда для додавання до %s (або 0, щоб завершити вибір): ", teamName);

            if (scanner.hasNextInt()) {
                int selection = scanner.nextInt();
                scanner.nextLine();

                if (selection == 0) {
                    if (teamDroids.isEmpty()) {
                        System.out.println("⚠️ Команда не може бути порожньою. Будь ласка, оберіть хоча б одного дроїда.");
                        continue;
                    }
                    break;
                }

                int index = selection - 1;
                if (index >= 0 && index < allDroids.size()) {
                    Droid originalDroid = allDroids.get(index);
                    Droid fighterCopy = createBattleCopy(originalDroid, " (" + teamName + ")"); // Створення копії
                    teamDroids.add(fighterCopy);
                    System.out.printf("✅ Додано до %s: %s\n", teamName, fighterCopy.getName());
                } else {
                    System.out.println("Некоректний номер дроїда. Спробуйте ще раз.");
                }
            } else {
                System.out.println("Некоректний ввід. Будь ласка, введіть число.");
                scanner.nextLine();
            }
        }
        return teamDroids;
    }

    // Функція 4: Бій Команда на Команду
    private void startTeamBattle() {
        if (allDroids.size() < 2) {
            System.out.println("⚠️ Потрібно створити мінімум два дроїда для командного бою.");
            return;
        }

        List<Droid> teamA = selectTeam("Команда А"); // Вибір Команди А
        List<Droid> teamB = selectTeam("Команда Б"); // Вибір Команди Б

        if (teamA.isEmpty() || teamB.isEmpty()) {
            System.out.println("⚠️ Бій не може розпочатися, якщо одна з команд порожня.");
            return;
        }

        // Запуск командного бою
        TeamBattle battle = new TeamBattle();
        lastBattleLog = battle.startBattle(teamA, teamB);

        lastBattleLog.forEach(System.out::println);
    }

    // Функція 5: Збереження логу
    private void saveBattleLog() {
        if (lastBattleLog.isEmpty()) {
            System.out.println("⚠️ Спочатку проведіть бій, щоб його записати.");
            return;
        }
        System.out.print("Введіть назву файлу для збереження (наприклад, 'battle.txt'): ");
        String filename = scanner.nextLine();

        BattleFileManager.saveLogToFile(lastBattleLog, filename); // Виклик функції збереження
    }

    // Функція 6: Відтворення логу
    private void replayBattle() {
        System.out.print("Введіть назву файлу для відтворення: ");
        String filename = scanner.nextLine();

        BattleFileManager.replayBattle(filename); // Виклик функції відтворення
    }
}
