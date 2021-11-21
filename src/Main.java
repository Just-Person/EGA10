import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество шагов: ");
        int steps = 0;
        while (steps < 1)
        steps = in.nextInt();
        System.out.println("Введите размер популяции: ");
        int populationsize = 0;
        while (populationsize < 5)
            populationsize = in.nextInt();
        System.out.println("Введите способ задания начания начальной популяции: ");
        System.out.println("1 - Случайно");
        System.out.println("2 - Жадный алгоритм");
        int form = 0;
        while (form != 1 && form != 2)
            form = in.nextInt();
        System.out.println("Введите способ кроссовера: ");
        System.out.println("1 - Многоточечный");
        System.out.println("2 - Однородный");
        int cross = 0;
        while (cross != 1 && cross != 2)
            cross = in.nextInt();
        System.out.println("Введите способ мутации: ");
        System.out.println("1 - Точечный");
        System.out.println("2 - Дополнение");
        int mutate = 0;
        while (mutate != 1 && mutate != 2)
            mutate = in.nextInt();
        System.out.println("Введите способ селекции: ");
        System.out.println("1 - Пропорциональная");
        System.out.println("2 - Линейная");
        int select = 0;
        while (select != 1 && select != 2)
            select = in.nextInt();
        Evolution evolution = new Evolution(steps,populationsize,form,cross, mutate,select);
        evolution.evolutionfunction();
    }
}
