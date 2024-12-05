import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
    private static boolean doAdd = true;

    public static void main(String[] args) throws IOException {
        List<Multiplication> multiplications = parseFile(args);
        long safeCount = multiplications.stream()
                                        .map(Multiplication::multiply)
                                        .mapToInt(i -> i)
                                        .sum();
        System.out.println(safeCount);
        List<Multiplication> multiplications2 = parseFile2(args);

        long safeCount2 = multiplications2.stream()
                                        .map(Multiplication::multiply)
                                        .mapToInt(i -> i)
                                        .sum();
        System.out.println(safeCount2);
    }

    private static List<Multiplication> parseFile(String[] args) throws IOException {

        String file = "input.txt";
        if (args.length == 1) {
            file = args[0];
        }
        Path path = FileSystems.getDefault().getPath(file);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines()
                         .map(Day3::getMultiplications)
                         .flatMap(List::stream)
                         .toList();
        }
    }

    private static List<Multiplication> getMultiplications(String s) {
        final String regex = "mul\\((?<f1>\\d{1,3}),(?<f2>\\d{1,3})\\)";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(s);
        List<Multiplication> result = new ArrayList<>();
        while (matcher.find()) {
            int f1 = Integer.parseInt(matcher.group("f1"));
            int f2 = Integer.parseInt(matcher.group("f2"));
            result.add(new Multiplication(f1, f2));
        }

        return result;
    }

    private static List<Multiplication> parseFile2(String[] args) throws IOException {

        String file = "input.txt";
        if (args.length == 1) {
            file = args[0];
        }
        Path path = FileSystems.getDefault().getPath(file);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines()
                         .map(Day3::getMultiplications2)
                         .flatMap(List::stream)
                         .toList();
        }
    }


    private static List<Multiplication> getMultiplications2(String s) {
        final String regex = "(do\\(\\)|don't\\(\\)|mul\\((?<f1>\\d{1,3}),(?<f2>\\d{1,3})\\))";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(s);
        List<Multiplication> result = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            switch (group) {
                case "do()" -> doAdd = true;
                case "don't()" -> doAdd = false;
                default -> {
                    if (doAdd) {
                        int f1 = Integer.parseInt(matcher.group("f1"));
                        int f2 = Integer.parseInt(matcher.group("f2"));
                        result.add(new Multiplication(f1, f2));
                    }
                }
            }
        }

        return result;
    }

    private record Multiplication(int factor1, int factor2) {
        public int multiply() {
            return factor1 * factor2;
        }
    }
}
