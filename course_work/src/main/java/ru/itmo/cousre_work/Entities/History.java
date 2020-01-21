package ru.itmo.cousre_work.Entities;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "history", schema = "public")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long userId;

    @Column
    private byte coins;

    @Column
    private Date date;

    @Column
    private long eventId;

    public History() {
    }

    public History(long userId, CostOfEvent event) {
        this.userId = userId;
        this.coins = event.getCost();
        this.date = new Date();
        eventId = event.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public byte getCoins() {
        return coins;
    }

    public void setCoins(byte coins) {
        this.coins = coins;
    }

    public String getDate() {
        Locale local = new Locale("ru","RU");
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, local);

        return df.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
