package ua.lviv.oi.taxi.main;

import ua.lviv.oi.taxi.park.TaxiPark;
import ua.lviv.oi.taxi.menu.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaxiPark taxiManager = new TaxiPark();
        Menu menu = new Menu();

        // Основні функції
        menu.registerCommand("1", "Додати автомобіль", new AddCarCommand(taxiManager));
        menu.registerCommand("2", "Видалити автомобіль", new RemoveCarCommand(taxiManager));
        menu.registerCommand("3", "Показати весь список авто", new ShowAllCarsCommand(taxiManager));
        menu.registerCommand("4", "Підрахувати загальну вартість", new CalculateCostCommand(taxiManager));
        menu.registerCommand("5", "Сортування", new SortMenuCommand(taxiManager));
        menu.registerCommand("6", "Пошук авто за швидкістю", new SearchCommand(taxiManager));

        // Робота з файлами
        menu.registerCommand("7", "Зберегти у файл", new SaveCommand(taxiManager));
        menu.registerCommand("8", "Завантажити з файлу", new LoadCommand(taxiManager));

        menu.registerCommand("0", "Вихід", new ExitCommand());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            menu.display();
            String choice = scanner.nextLine();
            menu.executeCommand(choice);
        }
    }
}