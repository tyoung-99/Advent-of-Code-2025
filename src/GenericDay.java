import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDay implements DaySolution {

    private List<String> part1Input, part2Input;

    public GenericDay(int day, boolean part1IsExample, boolean part2IsExample) {
        String folder = "inputs/day" + String.format("%02d", day);
        part1Input = readInput(
                String.format("%s/Part1%s.txt", folder, part1IsExample ? "Example" : ""));
        part2Input = readInput(
                String.format("%s/Part2%s.txt", folder, part2IsExample ? "Example" : ""));
    }

    protected void solve() {
        System.out.printf("Part 1: %s\nPart 2: %s\n", part1(part1Input), part2(part2Input));
    }

    private List<String> readInput(String inputPath) {
        ArrayList<String> input = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line = reader.readLine();
            while (line != null) {
                input.add(line);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Failed to read from file: " + inputPath);
        }

        return input;
    }

}
