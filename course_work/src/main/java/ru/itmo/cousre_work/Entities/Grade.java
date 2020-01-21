package ru.itmo.cousre_work.Entities;

import javax.persistence.*;

@Entity
@Table(name = "grade", schema = "public")
public class Grade {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long user;

    @Column(name = "goal_id")
    private long goal;

    public Grade() {
    }

    public Grade(long id, long user, long goal) {
        this.id = id;
        this.user = user;
        this.goal = goal;
    }

    public long getHistory() {
        return id;
    }

    public void setHistory(long history) {
        this.id = history;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }
}
