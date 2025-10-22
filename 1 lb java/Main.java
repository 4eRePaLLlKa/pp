import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int N;

      
        if(args.length > 0){
            N = Integer.parseInt(args[0]);
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.print("Введіть кількість чисел Фібоначчі: ");
            N = sc.nextInt();
            sc.close();
        }


        FibonacciNumber[] fibNumbers = new FibonacciNumber[N];
        for(int i = 0; i < N; i++){
            fibNumbers[i] = new FibonacciNumber(i + 1);
        }

        System.out.println("Перші " + N + " чисел Фібоначчі та перевірка на форму w^3 + 1:");
        for(FibonacciNumber fn : fibNumbers){
            System.out.println("F(" + fn.getIndex() + ") = " + fn.getValue() +
                    (fn.isCubePlusOne() ? " (має вигляд w^3 + 1)" : ""));
        }
    }
}

