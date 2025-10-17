public class StudentTutored extends User {
    private int progressLevelOfUser;
    private double scoreOfUser;
    private String learningStyleOfUser;
    private String feedbackOfUser;

    //Constructor
    public StudentTutored(int userTutoredID, String nameUser, String emailUser, String learningStyleOfUser) {
        super(userTutoredID, nameUser, "Student", emailUser);
        this.learningStyleOfUser = learningStyleOfUser;
        this.progressLevelOfUser = 0;
        this.scoreOfUser = 0.0;
        this.feedbackOfUser = "";
    }

    //Assessments here
    public void takeAssessment(double newScoreOfUser) {

    }

    //Evaluating Scores
    public String evaluatePerformance() {
        if (scoreOfUser >= 9) return "Excellent";
        else if (scoreOfUser >= 7) return "Good";
        else if (scoreOfUser >= 5) return "Average";
        return "Needs Improvement";
    }

    //Feedback on scores
    public String giveFeedback() {
        String scoreEval = evaluatePerformance();
        switch (scoreEval) {
            case "Excellent": return "Great Performance! Keep it up.";
            case "Good": return "Good work! Reviewing a few more topics for mastery.";
            case "Average": return "You're making progress! Review topics again to improve.";
            default: return "Consider revisiting fundamentals and ask tutor for help.";
        }
    }

    //Display progress
    public void displayProgress() {
        System.out.println("\nProgress level: " + progressLevelOfUser + "\nAverage Score: " + scoreOfUser);
    }

    //Display student info
    @Override
    public void displayInfo(){
        super.displayInfo();
        System.out.println("Learning Style: " + learningStyleOfUser);
        System.out.println("Performance: " + evaluatePerformance());
        System.out.println("Feedback: " + giveFeedback());
    }
}
