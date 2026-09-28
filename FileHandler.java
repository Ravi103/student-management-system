import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;

public class FileHandler {
    public static void writeToFile(String student, int action) throws IOException {
        System.out.println(student);
        File file = new File("student.json");
        if (!file.exists()) {
            file.createNewFile();
        }

        String content = readFileContent(file);
        String studentRecords = student.trim();
        if (!studentRecords.startsWith("[") || !studentRecords.endsWith("]")) {
            throw new IOException("Invalid student data: expected a JSON array");
        }
        studentRecords = studentRecords.substring(1, studentRecords.length() - 1).trim();
        if (studentRecords.isEmpty()) {
            throw new IOException("Invalid student data: no student record to save");
        }

        String existingRecords = content.trim();
        if (existingRecords.isEmpty()) {
            content = "[\n" + studentRecords + "\n]";
        } else if (existingRecords.startsWith("[") && existingRecords.endsWith("]")) {
            String records = existingRecords.substring(1, existingRecords.length() - 1).trim();
            if (records.isEmpty()) {
                content = "[\n" + studentRecords + "\n]";
            } else {
                content = existingRecords.substring(0, existingRecords.length() - 1)
                        + ",\n" + studentRecords + "\n]";
            }
        } else {
            throw new IOException("Invalid student data file: expected a JSON array");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
            System.out.println("Student data saved successfully!");
            writer.close();
        } catch (IOException e) {
            throw new IOException("Error writing to file: " + e.getMessage());
        }
    }

    public static String readFromFile() throws IOException {
        File file = new File("student.json");
        if (!file.exists()) {
            file.createNewFile();
        }

        return readFileContent(file);
    }

    public static void overwriteFile(String content) throws IOException {
        File file = new File("student.json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            throw new IOException("Error writing to file: " + e.getMessage());
        }
    }

    public static String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new IOException("Error reading from file: " + e.getMessage());
        }
        return content.toString().trim();
    }
}
