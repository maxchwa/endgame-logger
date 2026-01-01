package org.example.modal;

import jakarta.persistence.*;

@Entity
@Table(name = "party_members")
public class PartyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int eidolon;
    private int signature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clear_id", nullable = false)

    private Clear clear;

    protected PartyMember() {
    }

    public PartyMember(String name, int eidolon, int signature) {
        this.name = name;
        this.eidolon = eidolon;
        this.signature = signature;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getEidolon() {
        return eidolon;
    }

    public int getSignature() {
        return signature;
    }

    public Clear getClear() {
        return clear;
    }

    public void setClear(Clear clear) {
        this.clear = clear;
    }

}
