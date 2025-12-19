import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day09 extends GenericDay {

    public static void main(String[] args) {
        new Day09().solve();
    }

    public Day09() {
        super(9, false, false);
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

        ArrayList<Line> horizontalLines = getLinesPart2(coords, true);
        ArrayList<Line> verticalLines = getLinesPart2(coords, false);
        long result = 0;

        for (int i = 0; i < coords.length; i++) {
            Integer[] pair1 = coords[i];
            for (int j = i + 1; j < coords.length; j++) {
                Integer[] pair2 = coords[j];

                if (!checkValidityPart2(pair1, pair2, horizontalLines, verticalLines)) {
                    continue;
                }

                long area = (long) (Math.max(pair1[0], pair2[0]) - Math.min(pair1[0], pair2[0]) + 1)
                        * (Math.max(pair1[1], pair2[1]) - Math.min(pair1[1], pair2[1]) + 1);
                if (area > result) {
                    result = area;
                }
            }
        }

        return String.valueOf(result);
    }

    private ArrayList<Line> getLinesPart2(Integer[][] coords, boolean horizontal) {
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

        ArrayList<Line> lines = new ArrayList<Line>();
        int lineIndex = horizontal ? 0 : 1;

        for (int i = 0; i < coords.length; i += 2) {
            Integer[] first = coords[i];
            Integer[] second = coords[i + 1];
            lines.add(new Line(first[lineIndex], second[lineIndex], first[1 - lineIndex]));
        }

        return lines;
    }

    private boolean checkValidityPart2(Integer[] start, Integer[] end, ArrayList<Line> horizontalLines,
            ArrayList<Line> verticalLines) {

        int lowCol = Math.min(start[0], end[0]);
        int highCol = Math.max(start[0], end[0]);
        int lowRow = Math.min(start[1], end[1]);
        int highRow = Math.max(start[1], end[1]);

        return !(checkForIntersectionPart2(lowCol, highCol, lowRow, verticalLines, true) ||
                checkForIntersectionPart2(lowCol, highCol, highRow, verticalLines, false) ||
                checkForIntersectionPart2(lowRow, highRow, lowCol, horizontalLines, true) ||
                checkForIntersectionPart2(lowRow, highRow, highCol, horizontalLines, false));

    }

    private boolean checkForIntersectionPart2(int startCoord1, int endCoord1, int startCoord2,
            ArrayList<Line> lines, boolean isLowSide) {
        for (Line line : lines) {
            int lineStart = line.getStart();
            int lineEnd = line.getEnd();
            int linePos = line.getPos();

            if (linePos <= Math.min(startCoord1, endCoord1) || linePos >= Math.max(startCoord1, endCoord1)) {
                continue;
            }

            if (startCoord2 < lineStart || startCoord2 > lineEnd) {
                continue;
            }

            if (startCoord2 > lineStart && startCoord2 < lineEnd) {
                return true;
            }

            if (isLowSide && startCoord2 == lineEnd) {
                continue;
            }

            if (!isLowSide && startCoord2 == lineStart) {
                continue;
            }

            return true;
        }

        return false;
    }

    private class Line {
        private int start, end, position;

        public Line(int point1, int point2, int position) {
            this.start = Math.min(point1, point2);
            this.end = Math.max(point1, point2);
            this.position = position;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int getPos() {
            return position;
        }
    }

}