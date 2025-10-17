//Smart Tutoring System (STS) by Group 1
import java.util.*;
import java.io.*;

//Needed to be implemented, CSV progress, Different content to chhoose and this.

public class SmartTutoringSystem {
    private StudentTutored[] students;
    private TutorTutoring[] tutors;
    private LessonTutoring[] lessons;
    private AssessmentTutoring[] assessments;
    private String filePath = "files/";

    static Scanner scan = new Scanner(System.in);
    //Main Program
    public static void main(String[] args) {
    SmartTutoringSystem app = new SmartTutoringSystem();
    app.menuLoop();   
    }
    
    //Menu
    private void menuLoop() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Smart Tutoring System ===");
            System.out.println("1. Add student");
            System.out.println("2. Remove student by ID");
            System.out.println("3. Display all students");
            System.out.println("4. Take assessment (student)");
            System.out.println("5. Add tutor");
            System.out.println("6. Assign tutor to student");
            System.out.println("7. Display lessons");
            System.out.println("8. Save to CSV");
            System.out.println("9. Load from CSV");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            
            switch (c) {
                case "1": addStudentPrompt(); break;
                case "2": removeStudentPrompt(); break;
                case "3": displayAllStudents(); break;
                case "4": takeAssessmentPrompt(); break;
                case "5": addTutorPrompt(); break;
                case "6": assignTutorPrompt(); break;
                case "7": displayLessons(); break;
                case "8": saveAllCSV(); break;
                case "9": loadAllCSV(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye.");
    }

    // Student related
    public void addStudent(StudentTutored student) {
        students.add(student);
        System.out.println("Student added: " + student.nameUser);
    }

    private void addStudentPrompt() {
        try {
            System.out.print("Enter student ID (int): ");
            int id = Integer.parseInt(scan.nextLine().trim());
            System.out.print("Enter full name: ");
            String name = scan.nextLine().trim();
            System.out.print("Enter email: ");
            String email = scan.nextLine().trim();
            System.out.print("Enter learning style: ");
            String style = scan.nextLine().trim();
            addStudent(new StudentTutored(id, name, email, style));
        } catch (Exception e) {
            System.out.println("Error adding student. Input invalid.");
        }
    }
    
}
