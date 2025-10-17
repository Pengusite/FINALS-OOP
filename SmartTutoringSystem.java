import java.io.File;
import java.util.*;

/**
 * Main class & entry point for Smart Learning Assistant (SLA).
 * Uses arrays (not dynamic collections) to satisfy the "array of objects" requirement.
 *
 * Adjustments for predefined content and fixed CSV directory:
 * - filePath is fixed to "data" relative to program folder.
 * - lessons, assessments and tutors are predefined and users cannot add them via menu.
 * - On first run (no CSV files present) predefined content is created and saved.
 */
public class SmartTutoringSystem {

    private static final int MAX_STUDENTS = 200;
    private static final int MAX_TUTORS = 50;
    private static final int MAX_LESSONS = 500;
    private static final int MAX_ASSESSMENTS = 500;

    private StudentTutored[] studentTutored = new StudentTutored[MAX_STUDENTS];
    private TutorTutoring[] tutorTutoring = new TutorTutoring[MAX_TUTORS];
    private LessonTutoring[] lessonTutoring = new LessonTutoring[MAX_LESSONS];
    private AssessmentTutoring[] assessmentTutoring = new AssessmentTutoring[MAX_ASSESSMENTS];

    private int studentCount = 0;
    private int tutorCount = 0;
    private int lessonCount = 0;
    private int assessmentCount = 0;

    // fixed relative data directory where the program and CSV files live
    private String filePath = "data";

    private final Scanner scanner = new Scanner(System.in);

    // Fixed subjects in the scope
    private final List<String> SUBJECTS = Arrays.asList("INTROWEB", "OOP", "DSAL", "ICYBER", "OPSYSFUN");

    public static void main(String[] args) {
        SmartTutoringSystem app = new SmartTutoringSystem();
        app.loadOrInitData(); // auto-load or initialize predefined content
        app.mainMenu();
    }

    /**
     * Attempt to load CSV data. If loading fails or there is no content (tutors/lessons/assessments),
     * initialize predefined content and save it.
     */
    private void loadOrInitData() {
        boolean loaded = loadFromCSV();
        if (!loaded || tutorCount == 0 || lessonCount == 0 || assessmentCount == 0) {
            System.out.println("No saved data found or incomplete data. Initializing predefined content...");
            initPredefinedContent();
            boolean ok = saveToCSV();
            if (ok) System.out.println("Predefined content created and saved to '" + filePath + "'.");
            else System.out.println("Failed to automatically save predefined content, but content is available this session.");
        } else {
            System.out.println("Data loaded from '" + filePath + "'.");
        }
    }

    /**
     * Initialize predefined tutors, lessons and assessments.
     * These are the fixed contents that the user cannot create via the menu.
     */
    private void initPredefinedContent() {
        // clear any existing arrays
        studentCount = 0; // keep students empty on initial load so teacher/tester can add students
        tutorCount = 0;
        lessonCount = 0;
        assessmentCount = 0;

        // Predefined tutors with qualifications and education level
        addTutor(new TutorTutoring(1001, "Dr. Alice Smith", "alice@example.com", "OOP", "PhD in Computer Science", "Doctorate"));
        addTutor(new TutorTutoring(1002, "Mr. Bob Tan", "bob@example.com", "INTROWEB", "MSc Software Engineering", "Masters"));
        addTutor(new TutorTutoring(1003, "Ms. Carla Reyes", "carla@example.com", "DSAL", "BSc Computer Science, Teaching Certificate", "Bachelors"));
        addTutor(new TutorTutoring(1004, "Engr. Danilo Cruz", "danilo@example.com", "ICYBER", "MSc Information Security", "Masters"));
        addTutor(new TutorTutoring(1005, "Prof. Edwin Lim", "edwin@example.com", "OPSYSFUN", "PhD Computer Engineering", "Doctorate"));

        // Predefined lessons (one or more per subject)
        addLesson(new LessonTutoring(2001, "INTROWEB", "uncompleted", "HTML & CSS Fundamentals"));
        addLesson(new LessonTutoring(2002, "INTROWEB", "uncompleted", "JavaScript Basics"));
        addLesson(new LessonTutoring(2101, "OOP", "uncompleted", "Classes and Objects"));
        addLesson(new LessonTutoring(2102, "OOP", "uncompleted", "Inheritance & Polymorphism"));
        addLesson(new LessonTutoring(2201, "DSAL", "uncompleted", "Arrays, Lists and Complexity"));
        addLesson(new LessonTutoring(2202, "DSAL", "uncompleted", "Trees and Graphs Introduction"));
        addLesson(new LessonTutoring(2301, "ICYBER", "uncompleted", "Security Fundamentals"));
        addLesson(new LessonTutoring(2401, "OPSYSFUN", "uncompleted", "Operating System Concepts"));

        // Predefined assessments (post-module or generic per subject)
        addAssessment(new AssessmentTutoring(3001, "INTROWEB", "uncompleted"));
        addAssessment(new AssessmentTutoring(3002, "OOP", "uncompleted"));
        addAssessment(new AssessmentTutoring(3003, "DSAL", "uncompleted"));
        addAssessment(new AssessmentTutoring(3004, "ICYBER", "uncompleted"));
        addAssessment(new AssessmentTutoring(3005, "OPSYSFUN", "uncompleted"));
    }

