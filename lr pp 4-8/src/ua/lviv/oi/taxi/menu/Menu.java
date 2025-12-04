package ua.lviv.oi.taxi.menu;

import java.util.LinkedHashMap;
import java.util.Map;

public class Menu {
    private Map<String, MenuItem> menuItems = new LinkedHashMap<>();

    private static class MenuItem {
        String description;
        Command command;

        MenuItem(String description, Command command) {
            this.description = description;
            this.command = command;
        }
    }

    public void registerCommand(String key, String description, Command command) {
        menuItems.put(key, new MenuItem(description, command));
    }

    public void display() {
        System.out.println("\n=== МЕНЮ ТАКСОПАРКУ ===");
        for (Map.Entry<String, MenuItem> entry : menuItems.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue().description);
        }
        System.out.print("Оберіть дію: ");
    }

    public void executeCommand(String key) {
        if (menuItems.containsKey(key)) {
            menuItems.get(key).command.execute();
        } else {
            System.out.println("Невідома команда. Спробуйте ще раз.");
        }
    }
}