package org.example.modal;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clears")
public class Clear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endgame;
    private double version;
    private int side;

    private int stars;
    private int score;

    @Column(name = "zero_cycle")
    private boolean zeroCycle;

    @Column(length = 1000)
    private String notes;

    @OneToMany(
            mappedBy = "clear",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<PartyMember> members = new ArrayList<>();

    protected Clear() {
    }

    public Clear(String endgame, double version, int side,
                 int stars, int score, boolean zeroCycle, String notes) {

        this.endgame = endgame;
        this.version = version;
        this.side = side;
        this.stars = stars;
        this.score = score;
        this.zeroCycle = zeroCycle;
        this.notes = notes;

    }

    public void addMember(PartyMember member) {
        if (members.size() >= 4) {
            throw new IllegalStateException("A Clear must have exactly 4 members");
        }
        members.add(member);
        member.setClear(this);
    }

    public void removeMember(PartyMember member) {
        members.remove(member);
        member.setClear(null);
    }

    public List<PartyMember> getPartyMembers() {

        return members;

    }

    public void setMembers(List<PartyMember> members) {

        this.members = members;

    }

    public Long getId() {
        return id;
    }

    public String getEndgame() {
        return endgame;
    }
    public void setEndgame(String endgame) {
        this.endgame = endgame;
    }

    public double getVersion() {
        return version;
    }
    public void setVersion(double version) {
        this.version = version;
    }

    public int getSide() {
        return side;
    }
    public void setSide(int side) {
        this.side = side;
    }

    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public boolean isZeroCycle() {
        return zeroCycle;
    }
    public void setZeroCycle(boolean zeroCycle) { this.zeroCycle = zeroCycle;}

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) { this.notes = notes;}

}
