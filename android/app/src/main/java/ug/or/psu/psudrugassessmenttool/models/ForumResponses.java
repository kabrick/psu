package ug.or.psu.psudrugassessmenttool.models;

public class ForumResponses {
    private String comment;
    private String author;
    private String timestamp;
    private String id;

    public String getComment() {
        return comment;
    }

    public void setComment(String response) {
        this.comment = response;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
