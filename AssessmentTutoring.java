public class AssessmentTutoring extends Content {
    private double scoreStudent; // optional: last recorded score for this assessment
    private int maxScore;

    public AssessmentTutoring(int contentIdentifierID, String subjectContent, String statusContent) {
        super(contentIdentifierID, subjectContent, statusContent);
        this.scoreStudent = -1;
        this.maxScore = 100;
    }

    public double getScoreStudent() {
        return scoreStudent;
    }

    public void setScoreStudent(double scoreStudent) {
        this.scoreStudent = scoreStudent;
    }

    @Override
    public void loadContent() {
        System.out.println("Loading assessment for " + getSubjectContent() + " (Assessment ID: " + getContentIdentifierID() + ")");
    }

    public String evaluatePerformance(double score) {
        if (score >= 60) return "Passed";
        return "Needs Improvement";
    }

    public String giveFeedback(double score) {
        if (score >= 90) return "Outstanding. You showed mastery of this module.";
        if (score >= 75) return "Good understanding, practice a few more problems to perfect it.";
        if (score >= 60) return "Satisfactory. Review specific topics you missed.";
        return "Review lessons and ask your tutor for help.";
    }

    public String toCSV() { // id,subject,status,lastScore
        return getContentIdentifierID() + "," + getSubjectContent() + "," + getStatusContent() + "," + (scoreStudent < 0 ? "" : scoreStudent);
    }

    public static AssessmentTutoring fromCSV(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 3) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String subject = parts[1].trim();
            String status = parts[2].trim();
            AssessmentTutoring a = new AssessmentTutoring(id, subject, status);
            if (parts.length >= 4 && !parts[3].trim().isEmpty()) {
                a.scoreStudent = Double.parseDouble(parts[3].trim());
            }
            return a;
        } catch (Exception e) {
            return null;
        }
    }
}
