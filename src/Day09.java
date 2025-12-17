import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day09 extends GenericDay {

    public static void main(String[] args) {
        new Day09().solve();
    }

    public Day09() {
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
        ArrayList<ArrayList<Integer[]>> coordsByRow = formatCoordsPart2(input);
        ArrayList<Integer[]> legalColRanges = new ArrayList<Integer[]>();

        ArrayList<Integer[]> possibleTopCorners = new ArrayList<Integer[]>();
        ArrayList<Integer[]> possibleBottomCorners = new ArrayList<Integer[]>();

        long result = 0;

        for (ArrayList<Integer[]> row : coordsByRow) {
            for (Integer[] pair : row) {
                System.out.printf("(%d, %d); ", pair[0], pair[1]);
            }
            System.out.println();
        }
        System.out.println();

        for (ArrayList<Integer[]> row : coordsByRow) {
            // Need to check validity after adding area for addition, but before removing
            // for subtraction
            boolean isAddition = true;
            ArrayList<Integer[]> newRanges = new ArrayList<Integer[]>(legalColRanges);

            for (int i = 0; i < row.size(); i += 2) {
                int left = row.get(i)[0];
                int right = row.get(i + 1)[0];
                ArrayList<Integer> affectedRanges = findAffectedRangesPart2(left, right, legalColRanges);

                // Disconnected addition
                if (affectedRanges.size() == 0) {
                    newRanges.add(new Integer[] { left, right });
                    sortRangesPart2(newRanges);
                }

                // Connected addition, both sides
                else if (affectedRanges.size() == 2) {
                    int leftIndex = affectedRanges.get(0);
                    int rightIndex = affectedRanges.get(1);

                    newRanges.get(leftIndex)[1] = newRanges.get(rightIndex)[1];
                    newRanges.remove(rightIndex);
                }

                else {
                    Integer[] affectedRange = newRanges.get(affectedRanges.get(0));

                    // Connected addition, left side
                    if (affectedRange[0] == right) {
                        affectedRange[0] = left;
                    }

                    // Connected addition, right side
                    else if (affectedRange[1] == left) {
                        affectedRange[1] = right;
                    }

                    // Subtraction of entire range
                    else if (affectedRange[0] == left && affectedRange[1] == right) {
                        newRanges.remove(affectedRange);
                        isAddition = false;
                    }

                    // Subtraction from left
                    else if (affectedRange[0] == left) {
                        affectedRange[0] = right;
                        isAddition = false;
                    }

                    // Subtraction from right
                    else if (affectedRange[1] == right) {
                        affectedRange[1] = left;
                        isAddition = false;
                    }

                    // Subtraction from interior
                    else {
                        newRanges.add(new Integer[] { right, affectedRange[1] });
                        affectedRange[1] = left;
                        sortRangesPart2(newRanges);
                        isAddition = false;
                    }
                }
            }

            if (isAddition) {
                legalColRanges = newRanges;
                possibleTopCorners.addAll(row);
                possibleBottomCorners.addAll(row);
            }

            for (Integer[] top : possibleTopCorners) {
                for (Integer[] bottom : possibleBottomCorners) {
                    long area = (long) ((top[0] > bottom[0] ? top[0] - bottom[0] : bottom[0] - top[0]) + 1)
                            * (bottom[1] - top[1] + 1);
                    if (area > result) {
                        result = area;
                    }
                }
            }

            if (!isAddition) {
                legalColRanges = newRanges;
            }

            for (Integer[] bounds : legalColRanges) {
                System.out.printf("[%d-%d] ", bounds[0], bounds[1]);
            }
            System.out.println();
        }

        return String.valueOf(result);
    }

    private ArrayList<ArrayList<Integer[]>> formatCoordsPart2(List<String> input) {
        Integer[][] coords = new Integer[input.size()][2];

        for (int i = 0; i < input.size(); i++) {
            coords[i] = Arrays.stream(input.get(i).split(","))
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);
        }

        Arrays.sort(coords, new Comparator<Integer[]>() {
            public int compare(Integer[] first, Integer[] second) {
                int rowCompared = first[1].compareTo(second[1]);
                if (rowCompared != 0) {
                    return rowCompared;
                }
                return first[0].compareTo(second[0]);
            }
        });

        ArrayList<ArrayList<Integer[]>> coordsByRow = new ArrayList<ArrayList<Integer[]>>();

        ArrayList<Integer[]> currentRow = new ArrayList<Integer[]>();
        coordsByRow.add(currentRow);
        for (Integer[] pair : coords) {
            if (!currentRow.isEmpty() && !pair[1].equals(currentRow.get(0)[1])) {
                currentRow = new ArrayList<Integer[]>();
                coordsByRow.add(currentRow);
            }
            currentRow.add(pair);
        }

        return coordsByRow;
    }

    private void sortRangesPart2(ArrayList<Integer[]> ranges) {
        ranges.sort(new Comparator<Integer[]>() {
            public int compare(Integer[] first, Integer[] second) {
                return first[0].compareTo(second[0]);
            }
        });

    }

    private ArrayList<Integer> findAffectedRangesPart2(int left, int right, ArrayList<Integer[]> ranges) {
        ArrayList<Integer> affectedIndices = new ArrayList<Integer>();

        for (int i = 0; i < ranges.size(); i++) {
            Integer[] bounds = ranges.get(i);

            if (left <= bounds[1] && right >= bounds[0]) {
                affectedIndices.add(i);
            }
        }

        return affectedIndices;
    }

}