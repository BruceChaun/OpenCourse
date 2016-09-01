import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Equation {
    Equation(double a[][], double b[]) {
        this.a = a;
        this.b = b;
    }

    double a[][];
    double b[];
}

class Position {
    Position(int column, int raw) {
        this.column = column;
        this.raw = raw;
    }

    int column;
    int raw;
}

class EnergyValues {
    static Equation ReadEquation() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        double a[][] = new double[size][size];
        double b[] = new double[size];
        for (int raw = 0; raw < size; ++raw) {
            for (int column = 0; column < size; ++column)
                a[raw][column] = scanner.nextInt();
            b[raw] = scanner.nextInt();
        }
        return new Equation(a, b);
    }

    /*
     * The following five functions are original, but I don't like them, 
     * so I write my own version.
     *
    static Position SelectPivotElement(double a[][], boolean used_raws[], boolean used_columns[]) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
        Position pivot_element = new Position(0, 0);
        while (used_raws[pivot_element.raw])
            ++pivot_element.raw;
        while (used_columns[pivot_element.column])
            ++pivot_element.column;
        return pivot_element;
    }

    static void SwapLines(double a[][], double b[], boolean used_raws[], Position pivot_element) {
        int size = a.length;

        for (int column = 0; column < size; ++column) {
            double tmpa = a[pivot_element.column][column];
            a[pivot_element.column][column] = a[pivot_element.raw][column];
            a[pivot_element.raw][column] = tmpa;
        }

        double tmpb = b[pivot_element.column];
        b[pivot_element.column] = b[pivot_element.raw];
        b[pivot_element.raw] = tmpb;

        boolean tmpu = used_raws[pivot_element.column];
        used_raws[pivot_element.column] = used_raws[pivot_element.raw];
        used_raws[pivot_element.raw] = tmpu;

        pivot_element.raw = pivot_element.column;
    }

    static void ProcessPivotElement(double a[][], double b[], Position pivot_element) {
        // Write your code here
    }

    static void MarkPivotElementUsed(Position pivot_element, boolean used_raws[], boolean used_columns[]) {
        used_raws[pivot_element.raw] = true;
        used_columns[pivot_element.column] = true;
    }

    static double[] SolveEquation(Equation equation) {
        double a[][] = equation.a;
        double b[] = equation.b;
        int size = a.length;

        boolean[] used_columns = new boolean[size];
        boolean[] used_raws = new boolean[size];
        for (int step = 0; step < size; ++step) {
            Position pivot_element = SelectPivotElement(a, used_raws, used_columns);
            SwapLines(a, b, used_raws, pivot_element);
            ProcessPivotElement(a, b, pivot_element);
            MarkPivotElementUsed(pivot_element, used_raws, used_columns);
        }

        return b;
    }*/

    static Position SelectPivotElement(double a[][], int i) {
        double val = 0.0;
        int pivot_row = i;
        int n = a.length;
        for (int j = i; j < n; j++) {
            double ele = Math.abs(a[j][i]);
            if (ele > val) {
                val = ele;
                pivot_row = j;
            }
        }
        Position pivot = new Position(i, pivot_row);
        return pivot;
    }

    static void SwapLines(double a[][], double b[], Position pivot_element) {
        int n = a.length;
        for (int col = 0; col < n; col++) {
            double tmp = a[pivot_element.column][col];
            a[pivot_element.column][col] = a[pivot_element.raw][col];
            a[pivot_element.raw][col] = tmp;
        }

        double tmp = b[pivot_element.column];
        b[pivot_element.column] = b[pivot_element.raw];
        b[pivot_element.raw] = tmp;
    }

    static void ProcessPivotElement(double a[][], double b[], int step) {
        if (a[step][step] == 0) {
            System.err.println("matrix is not full-rank, do not have unique solution.");
            return;
        }
        int n = a.length;
        b[step] /= a[step][step];
        for (int col = n - 1; col >= step; col--) {
            a[step][col] /= a[step][step];
        }

        for (int row = 0; row < n; row++) {
            if (row != step) {
                b[row] -= b[step] * a[row][step];
                for (int col = n - 1; col >= step; col--) {
                    a[row][col] -= a[row][step] * a[step][col];
                }
            }
        }

    }

    static double[] SolveEquation(Equation equation) {
        double a[][] = equation.a;
        double b[] = equation.b;
        int size = a.length;

        for (int i = 0; i < size; i++) {
            Position pivot_element = SelectPivotElement(a, i);
            SwapLines(a, b, pivot_element);
            ProcessPivotElement(a, b, i);
        }

        return b;
    }

    static void PrintColumn(double column[]) {
        int size = column.length;
        for (int raw = 0; raw < size; ++raw)
            System.out.printf("%.20f\n", column[raw]);
    }

    public static void main(String[] args) throws IOException {
        Equation equation = ReadEquation();
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}
