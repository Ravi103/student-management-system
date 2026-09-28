import java.io.IOException;
import java.util.Scanner;

public class StudentManager {
    static String AppStatus = "";

    public static void main(String[] args) {
        System.out.println("Welcome to Student Management System");
        Scanner scanner = new Scanner(System.in);
        AppStatus = "Running";
        while (AppStatus.equals("Running")) {
            App(args, scanner);
        }
    }

    public static void App(String[] args, Scanner scanner) {
        System.out.println(
                "1. Add Student     2. View Student Details     3. Update Student Details     4. Delete Student     5. Exit");
        System.out.println("Enter your Action (1-5):");

        int option = readInt(scanner, "Enter your Action (1-5): ");
        switch (option) {
            case 1:
                addStudent(scanner, 1);
                break;
            case 2:
                viewStudentDetails(scanner, 2);
                break;
            case 3:
                updateStudent(scanner, 3);
                break;
            case 4:
                deleteStudent(scanner);
                break;
            case 5:
                System.out.println("Thank you!");
                AppStatus = "Stopped";
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

    private static void addStudent(Scanner scanner, int action) {
        System.out.println("Please enter student details to add.");
        Student student = readStudentDetails(scanner, action);
        try {
            Student.setStudentDetails(student, action);
        } catch (IOException e) {
            System.out.println("Error saving student details: " + e.getMessage());
        }
    }

    private static void updateStudent(Scanner scanner, int action) {
        long id = readLong(scanner, "Enter Student ID to update: ");

        boolean isRecordAvailable = Student.getStudentDetails(id, action);
        if (!isRecordAvailable) {
            return;
        }

        System.out.println("Enter the updated student details:");
        Student updatedStudent = readStudentDetails(scanner, action);
        try {
            if (Student.updateStudentDetails(id, updatedStudent)) {
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
            if (Student.deleteStudentDetails(id)) {
                System.out.println("Student record deleted successfully.");
            } else {
                System.out.println("No student found with ID " + id + ".");
            }
        } catch (IOException e) {
            System.out.println("Error deleting student details: " + e.getMessage());
        }
    }

    private static Student readStudentDetails(Scanner scanner, int action) {
        String name = "";
        int age = 0;
        String dateOfBirth = "";

        if (action == 1) {
            System.out.print("Enter student name: ");
            name = scanner.nextLine();
            System.out.print("Enter student age: ");
            age = readInt(scanner, "Enter student age: ");
            System.out.print("Enter student date of birth: ");
            dateOfBirth = scanner.nextLine();
        }
        long contact = readLong(scanner, "Enter student contact: ");
        System.out.print("Enter student email: ");
        String mail = scanner.nextLine();
        System.out.print("Enter student address: ");
        String address = scanner.nextLine();
        System.out.print("Enter student grade: ");
        String grade = scanner.nextLine();
        return new Student(name, age, dateOfBirth, mail, contact, address, grade);
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

    public static void viewStudentDetails(Scanner scanner, int action) {
        System.out.print("Enter Student ID to view details:");
        long id = scanner.nextLong();
        scanner.nextLine();

        boolean isRecordAvailable = Student.getStudentDetails(id, action);
    }
}