import java.util.List;

public class Day04 extends GenericDay {
    static final char ROLL_MARK = '@';

    public static void main(String[] args) {
        new Day04().solve();
    }

    public Day04() {
        super(4, false, false);
    }

    @Override
    public String part1(List<String> input) {
        int result = 0;

        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                if (input.get(i).charAt(j) == ROLL_MARK) {
                    if (isAccessiblePart1(input, i, j)) {
                        result++;
                    }
                }
            }
        }

        return String.valueOf(result);
    }

    private boolean isAccessiblePart1(List<String> grid, int row, int col) {
        int allowedRolls = 4; // Roll being checked + max of 3 others
        int rollCount = 0;

        int gridHeight = grid.size();
        int gridWidth = grid.get(0).length();

        int checkedRowMin = row - 1 > 0 ? row - 1 : 0;
        int checkedRowMax = row + 2 < gridHeight ? row + 2 : gridHeight;
        int checkedColMin = col - 1 > 0 ? col - 1 : 0;
        int checkedColMax = col + 2 < gridWidth ? col + 2 : gridWidth;

        for (int i = checkedRowMin; i < checkedRowMax; i++) {
            for (int j = checkedColMin; j < checkedColMax; j++) {
                if (grid.get(i).charAt(j) == ROLL_MARK) {
                    rollCount++;
                }
            }
        }

        return rollCount <= allowedRolls;
    }

    @Override
    public String part2(List<String> input) {
        boolean[][] grid = new boolean[input.size()][input.get(0).length()];

        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                grid[i][j] = input.get(i).charAt(j) == ROLL_MARK;
            }
        }

        int result = 0;
        int removedThisPass;

        do {
            removedThisPass = 0;

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j]) {
                        if (isAccessiblePart2(grid, i, j)) {
                            removedThisPass++;
                            grid[i][j] = false;
                        }
                    }
                }
            }

            result += removedThisPass;
        } while (removedThisPass > 0);

        return String.valueOf(result);
    }

    private boolean isAccessiblePart2(boolean[][] grid, int row, int col) {
        int allowedRolls = 4; // Roll being checked + max of 3 others
        int rollCount = 0;

        int gridHeight = grid.length;
        int gridWidth = grid[0].length;

        int checkedRowMin = row - 1 > 0 ? row - 1 : 0;
        int checkedRowMax = row + 2 < gridHeight ? row + 2 : gridHeight;
        int checkedColMin = col - 1 > 0 ? col - 1 : 0;
        int checkedColMax = col + 2 < gridWidth ? col + 2 : gridWidth;

        for (int i = checkedRowMin; i < checkedRowMax; i++) {
            for (int j = checkedColMin; j < checkedColMax; j++) {
                if (grid[i][j]) {
                    rollCount++;
                }
            }
        }

        return rollCount <= allowedRolls;
    }

}