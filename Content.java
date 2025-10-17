public class Content {
    private int contentIdentifierID;
    private String subjectContent;
    private String statusContent;

    public Content(int contentIdentifierID, String subjectContent, String statusContent) {
        this.contentIdentifierID = contentIdentifierID;
        this.subjectContent = subjectContent;
        this.statusContent = statusContent;
    }

    public void loadContent() {
        System.out.println("Loading general content...");
    }
}
