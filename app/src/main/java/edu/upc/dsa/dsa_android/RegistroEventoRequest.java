package edu.upc.dsa.dsa_android;

import com.google.gson.annotations.SerializedName;

public class RegistroEventoRequest {

    @SerializedName("userId")
    private int userId;

    @SerializedName("username")
    private String username;

    public RegistroEventoRequest(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
