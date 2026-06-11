package edu.upc.dsa.dsa_android;

import java.util.List;

public class TeamInfoResponse {
    private String team;
    private List<TeamMember> members;

    public TeamInfoResponse() {}

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public List<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMember> members) {
        this.members = members;
    }
}