import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day09Test extends GenericDay {

    public static void main(String[] args) {
        new Day09Test().solve();
    }

    public Day09Test() {
        super(9, false, true);
    }

    @Override
    public String part1(List<String> input) {
        int[][] coords = new int[input.size()][2];

        for (int i = 0; i < input.size(); i++) {
            coords[i] = Arrays.stream(input.get(i).split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        long result = 0;

        for (int i = 0; i < coords.length; i++) {
            int[] pair1 = coords[i];
            for (int j = i + 1; j < coords.length; j++) {
                int[] pair2 = coords[j];
                long area = (long) ((pair1[0] > pair2[0] ? pair1[0] - pair2[0] : pair2[0] - pair1[0]) + 1)
                        * ((pair1[1] > pair2[1] ? pair1[1] - pair2[1] : pair2[1] - pair1[1]) + 1);
                if (area > result) {
                    result = area;
                }
            }
        }

        return String.valueOf(result);
    }

    @Override
    public String part2(List<String> input) {
        Integer[][] coords = new Integer[input.size()][2];

        for (int i = 0; i < input.size(); i++) {
            coords[i] = Arrays.stream(input.get(i).split(","))
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);
        }

        ArrayList<Integer[]> horizontalLines = getLines(coords, true);
        ArrayList<Integer[]> verticalLines = getLines(coords, false);
        long result = 0;

        for (int i = 0; i < coords.length; i++) {
            Integer[] pair1 = coords[i];
            for (int j = i + 1; j < coords.length; j++) {
                Integer[] pair2 = coords[j];

                // TODO: check for intersection

                long area = (long) ((pair1[0] > pair2[0] ? pair1[0] - pair2[0] : pair2[0] - pair1[0]) + 1)
                        * ((pair1[1] > pair2[1] ? pair1[1] - pair2[1] : pair2[1] - pair1[1]) + 1);
                if (area > result) {
                    result = area;
                }
            }
        }

        return String.valueOf(result);
    }

    private ArrayList<Integer[]> getLines(Integer[][] coords, boolean horizontal) {
        int firstCompareIndex = horizontal ? 1 : 0;

        Arrays.sort(coords, new Comparator<Integer[]>() {
            public int compare(Integer[] a, Integer[] b) {
                int compared = a[firstCompareIndex].compareTo(b[firstCompareIndex]);
                if (compared != 0) {
                    return compared;
                }
                return a[1 - firstCompareIndex].compareTo(b[1 - firstCompareIndex]);
            }
        });

        ArrayList<Integer[]> lines = new ArrayList<Integer[]>();
        int lineIndex = horizontal ? 0 : 1;

        for (int i = 0; i < coords.length; i += 2) {
            Integer[] first = coords[i];
            Integer[] second = coords[i + 1];
            lines.add(new Integer[] { first[lineIndex], second[lineIndex], first[1 - lineIndex] });
        }

        return lines;
    }

    private boolean checkForIntersection(int startCoord1, int startCoord2, int endCoord1, ArrayList<Integer[]> lines) {
        for (Integer[] line : lines) {
            if (startCoord2 >= line[0]
                    && startCoord2 <= line[1]
                    && line[2] > Math.min(startCoord1, endCoord1)
                    && line[2] < Math.max(startCoord1, endCoord1)) {
                return true;
            }
        }
        return false;
    }

}