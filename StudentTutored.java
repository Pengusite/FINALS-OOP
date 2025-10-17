public class StudentTutored extends User {
    private int progressLevelOfUser; // total modules completed
    private double averageScoreOutOf10; // average correct answers (0-10)
    private String learningStyleOfUser;
    private String feedbackOfUser;
    private Integer assignedTutorID; // nullable
    private int assessmentsTaken;

    public StudentTutored(int userTutoredID, String nameUser, String emailUser, String learningStyleOfUser) {
        super(userTutoredID, nameUser, "STUDENT", emailUser);
        this.learningStyleOfUser = learningStyleOfUser;
        this.progressLevelOfUser = 0;
        this.averageScoreOutOf10 = 0.0;
        this.feedbackOfUser = "";
        this.assignedTutorID = null;
        this.assessmentsTaken = 0;
    }

    public int getProgressLevelOfUser() {
        return progressLevelOfUser;
    }

    public double getAverageScoreOutOf10() {
        return averageScoreOutOf10;
    }

    public String getLearningStyleOfUser() {
        return learningStyleOfUser;
    }

    public String getFeedbackOfUser() {
        return feedbackOfUser;
    }

    public Integer getAssignedTutorID() {
        return assignedTutorID;
    }

    public void setAssignedTutorID(Integer assignedTutorID) {
        this.assignedTutorID = assignedTutorID;
    }

    public int getAssessmentsTaken() { return assessmentsTaken; }

    /**
     * Update student record after taking an assessment.
     * newScore is number of correct answers (0..10)
     */
    public void takeAssessment(int newScore) {
        if (newScore < 0 || newScore > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10.");
        }
        // update rolling average
        double total = this.averageScoreOutOf10 * this.assessmentsTaken;
        total += newScore;
        this.assessmentsTaken++;
        this.averageScoreOutOf10 = total / this.assessmentsTaken;
        this.feedbackOfUser = giveFeedback();
    }

    public void completeModule() {
        this.progressLevelOfUser++;
    }

    /**
     * Evaluate overall performance based on average correct answers out of 10.
     */
    public String evaluatePerformance() {
        if (assessmentsTaken == 0) return "No Assessments Taken";
        double avg = this.averageScoreOutOf10;
        if (avg >= 9) return "Excellent";
        if (avg >= 7) return "Good";
        if (avg >= 5) return "Average";
        return "Needs Improvement";
    }

    /**
     * Provide rule-based feedback string.
     */
    public String giveFeedback() {
        String perf = evaluatePerformance();
        switch (perf) {
            case "Excellent":
                return "Outstanding work! Keep it up and try advanced modules.";
            case "Good":
                return "Good job! Review weaker areas to achieve excellence.";
            case "Average":
                return "You're doing okay. Spend more time practicing the lessons.";
            case "Needs Improvement":
                return "You should review the lessons and seek help from your tutor.";
            default:
                return "No assessments yet. Try a post-module quiz!";
        }
    }

    public void displayProgress(int totalModules) {
        int completed = progressLevelOfUser;
        int total = Math.max(1, totalModules); // avoid divide by zero
        int percent = (int) Math.round((completed * 100.0) / total);
        int bars = percent / 10; // 10-char bar
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < bars; i++) bar.append("#");
        for (int i = bars; i < 10; i++) bar.append("-");
        bar.append("] ");
        System.out.println("Progress: " + bar.toString() + " " + percent + "% (" + completed + "/" + total + " modules)");
        System.out.println("Average score (out of 10): " + String.format("%.2f", averageScoreOutOf10) + " | Assessments taken: " + assessmentsTaken);
        System.out.println("Overall Performance: " + evaluatePerformance());
        System.out.println("Feedback: " + feedbackOfUser);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Learning Style: " + learningStyleOfUser);
        if (assignedTutorID != null) System.out.println("Assigned Tutor ID: " + assignedTutorID);
        // totalModules must be provided by the caller when showing progress; not stored here.
    }

    // CSV export: id,name,email,learningStyle,progress,avgScore,assessmentsTaken,assignedTutorId
    public String toCSV() {
        return getUserTutoredID() + "," + getNameUser().replace(",", " ") + "," + getEmailUser().replace(",", " ")
                + "," + learningStyleOfUser.replace(",", ";") + "," + progressLevelOfUser + "," + averageScoreOutOf10
                + "," + assessmentsTaken + "," + (assignedTutorID == null ? "" : assignedTutorID);
    }

    // Expected order: id,name,email,learningStyle,progress,avgScore,assessmentsTaken,assignedTutorId
    public static StudentTutored fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 8);
        if (parts.length < 7) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            String learningStyle = parts[3].trim();
            int progress = Integer.parseInt(parts[4].trim());
            double avg = Double.parseDouble(parts[5].trim());
            int taken = Integer.parseInt(parts[6].trim());
            StudentTutored s = new StudentTutored(id, name, email, learningStyle);
            s.progressLevelOfUser = progress;
            s.averageScoreOutOf10 = avg;
            s.assessmentsTaken = taken;
            if (parts.length >= 8 && !parts[7].trim().isEmpty()) {
                s.assignedTutorID = Integer.parseInt(parts[7].trim());
            }
            s.feedbackOfUser = s.giveFeedback();
            return s;
        } catch (Exception e) {
            return null;
        }
    }
}
