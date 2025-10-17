public class LessonTutoring extends Content {
    private String topicLesson;
    private String lessonContent; // full textual content

    public LessonTutoring(int contentIdentifierID, String subjectContent, String statusContent, String topicLesson, String lessonContent) {
        super(contentIdentifierID, subjectContent, statusContent);
        this.topicLesson = topicLesson;
        this.lessonContent = lessonContent;
    }

    public String getTopicLesson() {
        return topicLesson;
    }

    public String getLessonContent() {
        return lessonContent;
    }

    public void deliverLesson() {
        System.out.println("\n=== Delivering Lesson ===");
        System.out.println("Topic: " + topicLesson);
        System.out.println("Subject: " + getSubjectContent());
        System.out.println("----- Content -----");
        System.out.println(lessonContent);
        setStatusContent("active");
        System.out.println("Status: " + getStatusContent());
    }

    @Override
    public void loadContent() {
        System.out.println("Loading lesson content for: " + topicLesson + " (" + getSubjectContent() + ")");
    }

    public String toCSV() {
        // id,subject,status,topic,content (commas replaced with semicolons)
        return getContentIdentifierID() + "," + getSubjectContent() + "," + getStatusContent() + "," + topicLesson.replace(",", ";") + "," + lessonContent.replace(",", ";");
    }

    // parse with limit to keep content intact
    public static LessonTutoring fromCSV(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length < 5) return null;
        try {
            int id = Integer.parseInt(parts[0].trim());
            String subject = parts[1].trim();
            String status = parts[2].trim();
            String topic = parts[3].trim();
            String content = parts[4].trim();
            return new LessonTutoring(id, subject, status, topic, content);
        } catch (Exception e) {
            return null;
        }
    }
}
