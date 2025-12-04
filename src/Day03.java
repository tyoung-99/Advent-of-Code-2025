import java.util.Arrays;
import java.util.List;

public class Day03 extends GenericDay {
    public static void main(String[] args) {
        new Day03().solve();
    }

    public Day03() {
        super(3, false, false);
    }

    @Override
    public String part1(List<String> input) {
        int result = 0;

        for (String bank : input) {
            char tens = '0', ones = '0';
            for (int i = 0; i < bank.length(); i++) {
                char battery = bank.charAt(i);
                // Tens place can never be last char
                if (i < bank.length() - 1 && battery > tens) {
                    tens = battery;
                    ones = '0';
                } else if (battery > ones) {
                    ones = battery;
                }
            }
            result += (tens - '0') * 10 + ones - '0';
        }

        return String.valueOf(result);
    }

    @Override
    public String part2(List<String> input) {
        long result = 0;
        int bankLength = input.get(0).length(); // All banks have same length

        for (String bank : input) {
            char[] digits = new char[12];
            Arrays.fill(digits, '0');
            int numDigits = digits.length;

            for (int i = 0; i < bankLength; i++) {
                char battery = bank.charAt(i);
                for (int j = 0; j < numDigits; j++) {
                    if (bankLength - i >= numDigits - j && battery > digits[j]) {
                        digits[j] = battery;
                        for (int k = j + 1; k < numDigits; k++) {
                            digits[k] = '0';
                        }
                        break;
                    }
                }
            }

            result += Long.parseLong(new String(digits));
        }

        return String.valueOf(result);
    }

}