import java.util.Arrays;
import java.util.List;

public class Day02 extends GenericDay {
    public static void main(String[] args) {
        new Day02().solve();
    }

    public Day02() {
        super(2, false, true);
    }

    @Override
    public String part1(List<String> input) {
        input = Arrays.asList(input.get(0).split(","));

        long invalidIdsSum = 0;
        for (String range : input) {
            String[] bounds = range.split("-");

            /**
             * Halving start/end by length allows checking for only invalid IDs, since no
             * IDs between will ever be invalid.
             * E.g., between 55 and 88, only 55, 66, 77, 88 are invalid, so counting 5-8
             * skips unnecessary checks
             */
            long start = findNextInvalidIdPart1(Long.parseLong(bounds[0]));
            String halfStartStr = String.valueOf(start).substring(0, String.valueOf(start).length() / 2);
            long halfStart = Long.parseLong(halfStartStr);

            long end = findPrevInvalidIdPart1(Long.parseLong(bounds[1]));
            String halfEndString = String.valueOf(end).substring(0, String.valueOf(end).length() / 2);
            long halfEnd = Long.parseLong(halfEndString);

            for (long i = halfStart; i <= halfEnd; i++) {
                String halfIdStr = String.valueOf(i);
                invalidIdsSum += Long.parseLong(halfIdStr + halfIdStr);
            }
        }
        return String.valueOf(invalidIdsSum);
    }

    private long findNextInvalidIdPart1(long start) {
        String startStr = String.valueOf(start);

        int strLen = startStr.length();
        if ((strLen & 1) == 1) {
            // Odd length IDs can never be invalid, round up to next even length ID
            start = (long) Math.pow(10, strLen);
            startStr = String.valueOf(start);
        }

        String halfStartStr = startStr.substring(0, startStr.length() / 2);

        long possibleId = Long.parseLong(halfStartStr + halfStartStr);
        if (possibleId < start) {
            halfStartStr = String.valueOf(Long.parseLong(halfStartStr) + 1);
            possibleId = Long.parseLong(halfStartStr + halfStartStr);
        }

        return possibleId;
    }

    private long findPrevInvalidIdPart1(long end) {
        String endStr = String.valueOf(end);

        int strLen = endStr.length();
        if ((strLen & 1) == 1) {
            // Odd length IDs can never be invalid, round down to previous even length ID,
            // which is automatically invalid since it's all 9s
            return (long) Math.pow(10, strLen - 1) - 1;
        }

        String halfEndStr = endStr.substring(0, endStr.length() / 2);

        long possibleId = Long.parseLong(halfEndStr + halfEndStr);
        if (possibleId > end) {
            halfEndStr = String.valueOf(Long.parseLong(halfEndStr) - 1);
            possibleId = Long.parseLong(halfEndStr + halfEndStr);
        }

        return possibleId;
    }

    @Override
    public String part2(List<String> input) {
        return "Part 2";
    }
}
