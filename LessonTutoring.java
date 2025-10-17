public class LessonTutoring extends Content{
    private String topicLesson;

    public LessonTutoring(int contentIdentifierID, String subjectContent, String statusContent, String topicLesson) {
        super(contentIdentifierID, subjectContent, statusContent);
        this.topicLesson = topicLesson;
    }

    //Loading
    public void deliverLesson() {
        System.out.println("Lesson: " + topicLesson + " (" + subjectContent + ")");
    }

    //Loading Content
    @Override
    public void loadContent() {
        System.out.println("Loading lesson: " + topicLesson);
    }
}
