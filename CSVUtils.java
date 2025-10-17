import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Small CSV utilities for simple read/write. Note: This is not a fully robust CSV parser.
 */
public class CSVUtils {

    public static boolean writeLines(File file, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing CSV file " + file.getAbsolutePath() + ": " + e.getMessage());
            return false;
        }
    }

    public static List<String> readLines(File file) throws IOException {
        ArrayList<String> out = new ArrayList<>();
        if (!file.exists()) return out;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.trim().isEmpty()) continue;
                out.add(l);
            }
        }
        return out;
    }
}
