package com.droids.game.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BattleFileManager {

    // Зберігає лог бою у файл
    public static void saveLogToFile(List<String> log, String filename) {
        String fullPath = filename;

        try (FileWriter writer = new FileWriter(fullPath)) {
            for (String line : log) {
                writer.write(line + System.lineSeparator()); // Запис кожного рядка
            }
            System.out.println("Лог бою успішно записано у файл: " + filename);
        } catch (IOException e) {
            System.err.println("Помилка при записі файлу: " + e.getMessage());
        }
    }

    // Зчитує та виводить лог бою з файлу
    public static void replayBattle(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            System.out.println("\n--- ВІДТВОРЕННЯ БОЮ З ФАЙЛУ: " + filename + " ---");
            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println("--- КІНЕЦЬ ВІДТВОРЕННЯ ---");
        } catch (IOException e) {
            System.err.println("Помилка при читанні файлу або файл не знайдено: " + e.getMessage());
        }
    }
}