    // ---- Student operations ----
    public void addStudent(StudentTutored student) {
        if (studentCount >= MAX_STUDENTS) {
            System.out.println("Max students reached.");
            return;
        }
        studentTutored[studentCount++] = student;
    }

    public boolean removeStudent(int studentTutorID) {
        for (int i = 0; i < studentCount; i++) {
            if (studentTutored[i].getUserTutoredID() == studentTutorID) {
                // shift left
                for (int j = i; j < studentCount - 1; j++) studentTutored[j] = studentTutored[j + 1];
                studentTutored[studentCount - 1] = null;
                studentCount--;
                return true;
            }
        }
        return false;
    }

    public void displayAllStudents() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }
        for (int i = 0; i < studentCount; i++) {
            System.out.println("----- Student #" + (i + 1) + " -----");
            studentTutored[i].displayInfo();
        }
    }

    // ---- Tutor operations (predefined, no add via UI) ----
    public void addTutor(TutorTutoring tutor) {
        if (tutorCount >= MAX_TUTORS) {
            System.out.println("Max tutors reached.");
            return;
        }
        tutorTutoring[tutorCount++] = tutor;
    }

    public void displayAllTutors() {
        if (tutorCount == 0) {
            System.out.println("No tutors registered.");
            return;
        }
        for (int i = 0; i < tutorCount; i++) {
            System.out.println("----- Tutor #" + (i + 1) + " -----");
            tutorTutoring[i].displayInfo();
        }
    }

    public TutorTutoring findTutorById(int id) {
        for (int i = 0; i < tutorCount; i++) {
            if (tutorTutoring[i].getUserTutoredID() == id) return tutorTutoring[i];
        }
        return null;
    }

    public StudentTutored findStudentById(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (studentTutored[i].getUserTutoredID() == id) return studentTutored[i];
        }
        return null;
    }

    // ---- Lesson operations (predefined, no add via UI) ----
    public void addLesson(LessonTutoring lesson) {
        if (lessonCount >= MAX_LESSONS) {
            System.out.println("Max lessons reached.");
            return;
        }
        lessonTutoring[lessonCount++] = lesson;
    }

    public void displayAllLessons() {
        if (lessonCount == 0) {
            System.out.println("No lessons available.");
            return;
        }
        for (int i = 0; i < lessonCount; i++) {
            lessonTutoring[i].displaySummary();
            System.out.println("Topic: " + lessonTutoring[i].getTopicLesson());
        }
    }

    public LessonTutoring findLessonById(int id) {
        for (int i = 0; i < lessonCount; i++) {
            if (lessonTutoring[i].getContentIdentifierID() == id) return lessonTutoring[i];
        }
        return null;
    }

    // ---- Assessment operations (predefined, no add via UI) ----
    public void addAssessment(AssessmentTutoring assessment) {
        if (assessmentCount >= MAX_ASSESSMENTS) {
            System.out.println("Max assessments reached.");
            return;
        }
        assessmentTutoring[assessmentCount++] = assessment;
    }

    public void displayAllAssessments() {
        if (assessmentCount == 0) {
            System.out.println("No assessments available.");
            return;
        }
        for (int i = 0; i < assessmentCount; i++) {
            assessmentTutoring[i].displaySummary();
            System.out.println("Last Score (if any): " + (assessmentTutoring[i].getScoreStudent() < 0 ? "N/A" : assessmentTutoring[i].getScoreStudent()));
        }
    }

    public AssessmentTutoring findAssessmentById(int id) {
        for (int i = 0; i < assessmentCount; i++) {
            if (assessmentTutoring[i].getContentIdentifierID() == id) return assessmentTutoring[i];
        }
        return null;
    }

    // ---- Persistence: CSV (fixed directory) ----
    public boolean saveToCSV() {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) dir.mkdirs();

            // Students
            List<String> studLines = new ArrayList<>();
            for (int i = 0; i < studentCount; i++) studLines.add(studentTutored[i].toCSV());
            boolean s1 = CSVUtils.writeLines(new File(dir, "students.csv"), studLines);

            // Tutors
            List<String> tutorLines = new ArrayList<>();
            for (int i = 0; i < tutorCount; i++) tutorLines.add(tutorTutoring[i].toCSV());
            boolean s2 = CSVUtils.writeLines(new File(dir, "tutors.csv"), tutorLines);

            // Lessons
            List<String> lessonLines = new ArrayList<>();
            for (int i = 0; i < lessonCount; i++) lessonLines.add(lessonTutoring[i].toCSV());
            boolean s3 = CSVUtils.writeLines(new File(dir, "lessons.csv"), lessonLines);

            // Assessments
            List<String> asLines = new ArrayList<>();
            for (int i = 0; i < assessmentCount; i++) asLines.add(assessmentTutoring[i].toCSV());
            boolean s4 = CSVUtils.writeLines(new File(dir, "assessments.csv"), asLines);

            return s1 && s2 && s3 && s4;
        } catch (Exception e) {
            System.err.println("Error saving CSVs: " + e.getMessage());
            return false;
        }
    }

    public boolean loadFromCSV() {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                return false;
            }

            // Students
            List<String> studLines = CSVUtils.readLines(new File(dir, "students.csv"));
            studentCount = 0;
            for (String l : studLines) {
                StudentTutored s = StudentTutored.fromCSV(l);
                if (s != null) studentTutored[studentCount++] = s;
            }

            // Tutors
            List<String> tutorLines = CSVUtils.readLines(new File(dir, "tutors.csv"));
            tutorCount = 0;
            for (String l : tutorLines) {
                TutorTutoring t = TutorTutoring.fromCSV(l);
                if (t != null) tutorTutoring[tutorCount++] = t;
            }

            // Lessons
            List<String> lessonLines = CSVUtils.readLines(new File(dir, "lessons.csv"));
            lessonCount = 0;
            for (String l : lessonLines) {
                LessonTutoring le = LessonTutoring.fromCSV(l);
                if (le != null) lessonTutoring[lessonCount++] = le;
            }

            // Assessments
            List<String> asLines = CSVUtils.readLines(new File(dir, "assessments.csv"));
            assessmentCount = 0;
            for (String l : asLines) {
                AssessmentTutoring a = AssessmentTutoring.fromCSV(l);
                if (a != null) assessmentTutoring[assessmentCount++] = a;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error loading CSVs: " + e.getMessage());
            return false;
        }
    }

    // ---- Menu UI (content creation removed) ----
    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Smart Learning Assistant (SLA) ===");
            System.out.println("1. Manage Students");
            System.out.println("2. View Tutors (predefined)");
            System.out.println("3. Lessons & Modules (predefined)");
            System.out.println("4. Assessments (predefined)");
            System.out.println("5. Save to CSV");
            System.out.println("6. Load from CSV");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        studentsMenu();
                        break;
                    case "2":
                        tutorsMenu();
                        break;
                    case "3":
                        lessonsMenu();
                        break;
                    case "4":
                        assessmentsMenu();
                        break;
                    case "5":
                        System.out.println(saveToCSV() ? "Saved successfully." : "Errors while saving.");
                        break;
                    case "6":
                        System.out.println(loadFromCSV() ? "Loaded successfully." : "Errors while loading.");
                        break;
                    case "0":
                        running = false;
                        System.out.println("Goodbye.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void studentsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Students ---");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. List Students");
            System.out.println("4. Assign Tutor to Student");
            System.out.println("5. Student takes an assessment");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1":
                    try {
                        System.out.print("ID (int): ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Full Name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.println("Choose learning style (text/visual/auditory): ");
                        String ls = scanner.nextLine().trim();
                        addStudent(new StudentTutored(id, name, email, ls));
                        System.out.println("Student added.");
                    } catch (Exception e) {
                        System.out.println("Failed to add student: " + e.getMessage());
                    }
                    break;
                case "2":
                    try {
                        System.out.print("Enter student ID to remove: ");
                        int rem = Integer.parseInt(scanner.nextLine().trim());
                        boolean ok = removeStudent(rem);
                        System.out.println(ok ? "Removed." : "Student not found.");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "3":
                    displayAllStudents();
                    break;
                case "4":
                    try {
                        System.out.print("Student ID: ");
                        int sid = Integer.parseInt(scanner.nextLine().trim());
                        StudentTutored s = findStudentById(sid);
                        if (s == null) {
                            System.out.println("Student not found.");
                            break;
                        }
                        System.out.print("Tutor ID: ");
                        int tid = Integer.parseInt(scanner.nextLine().trim());
                        TutorTutoring t = findTutorById(tid);
                        if (t == null) {
                            System.out.println("Tutor not found.");
                            break;
                        }
                        t.assignStudent(s);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "5":
                    try {
                        System.out.print("Student ID: ");
                        int sid2 = Integer.parseInt(scanner.nextLine().trim());
                        StudentTutored st = findStudentById(sid2);
                        if (st == null) { System.out.println("Student not found."); break; }
                        System.out.print("Assessment ID: ");
                        int aid = Integer.parseInt(scanner.nextLine().trim());
                        AssessmentTutoring as = findAssessmentById(aid);
                        if (as == null) { System.out.println("Assessment not found."); break; }
                        System.out.print("Enter score (0-100): ");
                        double sc = Double.parseDouble(scanner.nextLine().trim());
                        as.setScoreStudent(sc);
                        st.takeAssessment(sc);
                        System.out.println("Assessment recorded. Result: " + as.evaluatePerformance(sc));
                        System.out.println("Feedback: " + as.giveFeedback(sc));
                    } catch (Exception e) {
                        System.out.println("Error recording assessment: " + e.getMessage());
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid.");
            }
        }
    }

    private void tutorsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Tutors (predefined) ---");
            System.out.println("1. List Tutors");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1":
                    displayAllTutors();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid.");
            }
        }
    }

    private void lessonsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Lessons & Modules (predefined) ---");
            System.out.println("1. List Lessons");
            System.out.println("2. Open Lesson (deliver + optional quiz)");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1":
                    displayAllLessons();
                    break;
                case "2":
                    try {
                        System.out.print("Lesson ID to open: ");
                        int lid = Integer.parseInt(scanner.nextLine().trim());
                        LessonTutoring l = findLessonById(lid);
                        if (l == null) { System.out.println("Lesson not found."); break; }
                        l.deliverLesson();
                        System.out.println("Would you like to take a post-module quiz now? (y/n)");
                        String ans = scanner.nextLine().trim().toLowerCase();
                        if (ans.equals("y")) {
                            // find a related assessment by subject
                            AssessmentTutoring found = null;
                            for (int i = 0; i < assessmentCount; i++) {
                                if (assessmentTutoring[i].getSubjectContent().equalsIgnoreCase(l.getSubjectContent())) {
                                    found = assessmentTutoring[i];
                                    break;
                                }
                            }
                            if (found == null) {
                                System.out.println("No assessment for this module.");
                            } else {
                                takeAssessmentFlow(found);
                            }
                        }
                        l.setStatusContent("completed");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid.");
            }
        }
    }

    private void assessmentsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Assessments (predefined) ---");
            System.out.println("1. List Assessments");
            System.out.println("2. Take Assessment");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1":
                    displayAllAssessments();
                    break;
                case "2":
                    try {
                        System.out.print("Assessment ID: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        AssessmentTutoring a = findAssessmentById(id);
                        if (a == null) { System.out.println("Assessment not found."); break; }
                        takeAssessmentFlow(a);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid.");
            }
        }
    }

    private void takeAssessmentFlow(AssessmentTutoring assessment) {
        try {
            System.out.print("Are you a registered student? (y/n): ");
            String isReg = scanner.nextLine().trim().toLowerCase();
            StudentTutored student = null;
            if (isReg.equals("y")) {
                System.out.print("Enter your student ID: ");
                int sid = Integer.parseInt(scanner.nextLine().trim());
                student = findStudentById(sid);
                if (student == null) {
                    System.out.println("Student not found. You can take assessment as a guest, but no progress will be recorded.");
                }
            }
            System.out.print("Enter score obtained (0-100): ");
            double score = Double.parseDouble(scanner.nextLine().trim());
            assessment.setScoreStudent(score);
            System.out.println("Assessment Result: " + assessment.evaluatePerformance(score));
            System.out.println("Feedback: " + assessment.giveFeedback(score));
            if (student != null) {
                student.takeAssessment(score);
                System.out.println("Student progress updated.");
            }
        } catch (Exception e) {
            System.out.println("Error during assessment: " + e.getMessage());
        }
    }
}
