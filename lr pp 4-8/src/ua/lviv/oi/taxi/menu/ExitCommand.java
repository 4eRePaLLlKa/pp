package ua.lviv.oi.taxi.menu;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Завершення роботи. До побачення!");
        System.exit(0);
    }
}
