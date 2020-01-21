package ru.itmo.cousre_work.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "friendship", schema = "public")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long friendId;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;

    public Friendship() {
    }

    public Friendship(long user, long friend) {
        this.userId = user;
        this.friendId = friend;
        date = new Date();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
