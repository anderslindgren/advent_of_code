import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        Locations locations = parseFile(args);
        Locations orderdLocations = locations.sort();
        List<Integer> dists = orderdLocations.getDistances();
        int sum = dists.stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);

        // Part 2
        List<Integer> similarityScores = locations.getSimilarityScores();
        int sum2 = similarityScores.stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum2);
    }

    private static Locations parseFile(String[] args) throws IOException {
        String file = "input.txt";
        if (args.length == 1) {
            file = args[0];
        }
        Path path = FileSystems.getDefault().getPath(file);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            List<Integer> l = new ArrayList<>();
            List<Integer> r = new ArrayList<>();
            String st = reader.readLine();
            while (st != null && !st.isBlank()) {
                String[] strings = st.split(" +");
                l.add(Integer.parseInt(strings[0]));
                r.add(Integer.parseInt(strings[1]));
                st = reader.readLine();
            }
            return new Locations(l, r);
        }
    }

    private record Locations(List<Integer> left, List<Integer> right) {

        public Locations sort() {
            return new Locations(left.stream().sorted().toList(), right.stream().sorted().toList());
        }

        public List<Integer> getDistances() {
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < left.size(); i++) {
                result.add(Math.abs(left.get(i) - right.get(i)));
            }
            return result;
        }

        public List<Integer> getSimilarityScores() {
            List<Integer> result = new ArrayList<>();
            for (Integer integer : left) {
                result.add(integer * getOccurrence(integer));
            }
            return result;
        }

        private int getOccurrence(int i) {
            int occ = 0;
            for (Integer integer : right) {
                if (integer == i) {
                    occ++;
                }
            }
            return occ;
        }
    }
}
