package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

public class CreateForumMessageRequest {
    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public CreateForumMessageRequest(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() { return author; }
    public String getContent() { return content; }
}
