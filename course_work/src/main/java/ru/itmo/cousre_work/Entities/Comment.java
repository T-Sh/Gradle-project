package ru.itmo.cousre_work.Entities;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "comment", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "author")
    private long author;

    @Column(name = "goal")
    private long goal;

    @Column(name = "text", length = 500)
    private String text;

    @Column(name = "dateOfCreation")
    private Date dateOfCreation;

    public Comment() {
    }

    public Comment(long author, long goal, String text) {
        this.author = author;
        this.goal = goal;
        this.text = text;
        this.dateOfCreation = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuthor() {
        return author;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateOfCreation() {
        Locale local = new Locale("ru","RU");
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, local);

        return df.format(dateOfCreation);
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
