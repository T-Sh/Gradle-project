package ru.itmo.cousre_work.Entities;

import javax.persistence.*;

@Entity
@Table(name = "cost", schema = "public")
public class CostOfEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private byte cost;

    @Column
    private String name;

    public CostOfEvent() {
    }

    public CostOfEvent(byte cost, String name) {
        this.cost = cost;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getCost() {
        return cost;
    }

    public void setCost(byte cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
