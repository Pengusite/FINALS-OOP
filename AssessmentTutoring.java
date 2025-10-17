public class AssessmentTutoring extends Content {
    private double scoreStudent;

    //Constructor
    public AssessmentTutoring(int contentIdentifierID, String subjectContent, String statusContent) {
        super(contentIdentifierID, subjectContent, statusContent);
        this.scoreStudent = 0.0;
    }

    //Evaluation (needs )
    public String evaluatePerformance() {
        if (scoreStudent >= 75) return "Passed";
        if (scoreStudent >= 50) return "Marginal";
        return "Needs Improvement";
    }

    public String giveFeedback() {
        String eval = evaluatePerformance();
        switch (eval) {
            case "Passed": return "Well done. Keep practicing!";
            case "Marginal": return "You passed but consider revising.";
            default: return "Review the lessons and try again.";
        }
    }

    @Override
    public void loadContent() {
        System.out.printf("Loading assessment for %s (ID %d) - Status: %s%n",
                subjectContent, contentIdentifierID, statusContent);
    }

    public void setScoreStudent(double score) {
        this.scoreStudent = score;
    }

    public double getScoreStudent() {
        return scoreStudent;
    }

   
}
