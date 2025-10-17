public class StudentTutored extends User {
    private int progressLevelOfUser;
    private double scoreOfUser; // average score
    private String learningStyleOfUser;
    private String feedbackOfUser;
    private Integer assignedTutorID; // nullable

    public StudentTutored(int userTutoredID, String nameUser, String emailUser, String learningStyleOfUser) {
        super(userTutoredID, nameUser, "STUDENT", emailUser);
        this.learningStyleOfUser = learningStyleOfUser;
        this.progressLevelOfUser = 0;
        this.scoreOfUser = 0.0;
        this.feedbackOfUser = "";
        this.assignedTutorID = null;
    }

    public int getProgressLevelOfUser() {
        return progressLevelOfUser;
    }

    public double getScoreOfUser() {
        return scoreOfUser;
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

    /**
     * Student takes an assessment and we update average score and progress level.
     */
    public void takeAssessment(double newScoreOfUser) {
        if (newScoreOfUser < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }
        // update average
        double totalScore = this.scoreOfUser * this.progressLevelOfUser;
        totalScore += newScoreOfUser;
        this.progressLevelOfUser++;
        this.scoreOfUser = totalScore / this.progressLevelOfUser;
        this.feedbackOfUser = giveFeedback();
    }

    /**
     * Evaluate overall performance based on average score.
     */
    public String evaluatePerformance() {
        double avg = this.scoreOfUser;
        if (this.progressLevelOfUser == 0) {
            return "No Assessments Taken";
        }
        if (avg >= 90) return "Excellent";
        if (avg >= 75) return "Good";
        if (avg >= 60) return "Average";
        return "Needs Improvement";
    }

    /**
     * Provide feedback based on overall performance.
     */
    public String giveFeedback() {
        String perf = evaluatePerformance();
        switch (perf) {
            case "Excellent":
                return "Great job! Keep challenging yourself with advanced modules.";
            case "Good":
                return "Good work! Review weaker topics to reach excellence.";
            case "Average":
                return "You're doing okay. Spend more time on practice exercises.";
            case "Needs Improvement":
                return "Please review previous lessons and reach out to your tutor for guidance.";
            default:
                return "No assessment data yet. Try a post-module quiz!";
        }
    }

    public void displayProgress() {
        System.out.println("Progress Level: " + progressLevelOfUser + ", Average Score: " + String.format("%.2f", scoreOfUser));
        System.out.println("Overall Performance: " + evaluatePerformance());
        System.out.println("Feedback: " + feedbackOfUser);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Learning Style: " + learningStyleOfUser);
        if (assignedTutorID != null) System.out.println("Assigned Tutor ID: " + assignedTutorID);
        displayProgress();
    }

    // Helper for CSV export
    public String toCSV() {
        return getUserTutoredID() + "," + getNameUser().replace(",", " ") + "," + getEmailUser().replace(",", " ")
                + "," + learningStyleOfUser + "," + progressLevelOfUser + "," + scoreOfUser + "," + (assignedTutorID == null ? "" : assignedTutorID);
    }

    // Helper parse expected order: id,name,email,learningStyle,progress,avgScore,assignedTutorId
    public static StudentTutored fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 6) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            String learningStyle = parts[3].trim();
            int progress = Integer.parseInt(parts[4].trim());
            double avg = Double.parseDouble(parts[5].trim());
            StudentTutored s = new StudentTutored(id, name, email, learningStyle);
            s.progressLevelOfUser = progress;
            s.scoreOfUser = avg;
            if (parts.length >= 7 && !parts[6].trim().isEmpty()) {
                s.assignedTutorID = Integer.parseInt(parts[6].trim());
            }
            s.feedbackOfUser = s.giveFeedback();
            return s;
        } catch (Exception e) {
            return null;
        }
    }
}
