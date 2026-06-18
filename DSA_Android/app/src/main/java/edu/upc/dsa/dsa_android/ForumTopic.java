package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

public class ForumTopic {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("author")
    private String author;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("messageCount")
    private int messageCount;

    public ForumTopic() {}

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getCreatedAt() { return createdAt; }
    public int getMessageCount() { return messageCount; }
}
