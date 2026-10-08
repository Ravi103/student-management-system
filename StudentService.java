import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService {
    public Optional<Student> findStudent(long id) throws IOException {
        return loadStudents().stream()
                .filter(student -> student.id() == id)
                .findFirst();
    }

    public void addStudent(Student student) throws IOException {
        ArrayList<Student> students = loadStudents();
        long nextId = students.stream()
                .mapToLong(Student::id)
                .max()
                .orElse(0) + 1;
        Student studentWithId = new Student(student.name(), student.age(), student.dateOfBirth(), student.mail(),
                student.contact(), student.address(), nextId, student.grade());
        FileHandler.appendToFile("[" + studentWithId + "]");
    }

    public boolean updateStudent(long id, Student updatedStudent) throws IOException {
        ArrayList<Student> students = loadStudents();
        for (int index = 0; index < students.size(); index++) {
            Student existingStudent = students.get(index);
            if (existingStudent.id() == id) {
                students.set(index, new Student(existingStudent.name(), existingStudent.age(),
                        existingStudent.dateOfBirth(), updatedStudent.mail(), updatedStudent.contact(),
                        updatedStudent.address(), id, updatedStudent.grade()));
                saveStudents(students);
                return true;
            }
        }
        return false;
    }

    public boolean deleteStudent(long id) throws IOException {
        ArrayList<Student> students = loadStudents();
        boolean removed = students.removeIf(student -> student.id() == id);
        if (removed) {
            saveStudents(students);
        }
        return removed;
    }

    private ArrayList<Student> loadStudents() throws IOException {
        String json = FileHandler.readFromFile();
        if (json.isBlank()) {
            return new ArrayList<>();
        }
        return new StudentJsonParser(json).parse();
    }

    private void saveStudents(ArrayList<Student> students) throws IOException {
        String json = students.stream()
                .map(Student::toString)
                .collect(Collectors.joining(",\n", "[\n", "\n]"));
        FileHandler.overwriteFile(json);
    }
}