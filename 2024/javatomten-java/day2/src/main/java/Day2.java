import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<Report> reports = parseFile(args);
        long safeCount = reports.stream()
                                .filter(Report::isSafe)
                                .count();
        System.out.println(safeCount);
        long dampedSafeCount = reports.stream()
                                      .filter(report -> !report.isSafe())
                                      .filter(Report::isSafeWithDampener)
                                      .count();
        System.out.println("damped: " + dampedSafeCount);
        System.out.println(safeCount + dampedSafeCount);
    }

    private static List<Report> parseFile(String[] args) throws IOException {
        String file = "input.txt";
        if (args.length == 1) {
            file = args[0];
        }
        Path path = FileSystems.getDefault().getPath(file);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines()
                         .map(s -> Arrays.stream(s.split(" +"))
                                         .map(Integer::parseInt)
                                         .toList())
                         .map(Report::new)
                         .toList();
        }
    }

    private record Report(List<Integer> entries) {
        public boolean isSafe() {
            return isListSafe(entries);
        }

        public boolean isSafeWithDampener() {
            for (int i = 0; i < entries.size(); i++) {
                List<Integer> list = new ArrayList<>(entries);
                list.remove(i);
                if (isListSafe(list)) {
                    System.out.println("Safe: " + list);
                    return true;
                }
            }
            return false;
        }

        private boolean isListSafe(List<Integer> list) {
            boolean increasing = false;
            boolean decreasing = false;
            if (list.size() < 2) {
                return false;
            }
            int i1 = list.get(0);
            int i2 = list.get(1);
            if (deltaToBig(i1, i2)) {
                return false;
            }
            if (i1 < i2) {
                increasing = true;
            } else if (i1 > i2) {
                decreasing = true;
            } else {
                return false;
            }
            for (int j = 1, entriesSize = list.size() - 1; j < entriesSize; j++) {
                i1 = list.get(j);
                i2 = list.get(j + 1);
                if (deltaToBig(i1, i2)) {
                    return false;
                } else if (i1 < i2 && decreasing) {
                    return false;
                } else if ((i1 > i2 && increasing)) {
                    return false;
                } else if (i1 == i2) {
                    return false;
                }
            }
            return true;

        }


        private boolean deltaToBig(int i1, int i2) {
            return Math.abs(i1 - i2) > 3;
        }
    }
}
