import java.util.List;

public class Day01 extends GenericDay {
    public static void main(String[] args) {
        new Day01().solve();
    }

    public Day01() {
        super(1, false, false);
    }

    @Override
    public String part1(List<String> input) {
        int result = 0;

        int rotation = 50;
        for (String line : input) {
            rotation += Integer.parseInt(line.replace("L", "-").replace("R", ""));
            if (rotation % 100 == 0) {
                result++;
            }
        }

        return Integer.toString(result);
    }

    @Override
    public String part2(List<String> input) {
        int result = 0;

        int rotation = 50;
        for (String line : input) {
            int newRotation = rotation + Integer.parseInt(line.replace("L", "-").replace("R", ""));

            result += Math.abs((int) (newRotation / 100)) + (newRotation <= 0 && rotation != 0 ? 1 : 0);

            rotation = newRotation % 100;
            rotation = rotation < 0 ? rotation + 100 : rotation;
        }

        return Integer.toString(result);
    }
}
