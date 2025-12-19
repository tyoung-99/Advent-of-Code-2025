import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Day10 extends GenericDay {

    public static void main(String[] args) {
        new Day10().solve();
    }

    public Day10() {
        super(10, false, true);
    }

    @Override
    public String part1(List<String> input) {
        ArrayList<MachinePart1> machines = parseInputPart1(input);

        int result = 0;

        for (MachinePart1 machine : machines) {
            int presses = getMinPressesPart1(machine);
            if (presses < 0) {
                return "The following machine couldn't be configured with the buttons available:\n"
                        + machine.toString();
            }
            result += presses;
        }

        return String.valueOf(result);
    }

    private ArrayList<MachinePart1> parseInputPart1(List<String> input) {
        final char LIGHT_ON = '#';
        ArrayList<MachinePart1> machines = new ArrayList<MachinePart1>();

        for (String line : input) {
            int lightsStart = line.indexOf('[');
            int lightsEnd = line.indexOf(']');
            int joltagesStart = line.indexOf('{');

            String lightsString = line.substring(lightsStart + 1, lightsEnd);
            boolean[] lights = new boolean[lightsString.length()];

            for (int i = 0; i < lightsString.length(); i++) {
                lights[i] = lightsString.charAt(i) == LIGHT_ON;
            }

            String[] buttonStringArr = line.substring(lightsEnd + 3, joltagesStart - 2).split("\\) \\(");
            ButtonPart1[] buttons = new ButtonPart1[buttonStringArr.length];

            for (int i = 0; i < buttonStringArr.length; i++) {
                int[] lightsAffected = Arrays.stream(buttonStringArr[i].split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                buttons[i] = new ButtonPart1(lightsAffected, lightsString.length());
            }

            machines.add(new MachinePart1(lightsString, buttons));
        }

        return machines;
    }

    private int getMinPressesPart1(MachinePart1 machine) {
        HashSet<Integer> seen = new HashSet<Integer>();
        ArrayDeque<Integer> toCheck = new ArrayDeque<Integer>();

        toCheck.offer(0);
        int presses = 0;

        while (!toCheck.isEmpty()) {
            int nextGroupSize = toCheck.size();
            presses++;
            for (int i = 0; i < nextGroupSize; i++) {
                int lights = toCheck.poll();
                if (seen.contains(lights)) {
                    continue;
                }
                seen.add(lights);
                for (ButtonPart1 button : machine.buttons) {
                    int updatedLights = lights ^ button.lightsAffected;
                    if (updatedLights == machine.indicatorLights) {
                        return presses;
                    }
                    toCheck.offer(updatedLights);
                }
            }
        }

        return -1;
    }

    private class MachinePart1 {
        protected int indicatorLights;
        protected ButtonPart1[] buttons;
        private int numLights;

        public MachinePart1(String indicatorLights, ButtonPart1[] buttons) {
            this.numLights = indicatorLights.length();
            this.buttons = buttons;

            indicatorLights = indicatorLights.replace(".", "0").replace("#", "1");
            this.indicatorLights = Integer.parseInt(indicatorLights, 2);
        }

        @Override
        public String toString() {
            String str = Integer.toBinaryString(indicatorLights);
            while (str.length() < numLights) {
                str = "0" + str;
            }
            str = String.format("Lights: %s\tButtons: ", str);
            for (ButtonPart1 button : buttons) {
                str += String.format("(%s) ", button.toString());
            }
            return str;
        }
    }

    private class ButtonPart1 {
        protected int lightsAffected;
        private int numLights;

        public ButtonPart1(int[] lightsAffected, int numLights) {
            this.numLights = numLights;

            String affectedStr = "";
            int index = 0;
            for (int lightIndex : lightsAffected) {
                while (index < lightIndex) {
                    affectedStr += "0";
                    index++;
                }
                affectedStr += "1";
                index++;
            }

            while (index < numLights) {
                affectedStr += "0";
                index++;
            }

            this.lightsAffected = Integer.parseInt(affectedStr, 2);
        }

        @Override
        public String toString() {
            String str = Integer.toBinaryString(lightsAffected);
            while (str.length() < numLights) {
                str = "0" + str;
            }
            return str;
        }
    }

    @Override
    public String part2(List<String> input) {
        ArrayList<MachinePart2> machines = parseInputPart2(input);

        int result = 0;

        int machineNum = 0;
        for (MachinePart2 machine : machines) {
            int presses = getMinPressesPart2(machine);
            if (presses < 0) {
                return "The following machine couldn't be configured with the buttons available:\n"
                        + machine.toString();
            }
            result += presses;
            machineNum++;
            System.out.println("Got for machine " + machineNum);
        }

        return String.valueOf(result);
    }

    private ArrayList<MachinePart2> parseInputPart2(List<String> input) {
        ArrayList<MachinePart2> machines = new ArrayList<MachinePart2>();

        for (String line : input) {
            int lightsEnd = line.indexOf(']');
            int joltagesStart = line.indexOf('{');
            int joltagesEnd = line.indexOf('}');

            String joltagesString = line.substring(joltagesStart + 1, joltagesEnd);
            int[] joltages = Arrays.stream(joltagesString.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            String[] buttonStringArr = line.substring(lightsEnd + 3, joltagesStart - 2).split("\\) \\(");
            ButtonPart2[] buttons = new ButtonPart2[buttonStringArr.length];

            for (int i = 0; i < buttonStringArr.length; i++) {
                int[] joltagesAffected = Arrays.stream(buttonStringArr[i].split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                buttons[i] = new ButtonPart2(joltagesAffected);
            }

            machines.add(new MachinePart2(joltages, buttons));
        }

        return machines;
    }

    private int getMinPressesPart2(MachinePart2 machine) {
        return -1;
    }

    private class MachinePart2 {
        protected int[] joltages;
        protected ButtonPart2[] buttons;

        public MachinePart2(int[] joltages, ButtonPart2[] buttons) {
            this.joltages = joltages;
            this.buttons = buttons;
        }

        @Override
        public String toString() {
            String str = String.format("Joltages: %s\tButtons: ", Arrays.toString(joltages));
            for (ButtonPart2 button : buttons) {
                str += button.toString() + " ";
            }
            return str;
        }
    }

    private class ButtonPart2 {
        protected int[] joltagesAffected;

        public ButtonPart2(int[] joltagesAffected) {
            this.joltagesAffected = joltagesAffected;
        }

        @Override
        public String toString() {
            String str = Arrays.toString(joltagesAffected);
            str = str.substring(1, str.length() - 1);
            return "(" + str + ")";
        }
    }
}