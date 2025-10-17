public class LessonTutoring extends Content {
    private String topicLesson;

    public LessonTutoring(int contentIdentifierID, String subjectContent, String statusContent, String topicLesson) {
        super(contentIdentifierID, subjectContent, statusContent);
        this.topicLesson = topicLesson;
    }

    public String getTopicLesson() {
        return topicLesson;
    }

    public void deliverLesson() {
        System.out.println("=== Delivering Lesson ===");
        System.out.println("Topic: " + topicLesson);
        System.out.println("Subject: " + getSubjectContent());
        setStatusContent("active");
        System.out.println("Status: " + getStatusContent());
    }

    @Override
    public void loadContent() {
        System.out.println("Loading lesson content for: " + topicLesson + " (" + getSubjectContent() + ")");
    }

    public String toCSV() {
        return getContentIdentifierID() + "," + getSubjectContent() + "," + getStatusContent() + "," + topicLesson.replace(",", " ");
    }

    public static LessonTutoring fromCSV(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 4) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String subject = parts[1].trim();
            String status = parts[2].trim();
            String topic = parts[3].trim();
            return new LessonTutoring(id, subject, status, topic);
        } catch (Exception e) {
            return null;
        }
    }
}
