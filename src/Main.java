import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ComplexMatrix matrixA = null;
        ComplexMatrix matrixB = null;
        boolean running = true;

        System.out.println("Матрицы комплексных чисел");

        while (running) {
            printMenu();

            try {
                int choice = readInteger(scanner, "Выберите операцию: ", 0, 8);

                switch (choice) {
                    case 0:
                        running = false;
                        break;

                    case 1:
                        matrixA = readMatrix(scanner, "A");
                        break;

                    case 2:
                        matrixB = readMatrix(scanner, "B");
                        break;

                    case 3:
                        printMatrix("A", matrixA);
                        printMatrix("B", matrixB);
                        break;

                    case 4:
                        requireBoth(matrixA, matrixB);
                        printMatrix("A + B", matrixA.add(matrixB));
                        break;

                    case 5:
                        requireBoth(matrixA, matrixB);
                        printMatrix("A * B", matrixA.multiply(matrixB));
                        break;

                    case 6:
                        requireBoth(matrixA, matrixB);
                        printMatrix("A / B", matrixA.divide(matrixB));
                        break;

                    case 7:
                        ComplexMatrix forTranspose =
                                selectMatrix(scanner, matrixA, matrixB);
                        printMatrix(
                                "Транспонированная матрица",
                                forTranspose.transpose()
                        );
                        break;

                    case 8:
                        ComplexMatrix forDeterminant =
                                selectMatrix(scanner, matrixA, matrixB);
                        System.out.println(
                                "Определитель: " + forDeterminant.determinant()
                        );
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println("Ошибка вычисления: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Работа завершена.");
    }

    private static void printMenu() {
        System.out.println("\n1. Создать или заменить матрицу A");
        System.out.println("2. Создать или заменить матрицу B");
        System.out.println("3. Показать матрицы");
        System.out.println("4. Сложить A + B");
        System.out.println("5. Умножить A * B");
        System.out.println("6. Разделить A / B");
        System.out.println("7. Транспонировать A или B");
        System.out.println("8. Вычислить определитель A или B");
        System.out.println("0. Выход");
    }

    private static int readInteger(
            Scanner scanner,
            String prompt,
            int minimum,
            int maximum
    ) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next();

            try {
                int value = Integer.parseInt(input);

                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат целого числа.");
            }

            System.out.println(
                    "Введите целое число от " + minimum + " до " + maximum + "."
            );
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next();
            input = input.replace(',', '.');

            try {
                double value = Double.parseDouble(input);

                if (value >= -Double.MAX_VALUE && value <= Double.MAX_VALUE) {
                    return value;
                }

                System.out.println("Число должно быть конечным.");
            } catch (NumberFormatException e) {
                System.out.println("Введите число, например 2, -3 или 1.5.");
            }
        }
    }

    private static ComplexMatrix readMatrix(Scanner scanner, String name) {
        System.out.println("Создание матрицы " + name);

        int rows = readInteger(scanner, "Число строк (1..10): ", 1, 10);
        int columns = readInteger(scanner, "Число столбцов (1..10): ", 1, 10);

        Complex[][] values = new Complex[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.println(
                        "Элемент " + name + "[" + (i + 1) + "][" + (j + 1) + "]"
                );

                double real = readDouble(scanner, "Действительная часть: ");
                double imaginary = readDouble(scanner, "Мнимая часть: ");

                values[i][j] = new Complex(real, imaginary);
            }
        }

        ComplexMatrix matrix = new ComplexMatrix(values);
        printMatrix(name, matrix);

        return matrix;
    }

    private static void printMatrix(String name, ComplexMatrix matrix) {
        System.out.println(name + ":");

        if (matrix == null) {
            System.out.println("Матрица не создана.");
        } else {
            System.out.println(matrix);
        }
    }

    private static void requireBoth(
            ComplexMatrix matrixA,
            ComplexMatrix matrixB
    ) {
        if (matrixA == null || matrixB == null) {
            throw new IllegalArgumentException(
                    "Сначала создайте обе матрицы A и B."
            );
        }
    }

    private static ComplexMatrix selectMatrix(
            Scanner scanner,
            ComplexMatrix matrixA,
            ComplexMatrix matrixB
    ) {
        int choice = readInteger(
                scanner,
                "Выберите матрицу: 1 — A, 2 — B: ",
                1,
                2
        );

        ComplexMatrix selected;

        if (choice == 1) {
            selected = matrixA;
        } else {
            selected = matrixB;
        }

        if (selected == null) {
            throw new IllegalArgumentException(
                    "Сначала создайте выбранную матрицу."
            );
        }

        return selected;
    }
}