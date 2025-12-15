import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day06 extends GenericDay {
    public static void main(String[] args) {
        new Day06().solve();
    }

    public Day06() {
        super(6, false, false);
    }

    @Override
    public String part1(List<String> input) {
        String[] operators = input.get(input.size() - 1).split("\s+");
        long[][] operands = new long[operators.length][input.size() - 1];

        for (int i = 0; i < input.size() - 1; i++) {
            long[] nextOperands = Arrays.stream(input.get(i).trim().split("\s+"))
                    .mapToLong(boundStr -> Long.parseLong(boundStr))
                    .toArray();
            for (int j = 0; j < nextOperands.length; j++) {
                operands[j][i] = nextOperands[j];
            }
        }

        long result = 0;

        for (int i = 0; i < operators.length; i++) {
            switch (operators[i]) {
                case "+":
                    result += Arrays.stream(operands[i]).sum();
                    break;
                case "*":
                    result += Arrays.stream(operands[i]).reduce(1, (a, b) -> a * b);
                    break;
                default:
                    System.out.println("Unknown operator: " + operators[i]);
            }
        }

        return String.valueOf(result);
    }

    @Override
    public String part2(List<String> input) {
        String[] operators = input.get(input.size() - 1).split("\s+");
        for (int i = 0; i < operators.length / 2; i++) {
            String temp = operators[i];
            operators[i] = operators[operators.length - 1 - i];
            operators[operators.length - 1 - i] = temp;
        }

        ArrayList<ArrayList<Long>> operands = new ArrayList<ArrayList<Long>>();

        int problemNum = 0;
        operands.add(new ArrayList<Long>());

        for (int i = input.get(0).length() - 1; i >= 0; i--) {

            String operandStr = "";
            for (int j = 0; j < input.size(); j++) {
                operandStr += input.get(j).charAt(i);
            }

            char operatorFlag = operandStr.charAt(operandStr.length() - 1);
            if (operatorFlag == ' ') {
                operands.get(problemNum).add(Long.parseLong(operandStr.trim()));
            } else {
                operands.get(problemNum).add(Long.parseLong(operandStr.substring(0, operandStr.length() - 1).trim()));
                problemNum++;
                i--; // Blank column to skip between problems
                if (i > 0) {
                    operands.add(new ArrayList<Long>());
                }
            }
        }

        long result = 0;

        for (int i = 0; i < operators.length; i++) {
            switch (operators[i]) {
                case "+":
                    // Have to make own sum method b/c stream is Stream<Long> not LongStream
                    result += operands.get(i).stream().reduce(Long.valueOf(0), (a, b) -> a + b);
                    break;
                case "*":
                    result += operands.get(i).stream().reduce(Long.valueOf(1), (a, b) -> a * b);
                    break;
                default:
                    System.out.println("Unknown operator: " + operators[i]);
            }
        }

        return String.valueOf(result);
    }

}