package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

import java.util.Scanner;

public class SortMenuCommand implements Command {
    private TaxiPark manager;
    public SortMenuCommand(TaxiPark manager) { this.manager = manager; }

    @Override
    public void execute() {
        System.out.println("\n--- ВАРІАНТИ СОРТУВАННЯ ---");
        System.out.println("1 - За зростанням (менше -> більше)");
        System.out.println("2 - За спаданням (більше -> менше)");
        System.out.println("0 - Назад");
        System.out.print("Ваш вибір: ");
        Scanner scanner = new Scanner(System.in);
        String subChoice = scanner.nextLine();
        switch (subChoice) {
            case "1": manager.sortByFuelConsumptionAsc(); break;
            case "2": manager.sortByFuelConsumptionDesc(); break;
            case "0": break;
            default: System.out.println("Невірний вибір.");
        }
    }
}
