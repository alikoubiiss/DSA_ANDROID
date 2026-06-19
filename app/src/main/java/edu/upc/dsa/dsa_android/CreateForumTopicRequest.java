package edu.upc.dsa.dsa_android;

public class CreateForumTopicRequest {
    private String title;
    private String description;
    private String author;

    public CreateForumTopicRequest(String title, String description, String author) {
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
}
