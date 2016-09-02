import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

class Position {
    Position(int column, int raw) {
        this.column = column;
        this.raw = raw;
    }

    int column;
    int raw;
}

class Mat {
    public static double[][] transpose(double[][] a) {
        int n = a.length;
        double[][] aT = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                aT[i][j] = a[j][i];
        return aT;
    }

    public static boolean Gaussian(double[][] a, double[] b, double[] ans) {
        int size = a.length;
        double[][] aa = new double[size][size];
        double[] bb = new double[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                aa[i][j] = a[i][j];
            }
            bb[i] = b[i];
        }

        for (int i = 0; i < size; i++) {
            Position pivot_element = SelectPivotElement(aa, i);
            SwapLines(aa, bb, pivot_element);
            if (!ProcessPivotElement(aa, bb, i)) return false;
        }

        for (int i = 0; i < size; i++)
            ans[i] = bb[i];
        return true;
    }

    private static Position SelectPivotElement(double a[][], int i) {
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

    private static void SwapLines(double a[][], double b[], Position pivot_element) {
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

    private static boolean ProcessPivotElement(double a[][], double b[], int step) {
        if (a[step][step] == 0) {
            return false;
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

        return true;
    }
}

public class Diet {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    int solveDietProblem(int n, int m, double A[][], double[] b, double[] c, double[] x) {
        Arrays.fill(x, 1);
        // Write your code here

        List<Integer> list = new ArrayList<Integer>();
        List<int[]> cand = new ArrayList<int[]>();
        combination(n, m, 0, list, cand);

        boolean found = false;
        int[] base = new int[m];
        for (int i = 0; i < cand.size(); i++) {
            base = cand.get(i);
            double[][] AB = new double[m][m];
            double[] bB = new double[m];
            for (int j = 0; j < m; j++) {
                AB[j] = A[base[j]];
                bB[j] = b[base[j]];
            }
            if (Mat.Gaussian(AB, bB, x)) {
                boolean satisfied = true;
                for (int j = 0; j < n; j++) {
                    double res = 0.0;
                    for (int k = 0; k < m; k++) {
                        res += A[j][k] * x[k];
                    }

                    if (res > b[j]) {
                        satisfied = false;
                        break;
                    }
                }
                if (satisfied) {
                    found = true;
                    break;
                }
            }
        }

        if (!found)
            return -1;

        return Simplex(A, b, base, c, x);
    }

    void combination(int n, int m, int index, List<Integer> list, List<int[]> candidates) {
        int size = list.size();
        if (size == m) {
            int[] array = new int[m];
            for (int i = 0; i < m; i++)
                array[i] = list.get(i).intValue();
            candidates.add(array);
            return;
        }

        if (index + m - size <= n) {
            combination(n, m, index+1, list, candidates);
            list.add(index);
            combination(n, m, index+1, list, candidates);
            list.remove(size);
        }
    }

    /*
     * given a feasible basis, Simplex() searches adjacent vertex 
     * till finds the optimum.
     */
    int Simplex(double[][] a, double[] b, int[] base, double[] c, double[] x) {
        double value = 0.0;
        int dim = base.length;

        while (true) {
            // subset of matrix a and vector b
            double[][] AB = new double[dim][dim];
            double[] bB = new double[dim];
            for (int i = 0; i < dim; i++) {
                AB[i] = a[base[i]];
                bB[i] = b[base[i]];
            }

            // solve x s.t. AB * x = bB
            // double[] x = new double[dim];
            Mat.Gaussian(AB, bB, x);

            // update value = c * x
            double new_value = 0.0;
            for (int i = 0; i < dim; i++)
                new_value += c[i] * x[i];

            // calculate lambdaB s.t. AB * lambda = c
            double[] lambdaB = new double[dim];
            Mat.Gaussian(Mat.transpose(AB), c, lambdaB);

            int j = 9999; // lambdaB_j < 0
            for (int i = 0; i < dim; i++) {
                if (lambdaB[i] < 0 && base[i] < j)
                    j = base[i];
            }

            if (j == 9999) 
                return 0;

            int jj = -1;
            for (int i = 0; i < dim; i++) {
                if (j == base[i]) jj = i;
            }

            // column jj of inverse AB
            double[] d = new double[dim];
            double[] last = new double[dim];
            last[jj] = 1;
            Mat.Gaussian(AB, last, d);

            // calculate K = a * (-d)
            double[] K = new double[a.length];
            List<Integer> S = new ArrayList<Integer>();
            for (int i = 0; i < K.length; i++) {
                for (int ii = 0; ii < dim; ii++) {
                    K[i] -= a[i][ii] * d[ii];
                }

                if (K[i] > 0)
                    S.add(i);
            }

            int SLen = S.size();
            double min = 9999;
            int k = -1;
            for (int i = 0; i < SLen; i++) {
                int S_i = S.get(i);
                double res = b[S_i];
                double denominator = 0.0;
                for (int ii = 0; ii < dim; ii++) {
                    res -= a[S_i][ii] * x[ii];
                    denominator -= a[S_i][ii] * d[ii];
                }
                res /= denominator;
                if (res < min) {
                    min = res;
                    k = S_i;
                }
            }

            if (k == -1) {
                // unbounded
                return 1;
            }

            base[jj] = k;
        }
    }

    void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n + m][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        for (int i = n; i < n + m; i++)
            A[i][i-n] = -1;
        double[] b = new double[n + m];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        double[] ansx = new double[m];
        int anst = solveDietProblem(n+m, m, A, b, c, ansx);
        if (anst == -1) {
            out.printf("No solution\n");
            return;
        }
        if (anst == 0) {
            out.printf("Bounded solution\n");
            for (int i = 0; i < m; i++) {
                out.printf("%.18f%c", ansx[i], i + 1 == m ? '\n' : ' ');
            }
            return;
        }
        if (anst == 1) {
            out.printf("Infinity\n");
            return;
        }
    }

    void add(List<Integer> list) {
        list.add(1);
    }

    Diet() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new Diet();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
}
