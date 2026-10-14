public class ComplexMatrix {
    private int rows;
    private int columns;
    private Complex[][] data;

    public ComplexMatrix(Complex[][] values) {
        if (values == null || values.length == 0
                || values[0] == null || values[0].length == 0) {
            throw new IllegalArgumentException("Матрица не должна быть пустой.");
        }
        rows = values.length;
        columns = values[0].length;
        data = new Complex[rows][columns];
        for (int i = 0; i < rows; i++) {
            if (values[i] == null || values[i].length != columns) {
                throw new IllegalArgumentException("Матрица должна быть прямоугольной.");
            }
            for (int j = 0; j < columns; j++) {
                if (values[i][j] == null) {
                    throw new IllegalArgumentException("Элемент матрицы не может быть null.");
                }
                data[i][j] = values[i][j];
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Complex get(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Неверный индекс элемента матрицы.");
        }
        return data[row][column];
    }

    public ComplexMatrix add(ComplexMatrix other) {
        requireOther(other);
        if (rows != other.rows || columns != other.columns) {
            throw new IllegalArgumentException("Для сложения размеры матриц должны совпадать.");
        }
        Complex[][] result = new Complex[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = data[i][j].add(other.data[i][j]);
            }
        }
        return new ComplexMatrix(result);
    }

    public ComplexMatrix multiply(ComplexMatrix other) {
        requireOther(other);
        if (columns != other.rows) {
            throw new IllegalArgumentException(
                    "Число столбцов A должно равняться числу строк B.");
        }
        Complex[][] result = new Complex[rows][other.columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.columns; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < columns; k++) {
                    Complex product = data[i][k].multiply(other.data[k][j]);
                    sum = sum.add(product);
                }
                result[i][j] = sum;
            }
        }
        return new ComplexMatrix(result);
    }

    public ComplexMatrix divide(ComplexMatrix other) {
        requireOther(other);
        other.requireSquare();
        if (columns != other.rows) {
            throw new IllegalArgumentException(
                    "Число столбцов A должно равняться размеру квадратной B.");
        }
        ComplexMatrix inverseMatrix = other.inverse();
        return multiply(inverseMatrix);
    }

    public ComplexMatrix transpose() {
        Complex[][] result = new Complex[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[j][i] = data[i][j];
            }
        }
        return new ComplexMatrix(result);
    }

    public Complex determinant() {
        requireSquare();
        Complex[][] work = copyData();
        Complex result = new Complex(1, 0);
        int sign = 1;
        for (int k = 0; k < rows; k++) {
            int pivotRow = findPivotRow(work, k);
            if (pivotRow == rows) {
                return new Complex(0, 0);
            }
            if (pivotRow != k) {
                swapRows(work, k, pivotRow);
                sign = -sign;
            }
            Complex pivot = work[k][k];
            result = result.multiply(pivot);
            for (int i = k + 1; i < rows; i++) {
                Complex factor = work[i][k].divide(pivot);
                work[i][k] = new Complex(0, 0);
                for (int j = k + 1; j < columns; j++) {
                    Complex product = factor.multiply(work[k][j]);
                    work[i][j] = work[i][j].subtract(product);
                }
            }
        }
        return result.multiply(new Complex(sign, 0));
    }

    public ComplexMatrix inverse() {
        requireSquare();
        int n = rows;
        Complex[][] extended = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                extended[i][j] = data[i][j];
                if (i == j) {
                    extended[i][j + n] = new Complex(1, 0);
                } else {
                    extended[i][j + n] = new Complex(0, 0);
                }
            }
        }
        for (int k = 0; k < n; k++) {
            int pivotRow = findPivotRow(extended, k);
            if (pivotRow == n) {
                throw new ArithmeticException("Обратная матрица не существует.");
            }
            if (pivotRow != k) {
                swapRows(extended, k, pivotRow);
            }
            Complex pivot = extended[k][k];
            for (int j = 0; j < 2 * n; j++) {
                extended[k][j] = extended[k][j].divide(pivot);
            }
            extended[k][k] = new Complex(1, 0);
            for (int i = 0; i < n; i++) {
                if (i != k) {
                    Complex factor = extended[i][k];
                    for (int j = 0; j < 2 * n; j++) {
                        if (j != k) {
                            Complex product = factor.multiply(extended[k][j]);
                            extended[i][j] = extended[i][j].subtract(product);
                        }
                    }
                    extended[i][k] = new Complex(0, 0);
                }
            }
        }
        Complex[][] result = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = extended[i][j + n];
            }
        }
        return new ComplexMatrix(result);
    }

    private Complex[][] copyData() {
        Complex[][] copy = new Complex[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                copy[i][j] = data[i][j];
            }
        }
        return copy;
    }

    private int findPivotRow(Complex[][] matrix, int column) {
        int row = column;
        while (row < matrix.length && matrix[row][column].isZero()) {
            row++;
        }
        return row;
    }

    private void swapRows(Complex[][] matrix, int first, int second) {
        Complex[] temporary = matrix[first];
        matrix[first] = matrix[second];
        matrix[second] = temporary;
    }

    private void requireSquare() {
        if (rows != columns) {
            throw new IllegalArgumentException("Для этой операции нужна квадратная матрица.");
        }
    }

    private void requireOther(ComplexMatrix other) {
        if (other == null) {
            throw new IllegalArgumentException("Вторая матрица не задана.");
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            result.append("[ ");
            for (int j = 0; j < columns; j++) {
                result.append(data[i][j]);
                if (j < columns - 1) {
                    result.append(" | ");
                }
            }
            result.append(" ]");
            if (i < rows - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }
}
