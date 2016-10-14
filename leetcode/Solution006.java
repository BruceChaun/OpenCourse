public class Solution {
    public String convert(String s, int numRows) {
        int len = s.length();
        if (numRows <= 1 || numRows >= len) 
            return s;
        
        int row = numRows;
        int step = 2 * row - 2;
        String ret = "";
        for (int r = 0; r < row; r++) {
            for (int t = 0; t < len / step + 1; t++) {
                int i = t * step + r;
                if (i >= 0 && i < len)
                    ret += s.charAt(i);
                i += step - 2 * r;
                if (r != row - 1 && r != 0 && i < len)
                    ret += s.charAt(i);
            }
        }
        /*int col = (row-1) * (len/step+1);
        char[][] matrix = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = '\0';
            }
        }
        int curRow = 0, curCol = 0;
        matrix[0][0] = s.charAt(0);
        for (int i = 1; i < len; i++) {
            if (curRow == row - 1) {
                curRow--;
                curCol++;
            } else if (curRow == 0) {
                curRow++;
            } else {
                if (curCol % (row-1) == 0) {
                    curRow++;
                } else {
                    curRow--;
                    curCol++;
                }
            }
            matrix[curRow][curCol] = s.charAt(i);
        }
        String ret = "";
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] != '\0')
                    ret += matrix[i][j];
                //System.out.print(matrix[i][j] + " ");
            }
            // System.out.println();
        }*/
        return ret;
    }
}
