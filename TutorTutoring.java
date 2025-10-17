public class TutorTutoring extends User {
    private String subjectExpertise;
    private String qualifications;
    private String educationLevel;

    public TutorTutoring(int userTutoredID, String nameUser, String emailUser, String subjectExpertise, String qualifications, String educationLevel) {
        super(userTutoredID, nameUser, "TUTOR", emailUser);
        this.subjectExpertise = subjectExpertise;
        this.qualifications = qualifications;
        this.educationLevel = educationLevel;
    }

    public String getSubjectExpertise() {
        return subjectExpertise;
    }

    public void setSubjectExpertise(String subjectExpertise) {
        this.subjectExpertise = subjectExpertise;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    /**
     * Assign a student to this tutor. This sets student's assignedTutorID.
     */
    public void assignStudent(StudentTutored studentAssigned) {
        if (studentAssigned == null) {
            System.out.println("No student provided to assign.");
            return;
        }
        studentAssigned.setAssignedTutorID(this.getUserTutoredID());
        System.out.println("Student " + studentAssigned.getNameUser() + " assigned to tutor " + getNameUser());
    }

    /**
     * Provide predefined rule-based feedback based on numeric score (0..10).
     * Messages are pre-defined, not typed by the tutor.
     */
    public String provideSupportByScore(int scoreOutOf10) {
        if (scoreOutOf10 >= 9) {
            return "Excellent performance! Keep going; consider advanced modules.";
        } else if (scoreOutOf10 >= 7) {
            return "Good job. Review a few tougher topics to improve further.";
        } else if (scoreOutOf10 >= 5) {
            return "Satisfactory. Go back to the lessons and practice more.";
        } else {
            return "You need improvement. Please review the module content and contact your tutor for help.";
        }
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Subject Expertise: " + subjectExpertise);
        System.out.println("Qualifications: " + qualifications + ", Education Level: " + educationLevel);
    }

    // CSV helpers: id,name,email,subjectExpertise,qualifications,educationLevel
    public String toCSV() {
        return getUserTutoredID() + "," + getNameUser().replace(",", " ") + "," + getEmailUser().replace(",", " ")
                + "," + subjectExpertise + "," + qualifications.replace(",", " ") + "," + educationLevel;
    }

    public static TutorTutoring fromCSV(String line) {
        String[] parts = line.split(",", 6);
        if (parts.length < 6) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String email = parts[2].trim();
            String subject = parts[3].trim();
            String quals = parts[4].trim();
            String edu = parts[5].trim();
            return new TutorTutoring(id, name, email, subject, quals, edu);
        } catch (Exception e) {
            return null;
        }
    }
}
