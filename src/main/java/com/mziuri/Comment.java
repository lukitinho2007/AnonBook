package com.mziuri;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "comment")
    private String comment;

    public Comment(Integer id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public Comment(String comment) {
        this.comment = comment;
    }

    public Comment() {

    }

    public Integer getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}