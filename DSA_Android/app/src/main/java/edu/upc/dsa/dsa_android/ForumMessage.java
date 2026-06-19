package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

public class ForumMessage {
    @SerializedName("id")
    private int id;

    @SerializedName("topicId")
    private int topicId;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("createdAt")
    private String createdAt;

    public ForumMessage() {}

    public ForumMessage(int id, int topicId, String author, String content, String createdAt) {
        this.id = id;
        this.topicId = topicId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getTopicId() { return topicId; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
}
