import java.io.IOException;
import java.util.Scanner;
import java.util.Optional;

public class StudentManager {
    private static final StudentService STUDENT_SERVICE = new StudentService();
    private static boolean running;

    public static void main(String[] args) {
        System.out.println("Welcome to Student Management System");
        Scanner scanner = new Scanner(System.in);
        running = true;
        while (running) {
            App(scanner);
        }
    }

    private static void App(Scanner scanner) {
        System.out.println(
                "1. Add Student     2. View Student Details     3. Update Student Details     4. Delete Student     5. Exit");
        System.out.println("Enter your Action (1-5):");

        int option = readInt(scanner, "Enter your Action (1-5): ");
        switch (option) {
            case 1:
                addStudent(scanner);
                break;
            case 2:
                viewStudentDetails(scanner);
                break;
            case 3:
                updateStudent(scanner);
                break;
            case 4:
                deleteStudent(scanner);
                break;
            case 5:
                System.out.println("Thank you!");
                running = false;
                scanner.close();
                break;
            default:
                System.out.println("Invalid option selected");
                break;
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.print(prompt);
            }
        }
    }

    private static void addStudent(Scanner scanner) {
        System.out.println("Please enter student details to add.");
        Student student = readStudentDetails(scanner);
        try {
            STUDENT_SERVICE.addStudent(student);
            System.out.println("Student record added successfully.");
        } catch (IOException e) {
            System.out.println("Error saving student details: " + e.getMessage());
        }
    }

    private static void updateStudent(Scanner scanner) {
        long id = readLong(scanner, "Enter Student ID to update: ");
        Optional<Student> existingStudent;
        try {
            existingStudent = STUDENT_SERVICE.findStudent(id);
        } catch (IOException e) {
            System.out.println("Error reading student details: " + e.getMessage());
            return;
        }
        if (existingStudent.isEmpty()) {
            System.out.println("No student found with ID " + id + ".");
            return;
        }
        printStudentDetails(existingStudent.get());

        System.out.println("Enter the updated student details:");
        Student updatedStudent = readUpdatedStudentDetails(scanner);
        try {
            if (STUDENT_SERVICE.updateStudent(id, updatedStudent)) {
                System.out.println("Student record updated successfully.");
            } else {
                System.out.println("No student found with ID " + id + ".");
            }
        } catch (IOException e) {
            System.out.println("Error updating student details: " + e.getMessage());
        }
    }

    private static void deleteStudent(Scanner scanner) {
        long id = readLong(scanner, "Enter Student ID to delete: ");
        try {
            if (STUDENT_SERVICE.deleteStudent(id)) {
                System.out.println("Student record deleted successfully.");
            } else {
                System.out.println("No student found with ID " + id + ".");
            }
        } catch (IOException e) {
            System.out.println("Error deleting student details: " + e.getMessage());
        }
    }

    private static Student readStudentDetails(Scanner scanner) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = readInt(scanner, "Enter student age: ");
        System.out.print("Enter student date of birth: ");
        String dateOfBirth = scanner.nextLine();
        long contact = readLong(scanner, "Enter student contact: ");
        System.out.print("Enter student email: ");
        String mail = scanner.nextLine();
        System.out.print("Enter student address: ");
        String address = scanner.nextLine();
        System.out.print("Enter student grade: ");
        String grade = scanner.nextLine();
        return new Student(name, age, dateOfBirth, mail, contact, address, grade);
    }

    private static Student readUpdatedStudentDetails(Scanner scanner) {
        long contact = readLong(scanner, "Enter student contact: ");
        System.out.print("Enter student email: ");
        String mail = scanner.nextLine();
        System.out.print("Enter student address: ");
        String address = scanner.nextLine();
        System.out.print("Enter student grade: ");
        String grade = scanner.nextLine();
        return new Student("", 0, "", mail, contact, address, grade);
    }

    private static long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static void viewStudentDetails(Scanner scanner) {
        long id = readLong(scanner, "Enter Student ID to view details: ");
        try {
            Optional<Student> student = STUDENT_SERVICE.findStudent(id);
            if (student.isPresent()) {
                printStudentDetails(student.get());
            } else {
                System.out.println("No student found with ID " + id + ".");
            }
        } catch (IOException e) {
            System.out.println("Error reading student details: " + e.getMessage());
        }
    }

    private static void printStudentDetails(Student student) {
        String[] headers = { "ID", "Name", "Age", "Date of birth", "Email", "Contact", "Address", "Grade" };
        String[] row = { Long.toString(student.id()), student.name(), Integer.toString(student.age()),
                student.dateOfBirth(), student.mail(), Long.toString(student.contact()), student.address(),
                student.grade() };
        int[] widths = new int[headers.length];
        for (int column = 0; column < headers.length; column++) {
            widths[column] = Math.max(headers[column].length(), row[column].length());
        }

        printTableBorder(widths);
        printTableRow(headers, widths);
        printTableBorder(widths);
        printTableRow(row, widths);
        printTableBorder(widths);
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
}