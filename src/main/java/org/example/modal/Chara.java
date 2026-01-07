package org.example.modal;

import jakarta.persistence.*;

@Entity
@Table(
        name = "charas",
        uniqueConstraints = @UniqueConstraint(
            columnNames = {"name"}
        )
)
public class Chara {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int eidolon;
    private int sig;
    private String role;

    private int rarity;
    private String element;
    private String path;
    private String smallImage;
    private String largeImage;

    @Column(length = 1000)
    private String notes;

    protected Chara() {
    }

    public Chara(String name, int eidolon, int sig, String role) {
        this.name = name;
        this.eidolon = eidolon;
        this.sig = sig;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEidolon() {
        return eidolon;
    }

    public void setEidolon(int eidolon) {
        this.eidolon = eidolon;
    }

    public int getSig() {
        return sig;
    }

    public void setSig(int sig) {
        this.sig = sig;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}