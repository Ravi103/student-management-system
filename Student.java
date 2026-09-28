import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public record Student(String name, int age, String dateOfBirth, String mail, long contact, String address, long id,
        String grade) {
    public Student(String name, int age, String dateOfBirth, String mail, long contact, String address) {
        this(name, age, dateOfBirth, mail, contact, address, 0, "");
    }

    public Student(String name, int age, String dateOfBirth, String mail, long contact, String address, String grade) {
        this(name, age, dateOfBirth, mail, contact, address, 0, grade);
    }

    public Student(String name, int age, String dateOfBirth, String mail, long contact, String address, long id) {
        this(name, age, dateOfBirth, mail, contact, address, id, "");
    }

    @Override
    public String toString() {
        return "{\"id\": " + id + ", \"name\": \"" + name + "\", \"age\": " + age
                + ", \"dateOfBirth\": \"" + dateOfBirth + "\","
                + "  \"mail\": \"" + mail + "\","
                + "  \"contact\": " + contact + ","
                + "  \"address\": \"" + address + "\","
                + "  \"grade\": \"" + grade + "\""
                + "}";
    }

    public static boolean getStudentDetails(long id, int action) {
        try {
            ArrayList<Student> students = getExistingStudents();
            if (students.isEmpty()) {
                System.out.println("No student records found.");
                return false;
            } else {
                ArrayList<Student> filteredStudent = (ArrayList<Student>) students.stream()
                        .filter(student -> student.id == id)
                        .collect(Collectors.toList());
                if (filteredStudent.size() == 0) {
                    System.out.println("No student found with ID " + id + ".");
                    return false;
                }
                students = filteredStudent;
            }

            String[] headers = { "ID", "Name", "Age", "Date of birth", "Email", "Contact", "Address", "Grade" };
            int[] widths = new int[headers.length];
            for (int column = 0; column < headers.length; column++) {
                widths[column] = headers[column].length();
            }

            ArrayList<String[]> rows = new ArrayList<>();
            for (int index = 0; index < students.size(); index++) {
                Student student = students.get(index);
                String[] row = { student.id() == 0 ? "N/A" : Long.toString(student.id()), student.name(),
                        Integer.toString(student.age()),
                        student.dateOfBirth(), student.mail(), Long.toString(student.contact()), student.address(),
                        student.grade() };
                rows.add(row);
                for (int column = 0; column < row.length; column++) {
                    widths[column] = Math.max(widths[column], row[column].length());
                }
            }

            printTableBorder(widths);
            printTableRow(headers, widths);
            printTableBorder(widths);
            for (String[] row : rows) {
                printTableRow(row, widths);
            }
            printTableBorder(widths);
            return true;
        } catch (IOException e) {
            System.out.println("Error reading student details: " + e.getMessage());
            return false;
        }
    }

    private static void printTableRow(String[] cells, int[] widths) {
        for (int column = 0; column < cells.length; column++) {
            System.out.printf("| %-" + widths[column] + "s ", cells[column]);
        }
        System.out.println("|");
    }

    private static void printTableBorder(int[] widths) {
        for (int width : widths) {
            System.out.print("+" + "-".repeat(width + 2));
        }
        System.out.println("+");
    }

    private static ArrayList<Student> getExistingStudents() throws IOException {
        String json = FileHandler.readFromFile();
        if (json.isBlank()) {
            return new ArrayList<>();
        }
        return new StudentJsonParser(json).parse();
    }

    public static void setStudentDetails(Student student, int action) throws IOException {
        ArrayList<Student> existingStudents = getExistingStudents();
        long nextId = 1;
        for (Student existingStudent : existingStudents) {
            nextId = Math.max(nextId, existingStudent.id() + 1);
        }

        Student studentWithId = new Student(student.name(), student.age(), student.dateOfBirth(), student.mail(),
                student.contact(), student.address(), nextId, student.grade());
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(studentWithId);
        FileHandler.writeToFile(studentList.toString(), action);
    }

    public static boolean updateStudentDetails(long id, Student updatedStudent) throws IOException {
        ArrayList<Student> students = getExistingStudents();
        for (int index = 0; index < students.size(); index++) {
            if (students.get(index).id() == id) {
                Student existingStudent = students.get(index);
                students.set(index, new Student(existingStudent.name(), existingStudent.age(),
                        existingStudent.dateOfBirth(), updatedStudent.mail(), updatedStudent.contact(),
                        updatedStudent.address(), id, updatedStudent.grade()));
                String json = students.stream().map(Student::toString)
                        .collect(Collectors.joining(",\n", "[\n", "\n]"));
                FileHandler.overwriteFile(json);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteStudentDetails(long id) throws IOException {
        ArrayList<Student> students = getExistingStudents();
        boolean removed = students.removeIf(student -> student.id() == id);
        if (!removed) {
            return false;
        }

        String json = students.stream().map(Student::toString)
                .collect(Collectors.joining(",\n", "[\n", "\n]"));
        FileHandler.overwriteFile(json);
        return true;
    }

}