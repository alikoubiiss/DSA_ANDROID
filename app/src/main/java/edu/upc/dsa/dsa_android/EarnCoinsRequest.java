package edu.upc.dsa.dsa_android;

public class EarnCoinsRequest {
    private String userId;
    private int coinsEarned;

    public EarnCoinsRequest() {
    }

    public EarnCoinsRequest(String userId, int coinsEarned) {
        this.userId = userId;
        this.coinsEarned = coinsEarned;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public void setCoinsEarned(int coinsEarned) {
        this.coinsEarned = coinsEarned;
    }
}
