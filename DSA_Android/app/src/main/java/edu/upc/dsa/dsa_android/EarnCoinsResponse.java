package edu.upc.dsa.dsa_android;

public class EarnCoinsResponse {
    private String userId;
    private int coinsEarned;
    private int totalCoins;

    public EarnCoinsResponse() {
    }

    public EarnCoinsResponse(String userId, int coinsEarned, int totalCoins) {
        this.userId = userId;
        this.coinsEarned = coinsEarned;
        this.totalCoins = totalCoins;
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

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    // Helper method to match expected field in UnityGameActivity
    public double getNewBalance() {
        return totalCoins;
    }
}
