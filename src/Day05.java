import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Day05 extends GenericDay {
    public static void main(String[] args) {
        new Day05().solve();
    }

    public Day05() {
        super(5, false, false);
    }

    @Override
    public String part1(List<String> input) {
        int separatorIndex = input.indexOf("");
        List<String> freshRanges = input.subList(0, separatorIndex);
        List<Long> availableIds = input.subList(separatorIndex + 1, input.size())
                .stream()
                .map(idStr -> Long.parseLong(idStr))
                .collect(Collectors.toList());

        int result = 0;

        for (String range : freshRanges) {
            long[] bounds = Arrays.stream(range.split("-"))
                    .mapToLong(boundStr -> Long.parseLong(boundStr))
                    .toArray();

            Iterator<Long> iterator = availableIds.iterator();

            while (iterator.hasNext()) {
                long id = iterator.next();
                if (id >= bounds[0] && id <= bounds[1]) {
                    result++;
                    iterator.remove();
                }
            }

        }

        return String.valueOf(result);
    }

    @Override
    public String part2(List<String> input) {
        List<long[]> freshRanges = input.subList(0, input.indexOf(""))
                .stream()
                .map(rangeStr -> Arrays.stream(rangeStr.split("-"))
                        .mapToLong(boundStr -> Long.parseLong(boundStr))
                        .toArray())
                .collect(Collectors.toList());

        freshRanges.sort((range1, range2) -> {
            return Long.compare(range1[0], range2[0]);
        });

        Iterator<long[]> iterator = freshRanges.iterator();
        long[] currentRange = iterator.next();
        long[] nextRange;
        while (iterator.hasNext()) {
            nextRange = iterator.next();

            if (currentRange[1] >= nextRange[0]) {
                currentRange[1] = Math.max(currentRange[1], nextRange[1]);
                iterator.remove();
            } else {
                currentRange = nextRange;
            }
        }

        long result = 0;

        for (long[] range : freshRanges) {
            result += range[1] - range[0] + 1;
        }

        return String.valueOf(result);
    }

}