import java.io.File;
import java.util.*;

/**
 * Smart Learning Assistant (SLA) main program.
 * - Loads lessons & assessments from CSV immediately (./data/).
 * - If CSV missing or incomplete, initializes predefined content (2 modules per subject) and saves to ./data/.
 * - Assessments: 10 MCQ items, auto-graded, score = correct out of 10.
 * - Tutors provide predefined, rule-based feedback messages.
 * - Uses arrays for storage, default package, console UI, and progress bars.
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

    private String filePath = "data"; // fixed relative folder

    private final Scanner scanner = new Scanner(System.in);

    // Subjects fixed
    private final List<String> SUBJECTS = Arrays.asList("INTROWEB", "OOP", "DSAL", "ICYBER", "OPSYSFUN");

    // Mapping subject -> number of modules (we ensure 2 each)
    private final int MODULES_PER_SUBJECT = 2;

    public static void main(String[] args) {
        SmartTutoringSystem app = new SmartTutoringSystem();
        app.loadOrInitData();
        app.mainMenu();
    }

    private void loadOrInitData() {
        boolean loaded = loadFromCSV();
        boolean contentOk = (tutorCount >= SUBJECTS.size() && lessonCount >= SUBJECTS.size() * MODULES_PER_SUBJECT && assessmentCount >= SUBJECTS.size());
        if (!loaded || !contentOk) {
            System.out.println("No valid saved data found. Initializing predefined content...");
            initPredefinedContent();
            boolean ok = saveToCSV();
            if (ok) System.out.println("Predefined content saved to ./data/");
            else System.out.println("Warning: failed to save predefined content automatically.");
        } else {
            System.out.println("Data loaded from ./data/");
        }
    }

    private void initPredefinedContent() {
        // reset arrays
        studentCount = 0;
        tutorCount = 0;
        lessonCount = 0;
        assessmentCount = 0;

        // Predefined tutors (one per subject)
        addTutor(new TutorTutoring(1001, "Dr. Alice Smith", "alice@example.com", "OOP", "PhD Computer Science", "Doctorate"));
        addTutor(new TutorTutoring(1002, "Mr. Bob Tan", "bob@example.com", "INTROWEB", "MSc Software Engineering", "Masters"));
        addTutor(new TutorTutoring(1003, "Ms. Carla Reyes", "carla@example.com", "DSAL", "BSc Computer Science", "Bachelors"));
        addTutor(new TutorTutoring(1004, "Engr. Danilo Cruz", "danilo@example.com", "ICYBER", "MSc Information Security", "Masters"));
        addTutor(new TutorTutoring(1005, "Prof. Edwin Lim", "edwin@example.com", "OPSYSFUN", "PhD Computer Engineering", "Doctorate"));

        // Predefined lessons: 2 per subject with actual content
        int lessonId = 2001;
        for (String subj : SUBJECTS) {
            // Module 1
            addLesson(new LessonTutoring(lessonId++, subj, "uncompleted", subj + " - Module 1",
                    "This is the content for " + subj + " Module 1.\nIt covers the basic concepts and examples."));
            // Module 2
            addLesson(new LessonTutoring(lessonId++, subj, "uncompleted", subj + " - Module 2",
                    "This is the content for " + subj + " Module 2.\nIt goes deeper with practice problems."));
        }

        // Predefined assessments: one per subject, each with 10 MCQs
        int assessId = 3001;
        for (String subj : SUBJECTS) {
            AssessmentTutoring a = new AssessmentTutoring(assessId++, subj, "uncompleted");
            // fill 10 simple sample questions - replace with richer content if needed
            for (int i = 0; i < a.getMaxQuestions(); i++) {
                String q = subj + " Question " + (i + 1) + ": What is the correct choice?";
                String[] opts = new String[] {
                        "Option A for " + (i + 1),
                        "Option B for " + (i + 1),
                        "Option C for " + (i + 1),
                        "Option D for " + (i + 1)
                };
                int correct = (i % 4) + 1; // rotate correct answers 1..4
                a.setQuestionAt(i, q, opts, correct);
            }
            addAssessment(a);
        }
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
        int totalModules = SUBJECTS.size() * MODULES_PER_SUBJECT;
        for (int i = 0; i < studentCount; i++) {
            System.out.println("----- Student #" + (i + 1) + " -----");
            studentTutored[i].displayInfo();
            studentTutored[i].displayProgress(totalModules);
        }
    }

    // ---- Tutor operations ----
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

    // ---- Lesson operations ----
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

    // ---- Assessment operations ----
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
            System.out.println("Last Score (if any): " + (assessmentTutoring[i].getLastScore() < 0 ? "N/A" : assessmentTutoring[i].getLastScore() + "/10"));
        }
    }

    public AssessmentTutoring findAssessmentBySubject(String subject) {
        for (int i = 0; i < assessmentCount; i++) {
            if (assessmentTutoring[i].getSubjectContent().equalsIgnoreCase(subject)) return assessmentTutoring[i];
        }
        return null;
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

    // ---- Menu UI ----
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
                        int score = as.administerQuiz(scanner);
                        // update
                        st.takeAssessment(score);
                        // tutor feedback: if assigned tutor exists use that tutor's mapped message; else find subject tutor
                        String tutorMsg = "";
                        if (st.getAssignedTutorID() != null) {
                            TutorTutoring t = findTutorById(st.getAssignedTutorID());
                            if (t != null) tutorMsg = t.provideSupportByScore(score);
                        } else {
                            // find tutor by subject (first one with subjectExpertise matching)
                            TutorTutoring subjTutor = null;
                            for (int i = 0; i < tutorCount; i++) {
                                if (tutorTutoring[i].getSubjectExpertise().equalsIgnoreCase(as.getSubjectContent())) {
                                    subjTutor = tutorTutoring[i];
                                    break;
                                }
                            }
                            if (subjTutor != null) tutorMsg = subjTutor.provideSupportByScore(score);
                        }
                        System.out.println("Assessment recorded. Result: " + as.evaluatePerformance(score));
                        System.out.println("Feedback: " + as.giveFeedback(score));
                        if (!tutorMsg.isEmpty()) {
                            System.out.println("Tutor feedback: " + tutorMsg);
                        }
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
                        // allow student to take quiz and track progress
                        System.out.println("Would you like to take the post-module quiz now? (y/n)");
                        String ans = scanner.nextLine().trim().toLowerCase();
                        if (ans.equals("y")) {
                            // find assessment by subject
                            AssessmentTutoring found = findAssessmentBySubject(l.getSubjectContent());
                            if (found == null) {
                                System.out.println("No assessment for this module's subject.");
                            } else {
                                // Ask if the taker is a registered student
                                System.out.print("Are you a registered student? (y/n): ");
                                String reg = scanner.nextLine().trim().toLowerCase();
                                StudentTutored student = null;
                                if (reg.equals("y")) {
                                    System.out.print("Enter your student ID: ");
                                    try {
                                        int sid = Integer.parseInt(scanner.nextLine().trim());
                                        student = findStudentById(sid);
                                        if (student == null) {
                                            System.out.println("Student not found. Assessment can be taken as guest but progress won't be recorded.");
                                        }
                                    } catch (NumberFormatException ex) {
                                        System.out.println("Invalid ID input; taking as guest.");
                                    }
                                }
                                int score = found.administerQuiz(scanner);
                                System.out.println("Result: " + found.evaluatePerformance(score));
                                System.out.println("Feedback: " + found.giveFeedback(score));
                                // tutor message
                                String tutorMsg = "";
                                if (student != null && student.getAssignedTutorID() != null) {
                                    TutorTutoring t = findTutorById(student.getAssignedTutorID());
                                    if (t != null) tutorMsg = t.provideSupportByScore(score);
                                } else {
                                    TutorTutoring subjTutor = null;
                                    for (int i = 0; i < tutorCount; i++) {
                                        if (tutorTutoring[i].getSubjectExpertise().equalsIgnoreCase(found.getSubjectContent())) {
                                            subjTutor = tutorTutoring[i];
                                            break;
                                        }
                                    }
                                    if (subjTutor != null) tutorMsg = subjTutor.provideSupportByScore(score);
                                }
                                if (!tutorMsg.isEmpty()) System.out.println("Tutor feedback: " + tutorMsg);
                                // update student if registered
                                if (student != null) {
                                    student.takeAssessment(score);
                                    student.completeModule(); // mark module completed when quiz taken
                                    System.out.println("Student progress updated.");
                                }
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
                        System.out.print("Are you a registered student? (y/n): ");
                        String reg = scanner.nextLine().trim().toLowerCase();
                        StudentTutored student = null;
                        if (reg.equals("y")) {
                            System.out.print("Enter your student ID: ");
                            int sid = Integer.parseInt(scanner.nextLine().trim());
                            student = findStudentById(sid);
                            if (student == null) System.out.println("Student not found. Taking as guest.");
                        }
                        int score = a.administerQuiz(scanner);
                        System.out.println("Result: " + a.evaluatePerformance(score));
                        System.out.println("Feedback: " + a.giveFeedback(score));
                        // tutor feedback
                        String tutorMsg = "";
                        if (student != null && student.getAssignedTutorID() != null) {
                            TutorTutoring t = findTutorById(student.getAssignedTutorID());
                            if (t != null) tutorMsg = t.provideSupportByScore(score);
                        } else {
                            TutorTutoring subjTutor = null;
                            for (int i = 0; i < tutorCount; i++) {
                                if (tutorTutoring[i].getSubjectExpertise().equalsIgnoreCase(a.getSubjectContent())) {
                                    subjTutor = tutorTutoring[i];
                                    break;
                                }
                            }
                            if (subjTutor != null) tutorMsg = subjTutor.provideSupportByScore(score);
                        }
                        if (!tutorMsg.isEmpty()) System.out.println("Tutor feedback: " + tutorMsg);
                        if (student != null) {
                            student.takeAssessment(score);
                            System.out.println("Student progress updated.");
                        }
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
}
