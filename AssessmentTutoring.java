/**
 * Assessment with 10 MCQ questions. Each assessment holds exactly 10 questions,
 * with 4 options each and an integer correct index (1..4).
 */
public class AssessmentTutoring extends Content {
    private int maxQuestions = 10;
    private String[] questions;           // length 10
    private String[][] options;           // [10][4]
    private int[] correctOptionIndex;     // [10] values 1..4
    private int lastScore = -1;           // last recorded correct answers out of 10

    public AssessmentTutoring(int contentIdentifierID, String subjectContent, String statusContent) {
        super(contentIdentifierID, subjectContent, statusContent);
        questions = new String[maxQuestions];
        options = new String[maxQuestions][4];
        correctOptionIndex = new int[maxQuestions];
        // default placeholder
        for (int i = 0; i < maxQuestions; i++) {
            questions[i] = "Question not set";
            for (int j = 0; j < 4; j++) options[i][j] = "Option " + (j + 1);
            correctOptionIndex[i] = 1;
        }
    }

    public int getMaxQuestions() { return maxQuestions; }

    public int getLastScore() { return lastScore; }

    public void setQuestionAt(int idx, String q, String[] opts, int correctOneBased) {
        if (idx < 0 || idx >= maxQuestions) throw new IllegalArgumentException("Invalid question index");
        questions[idx] = q;
        for (int j = 0; j < 4; j++) options[idx][j] = opts[j];
        if (correctOneBased < 1 || correctOneBased > 4) throw new IllegalArgumentException("correct index must be 1..4");
        correctOptionIndex[idx] = correctOneBased;
    }

    @Override
    public void loadContent() {
        System.out.println("Loading assessment for " + getSubjectContent() + " (Assessment ID: " + getContentIdentifierID() + ")");
    }

    /**
     * Present the quiz in console and auto-grade. Returns correctCount (0..10).
     * Input is validated.
     */
    public int administerQuiz(java.util.Scanner scanner) {
        int correct = 0;
        System.out.println("\n--- Assessment: " + getSubjectContent() + " ---");
        for (int i = 0; i < maxQuestions; i++) {
            System.out.println("\nQ" + (i + 1) + ". " + questions[i]);
            for (int j = 0; j < 4; j++) {
                System.out.println("  " + (j + 1) + ") " + options[i][j]);
            }
            int choice = -1;
            while (true) {
                System.out.print("Your answer (1-4): ");
                String line = scanner.nextLine().trim();
                try {
                    choice = Integer.parseInt(line);
                    if (choice >= 1 && choice <= 4) break;
                } catch (NumberFormatException ignored) {}
                System.out.println("Invalid input, enter a number between 1 and 4.");
            }
            if (choice == correctOptionIndex[i]) correct++;
        }
        this.lastScore = correct;
        System.out.println("\nYour score: " + correct + " out of " + maxQuestions);
        return correct;
    }

    /**
     * Evaluate (simple pass/fail). Passing threshold is 6/10 (60%).
     */
    public String evaluatePerformance(int score) {
        if (score >= 6) return "Passed";
        return "Needs Improvement";
    }

    public String giveFeedback(int score) {
        if (score >= 9) return "Outstanding. You showed mastery of this module.";
        if (score >= 7) return "Good understanding, practice a few more problems to perfect it.";
        if (score >= 6) return "Satisfactory. Review specific topics you missed.";
        return "Review lessons and ask your tutor for help.";
    }

    // Serialize questions to a single CSV-friendly field (commas replaced earlier)
    // Format: q1~~opt1~~opt2~~opt3~~opt4~~correct||q2~~...
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxQuestions; i++) {
            if (i > 0) sb.append("||");
            sb.append(questions[i].replace(",", ";")).append("~~");
            for (int j = 0; j < 4; j++) {
                sb.append(options[i][j].replace(",", ";"));
                sb.append("~~");
            }
            sb.append(correctOptionIndex[i]);
        }
        // id,subject,status,questionsSerialized
        return getContentIdentifierID() + "," + getSubjectContent() + "," + getStatusContent() + "," + sb.toString();
    }

    // Deserialize (split into 4 parts to keep the long questions field intact)
    public static AssessmentTutoring fromCSV(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length < 4) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String subject = parts[1].trim();
            String status = parts[2].trim();
            String qSerialized = parts[3].trim();
            AssessmentTutoring a = new AssessmentTutoring(id, subject, status);
            String[] qParts = qSerialized.split("\\|\\|");
            for (int i = 0; i < Math.min(qParts.length, a.maxQuestions); i++) {
                String[] items = qParts[i].split("~~");
                // items: [question, opt1, opt2, opt3, opt4, correct]
                if (items.length >= 6) {
                    String q = items[0];
                    String[] opts = new String[] { items[1], items[2], items[3], items[4] };
                    int correct = Integer.parseInt(items[5]);
                    a.setQuestionAt(i, q, opts, correct);
                }
            }
            return a;
        } catch (Exception e) {
            return null;
        }
    }
}
