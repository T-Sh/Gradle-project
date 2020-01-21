package ru.itmo.cousre_work.Entities;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "goals", schema = "public")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @Column(name = "author")
    private long author;

    @Column(name = "dateOfCreation")
    private Date dateOfCreation;

    @Column(name = "setTime")
    private Date setTime;

    @Column(name = "dateOfComp")
    private Date dateOfComp;

    @Column(name = "evidence")
    private String evidence;

    public Goal() {
    }

    public Goal(String text, long author, Date setTime) {
        this.text = text;
        this.author = author;
        this.setTime = setTime;
        this.dateOfCreation = new Date();
    }

    public long leftTime() {
        Date current = new Date();
        return (setTime.getTime() - current.getTime()) / (24 * 60 * 60 * 1000) + 1;
    }

    public boolean isDone() {
        return evidence != null && !evidence.isEmpty();
    }

    public void done(String evidence) {
        Date dateOfComp = new Date();
        this.evidence = evidence;
        this.dateOfComp = dateOfComp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) throws Exception {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        this.setTime = ft.parse(setTime);
    }

    public String getDateOfComp() {
        Locale local = new Locale("ru","RU");
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, local);

        return df.format(dateOfComp);
    }

    public void setDateOfComp(Date dateOfComp) {
        this.dateOfComp = dateOfComp;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }
}
