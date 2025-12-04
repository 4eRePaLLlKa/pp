package ua.lviv.oi.taxi.park;

import ua.lviv.oi.taxi.model.*;
import java.util.*;
import java.io.*;

public class TaxiPark {
    // Список для зберігання автомобілів
    private List<Car> cars = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    // 1. Додавання автомобіля
    public void addCar() {
        System.out.println("\n--- Додавання автомобіля ---");
        System.out.println("Виберіть тип авто:");
        System.out.println("1 - Economy (Економ)");
        System.out.println("2 - Comfort (Комфорт)");
        System.out.println("3 - Premium (Бізнес)");
        System.out.println("4 - Van (Фургон)");
        System.out.print("Ваш вибір: ");

        int type = Integer.parseInt(scanner.nextLine());

        System.out.print("Введіть ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Введіть Модель: ");
        String model = scanner.nextLine();
        System.out.print("Введіть Ціну ($): ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Введіть Витрату пального (л/100км): ");
        double fuel = Double.parseDouble(scanner.nextLine());
        System.out.print("Введіть Макс. швидкість (км/год): ");
        int speed = Integer.parseInt(scanner.nextLine());

        Car newCar = null;
        switch (type) {
            case 1:
                System.out.print("Чи дозволені тварини? (true/false): ");
                boolean pets = Boolean.parseBoolean(scanner.nextLine());
                newCar = new EconomyCar(id, model, price, fuel, speed, pets);
                break;
            case 2:
                System.out.print("Чи є дитяче крісло? (true/false): ");
                boolean childSeat = Boolean.parseBoolean(scanner.nextLine());
                newCar = new ComfortCar(id, model, price, fuel, speed, childSeat);
                break;
            case 3:
                System.out.print("Чи є міні-бар? (true/false): ");
                boolean bar = Boolean.parseBoolean(scanner.nextLine());
                newCar = new PremiumCar(id, model, price, fuel, speed, bar);
                break;
            case 4:
                System.out.print("Кількість місць: ");
                int seats = Integer.parseInt(scanner.nextLine());
                newCar = new VanCar(id, model, price, fuel, speed, seats);
                break;
            default:
                System.out.println("Невірний тип авто!");
                return;
        }

        if (newCar != null) {
            cars.add(newCar);
            System.out.println("Автомобіль успішно додано!");
        }
    }

    // 2. Видалення автомобіля
    public void removeCar() {
        System.out.print("Введіть ID авто для видалення: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean removed = cars.removeIf(car -> car.getId() == id);
        if (removed) {
            System.out.println("Автомобіль з ID " + id + " видалено.");
        } else {
            System.out.println("Автомобіль з таким ID не знайдено.");
        }
    }

    // 3. Показати всі авто
    public void showAllCars() {
        System.out.println("\n--- Список автомобілів ---");
        if (cars.isEmpty()) {
            System.out.println("Список порожній.");
        } else {
            for (Car car : cars) {
                System.out.println(car);
            }
        }
    }

    // 4. Підрахунок вартості
    public void calculateTotalCost() {
        double total = 0;
        for (Car car : cars) {
            total += car.getPrice();
        }
        System.out.println("Загальна вартість автопарку: " + total + " $");
    }

    // 5. Сортування (від меншого до більшого)
    public void sortByFuelConsumptionAsc() {
        cars.sort(Comparator.comparingDouble(Car::getFuelConsumption));
        System.out.println("Відсортовано за зростанням витрати пального.");
        showAllCars();
    }

    // 6. Сортування (від більшого до меншого)
    public void sortByFuelConsumptionDesc() {
        cars.sort(Comparator.comparingDouble(Car::getFuelConsumption).reversed());
        System.out.println("Відсортовано за спаданням витрати пального.");
        showAllCars();
    }

    // 7. Пошук за швидкістю
    public void searchBySpeedRange() {
        System.out.print("Введіть мінімальну швидкість: ");
        int min = Integer.parseInt(scanner.nextLine());
        System.out.print("Введіть максимальну швидкість: ");
        int max = Integer.parseInt(scanner.nextLine());

        System.out.println("--- Результати пошуку ---");
        boolean found = false;
        for (Car car : cars) {
            if (car.getMaxSpeed() >= min && car.getMaxSpeed() <= max) {
                System.out.println(car);
                found = true;
            }
        }
        if (!found) System.out.println("Автомобілів у такому діапазоні не знайдено.");
    }

    // 8. ЗБЕРЕЖЕННЯ У ФАЙЛ
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter("taxi_data.csv")) {
            for (Car car : cars) {
                writer.println(car.toCSV());
            }
            System.out.println("Дані успішно збережено у файл 'taxi_data.csv'.");
        } catch (FileNotFoundException e) {
            System.out.println("Помилка при збереженні файлу: " + e.getMessage());
        }
    }

    // 9. ЗАВАНТАЖЕННЯ З ФАЙЛУ
    public void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new File("taxi_data.csv"))) {
            cars.clear();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                // CSV: Type,ID,Model,Price,Fuel,Speed,UniqueParam
                if (parts.length < 7) continue;

                String type = parts[0];
                int id = Integer.parseInt(parts[1]);
                String model = parts[2];
                double price = Double.parseDouble(parts[3]);
                double fuel = Double.parseDouble(parts[4]);
                int speed = Integer.parseInt(parts[5]);

                Car car = null;
                switch (type) {
                    case "Economy":
                        car = new EconomyCar(id, model, price, fuel, speed, Boolean.parseBoolean(parts[6]));
                        break;
                    case "Comfort":
                        car = new ComfortCar(id, model, price, fuel, speed, Boolean.parseBoolean(parts[6]));
                        break;
                    case "Premium":
                        car = new PremiumCar(id, model, price, fuel, speed, Boolean.parseBoolean(parts[6]));
                        break;
                    case "Van":
                        car = new VanCar(id, model, price, fuel, speed, Integer.parseInt(parts[6]));
                        break;
                }
                if (car != null) cars.add(car);
            }
            System.out.println("Дані успішно завантажено! Кількість авто: " + cars.size());
        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено (можливо, це перший запуск).");
        } catch (Exception e) {
            System.out.println("Помилка при читанні файлу: " + e.getMessage());
        }
    }
}