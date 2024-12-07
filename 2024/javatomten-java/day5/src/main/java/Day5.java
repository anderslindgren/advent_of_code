import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day5 {

    public static void main(String[] args) throws IOException {
        Input input = parseFile(args);
        System.out.println(input);
        int count = findCorrectlyOrderUpdates(input);
        System.out.println(count);
        count = fixIncorrectlyOrderUpdates(input);
        System.out.println(count);
    }

    private static Input parseFile(String[] args) throws IOException {
        String file = "input.txt";
        if (args.length == 1) {
            file = args[0];
        }
        Path path = FileSystems.getDefault().getPath(file);
        List<Rule> rules = new ArrayList<>();
        List<Update> updates = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<String> lines = reader.lines().toList();
            int i = 0;
            String s = lines.get(i);
            while (!s.isBlank()) {
                String[] r = s.split("\\|");
                List<Integer> ruleList = Arrays.stream(r)
                                               .map(Integer::parseInt)
                                               .toList();
                rules.add(new Rule(ruleList));
                s = lines.get(i++);
            }
            while (i < lines.size()) {
                s = lines.get(i);
                String[] p = s.split(",");
                List<Integer> pages = Arrays.stream(p)
                                            .map(Integer::parseInt)
                                            .toList();
                updates.add(new Update(pages));
                i++;
            }
            return new Input(rules, updates);
        }
    }

    private static int findCorrectlyOrderUpdates(Input input) {
        return input.updates.stream()
                            .filter(update -> update.correctlyOrderedUpdate(input.rules))
                            .mapToInt(Update::getMiddlePage)
                            .sum();
    }

    private static int fixIncorrectlyOrderUpdates(Input input) {
        final RuleComparator comparator = new RuleComparator(input.rules);
        return input.updates.stream()
                            .filter(update -> !update.correctlyOrderedUpdate(input.rules))
                            .map(update -> update.applyRules(comparator))
                            .mapToInt(Update::getMiddlePage)
                            .sum();
    }

    private record Input(List<Rule> rules, List<Update> updates) {
    }

    private record Rule(int a, int b) {
        public Rule(List<Integer> ruleList) {
            this(ruleList.getFirst(), ruleList.getLast());
        }

    }

    private record Update(List<Integer> pages) {
        public boolean correctlyOrderedUpdate(List<Rule> rules) {
            for (Rule rule : rules) {
                if (hasBothRuleValues(rule)) {
                    if (pagesNotInOrder(rule)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public int getMiddlePage() {
            int middle = pages.size() / 2;
            return pages.get(middle);
        }

        private boolean hasBothRuleValues(Rule rule) {
            return pages.contains(rule.a) && pages.contains(rule.b);
        }

        private boolean pagesNotInOrder(Rule rule) {
            return pages.indexOf(rule.a) > pages.indexOf(rule.b);
        }

        public Update applyRules(RuleComparator comparator) {
            List<Integer> copy = new ArrayList<>(pages);
            copy.sort(comparator);
            return new Update(copy);
        }
    }

    private record RuleComparator(List<Rule> rules) implements Comparator<Integer> {
        @Override
        public int compare(Integer i1, Integer i2) {
            for (Rule rule : rules) {
                if (i1.equals(rule.a) && i2.equals(rule.b)) {
                    return -1;
                } else if (i1.equals(rule.b) && i2.equals(rule.a)) {
                    return 1;
                }
            }
            return 0;
        }
    }
}
