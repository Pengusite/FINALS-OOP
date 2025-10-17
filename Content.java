/**
 * Superclass for content items.
 */
public abstract class Content {
    private int contentIdentifierID;
    private String subjectContent;
    private String statusContent; // "uncompleted", "active", "completed"

    public Content(int contentIdentifierID, String subjectContent, String statusContent) {
        this.contentIdentifierID = contentIdentifierID;
        this.subjectContent = subjectContent;
        this.statusContent = statusContent;
    }

    public int getContentIdentifierID() {
        return contentIdentifierID;
    }

    public String getSubjectContent() {
        return subjectContent;
    }

    public String getStatusContent() {
        return statusContent;
    }

    public void setStatusContent(String statusContent) {
        this.statusContent = statusContent;
    }

    public abstract void loadContent();

    public void displaySummary() {
        System.out.println("Content ID: " + contentIdentifierID + ", Subject: " + subjectContent + ", Status: " + statusContent);
    }
}
