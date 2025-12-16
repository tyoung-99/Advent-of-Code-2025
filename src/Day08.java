import java.util.ArrayList;
import java.util.List;

public class Day08 extends GenericDay {
    private static boolean part1IsExample = false;

    public static void main(String[] args) {
        new Day08().solve();
    }

    public Day08() {
        super(8, part1IsExample, false);
    }

    @Override
    public String part1(List<String> input) {
        ArrayList<CircuitPart1> circuits = new ArrayList<CircuitPart1>();

        for (String line : input) {
            String[] coords = line.split(",");
            circuits.add(
                    new CircuitPart1(
                            new JunctionBoxPart1(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                                    Integer.parseInt(coords[2]))));
        }

        int MAX_ITERATIONS = part1IsExample ? 10 : 1000;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double shortestDistance = Double.POSITIVE_INFINITY;
            CircuitPart1 circuitA = null, circuitB = null;
            JunctionBoxPart1 boxA = null, boxB = null;

            for (int j = 0; j < circuits.size(); j++) {
                for (int k = j; k < circuits.size(); k++) {
                    JunctionBoxPart1[] closestPair = circuits.get(j).findClosestBoxes(circuits.get(k));
                    // closestPair will have nulls if both circuits are the same, and circuit has
                    // only 1 box
                    if (closestPair[0] == null) {
                        continue;
                    }

                    double distance = closestPair[0].distanceToBox(closestPair[1]);

                    if (distance < shortestDistance) {
                        shortestDistance = distance;

                        circuitA = circuits.get(j);
                        boxA = closestPair[0];

                        circuitB = circuits.get(k);
                        boxB = closestPair[1];
                    }
                }
            }

            boxA.makeConnection(boxB);

            if (circuitA != circuitB) {
                circuitA.joinCircuits(circuitB);
                circuits.remove(circuitB);
            }
        }

        circuits.sort(null);

        int result = 1;

        for (int i = 0; i < 3; i++) {
            result *= circuits.get(i).size();
        }

        return String.valueOf(result);
    }

    private class CircuitPart1 implements Comparable<CircuitPart1> {
        private ArrayList<JunctionBoxPart1> boxes;

        public CircuitPart1(JunctionBoxPart1 startingBox) {
            boxes = new ArrayList<JunctionBoxPart1>();
            boxes.add(startingBox);
        }

        public void joinCircuits(CircuitPart1 otherCircuit) {
            boxes.addAll(otherCircuit.boxes);
            otherCircuit.boxes.clear();
        }

        public JunctionBoxPart1[] findClosestBoxes(CircuitPart1 otherCircuit) {
            JunctionBoxPart1[] closestPair = new JunctionBoxPart1[2];
            double shortestDistance = Double.POSITIVE_INFINITY;

            for (JunctionBoxPart1 myBox : boxes) {
                for (JunctionBoxPart1 theirBox : otherCircuit.boxes) {
                    double distance = myBox.distanceToBox(theirBox);
                    if (distance < shortestDistance && distance > 0) {
                        shortestDistance = distance;
                        closestPair[0] = myBox;
                        closestPair[1] = theirBox;
                    }
                }
            }

            return closestPair;
        }

        public int size() {
            return boxes.size();
        }

        @Override
        public int compareTo(CircuitPart1 other) {
            return other.boxes.size() - boxes.size();
        }

        @Override
        public String toString() {
            String output = "Circuit: ";

            for (JunctionBoxPart1 box : boxes) {
                output += box.toString() + ", ";
            }

            // Substring removes extra comma
            return output.substring(0, output.length() - 2);
        }

    }

    private class JunctionBoxPart1 {
        private int x, y, z;
        private ArrayList<JunctionBoxPart1> connectedTo;

        public JunctionBoxPart1(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            connectedTo = new ArrayList<JunctionBoxPart1>();
        }

        public double distanceToBox(JunctionBoxPart1 otherBox) {
            if (connectedTo.contains(otherBox)) {
                return -1;
            }
            return Math.sqrt(Math.pow(otherBox.x - x, 2) + Math.pow(otherBox.y - y, 2) + Math.pow(otherBox.z - z, 2));
        }

        public void makeConnection(JunctionBoxPart1 otherBox) {
            connectedTo.add(otherBox);
            otherBox.connectedTo.add(this);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d)", x, y, z);
        }

    }

    @Override
    public String part2(List<String> input) {
        ArrayList<CircuitPart2> circuits = new ArrayList<CircuitPart2>();

        for (String line : input) {
            String[] coords = line.split(",");
            circuits.add(
                    new CircuitPart2(
                            new JunctionBoxPart2(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
                                    Integer.parseInt(coords[2]))));
        }

        JunctionBoxPart2 boxA = null, boxB = null;

        while (circuits.size() > 1) {
            double shortestDistance = Double.POSITIVE_INFINITY;
            CircuitPart2 circuitA = null, circuitB = null;

            for (int j = 0; j < circuits.size(); j++) {
                for (int k = j + 1; k < circuits.size(); k++) {
                    JunctionBoxPart2[] closestPair = circuits.get(j).findClosestBoxes(circuits.get(k));

                    double distance = closestPair[0].distanceToBox(closestPair[1]);

                    if (distance < shortestDistance) {
                        shortestDistance = distance;

                        circuitA = circuits.get(j);
                        boxA = closestPair[0];

                        circuitB = circuits.get(k);
                        boxB = closestPair[1];
                    }
                }
            }

            if (circuitA != circuitB) {
                circuitA.joinCircuits(circuitB);
                circuits.remove(circuitB);
            }
        }

        return String.valueOf((long) boxA.distanceToWall() * boxB.distanceToWall());
    }

    private class CircuitPart2 implements Comparable<CircuitPart2> {
        private ArrayList<JunctionBoxPart2> boxes;

        public CircuitPart2(JunctionBoxPart2 startingBox) {
            boxes = new ArrayList<JunctionBoxPart2>();
            boxes.add(startingBox);
        }

        public void joinCircuits(CircuitPart2 otherCircuit) {
            boxes.addAll(otherCircuit.boxes);
            otherCircuit.boxes.clear();
        }

        public JunctionBoxPart2[] findClosestBoxes(CircuitPart2 otherCircuit) {
            JunctionBoxPart2[] closestPair = new JunctionBoxPart2[2];
            double shortestDistance = Double.POSITIVE_INFINITY;

            for (JunctionBoxPart2 myBox : boxes) {
                for (JunctionBoxPart2 theirBox : otherCircuit.boxes) {
                    double distance = myBox.distanceToBox(theirBox);
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        closestPair[0] = myBox;
                        closestPair[1] = theirBox;
                    }
                }
            }

            return closestPair;
        }

        @Override
        public int compareTo(CircuitPart2 other) {
            return other.boxes.size() - boxes.size();
        }

        @Override
        public String toString() {
            String output = "Circuit: ";

            for (JunctionBoxPart2 box : boxes) {
                output += box.toString() + ", ";
            }

            // Substring removes extra comma
            return output.substring(0, output.length() - 2);
        }

    }

    private class JunctionBoxPart2 {
        private int x, y, z;

        public JunctionBoxPart2(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double distanceToBox(JunctionBoxPart2 otherBox) {
            return Math.sqrt(Math.pow(otherBox.x - x, 2) + Math.pow(otherBox.y - y, 2) + Math.pow(otherBox.z - z, 2));
        }

        public int distanceToWall() {
            return x;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d)", x, y, z);
        }

    }

}