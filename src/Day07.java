import java.util.HashSet;
import java.util.List;

public class Day07 extends GenericDay {

    public static void main(String[] args) {
        new Day07().solve();
    }

    public Day07() {
        super(7, false, false);
    }

    @Override
    public String part1(List<String> input) {
        final char SPLITTER_FLAG = '^';
        int result = 0;

        final int LAST_ROW = input.size();
        int row = 1;
        HashSet<Integer> cols = new HashSet<Integer>();
        cols.add(input.get(0).indexOf('S'));

        while (row < LAST_ROW) {
            HashSet<Integer> newCols = new HashSet<Integer>(cols);
            for (Integer col : cols) {
                if (input.get(row).charAt(col) == SPLITTER_FLAG) {
                    newCols.remove(col);
                    newCols.add(col - 1);
                    newCols.add(col + 1);
                    result++;
                }
            }
            cols = newCols;
            row++;
        }

        return String.valueOf(result);
    }

    @Override
    public String part2(List<String> input) {
        final String SPLITTER_FLAG = "^";
        String[][] grid = new String[input.size()][input.get(0).length()];
        for (int i = 0; i < input.size(); i++) {
            grid[i] = input.get(i).split("");
        }

        final int LAST_ROW = grid.length;
        int row = 1;
        int startCol = input.get(0).indexOf('S');
        HashSet<Integer> cols = new HashSet<Integer>();
        cols.add(startCol);

        grid[row][startCol] = "1";

        while (row < LAST_ROW) {
            HashSet<Integer> newCols = new HashSet<Integer>(cols);
            for (Integer col : cols) {
                if (grid[row][col].equals(SPLITTER_FLAG)) {
                    newCols.remove(col);
                    newCols.add(col - 1);
                    newCols.add(col + 1);

                    long ownCount = Long.parseLong(grid[row - 1][col]);

                    String newLeft = String.valueOf(ownCount);
                    if (!grid[row][col - 1].equals(".")) {
                        newLeft = String.valueOf(Long.parseLong(grid[row][col - 1]) + ownCount);
                    }
                    grid[row][col - 1] = newLeft;

                    String newRight = String.valueOf(ownCount);
                    if (!grid[row][col + 1].equals(".")) {
                        newRight = String.valueOf(Long.parseLong(grid[row][col + 1]) + ownCount);
                    }
                    grid[row][col - 1] = newRight;

                    for (int i = row; i < LAST_ROW && !grid[i][col - 1].equals(SPLITTER_FLAG); i++) {
                        grid[i][col - 1] = newLeft;
                    }
                    for (int i = row; i < LAST_ROW && !grid[i][col + 1].equals(SPLITTER_FLAG); i++) {
                        grid[i][col + 1] = newRight;
                    }
                }
            }
            cols = newCols;
            row++;
        }

        long result = 0;

        for (String count : grid[grid.length - 1]) {
            if (!count.equals(".")) {
                result += Long.parseLong(count);
            }
        }

        return String.valueOf(result);
    }

}