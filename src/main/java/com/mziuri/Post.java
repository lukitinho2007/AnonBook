package com.mziuri;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "posttext")
    private String postText;
    @Column(name = "imgname")
    private String imgName;

    @Column(name = "time")
    private String time;

    public Post() {

    }

    public Post(Integer id, String postText, String imgName, String time) {
        this.id = id;
        this.postText = postText;
        this.imgName = imgName;
        this.time = time;
    }

    public Post(String postText, String imgName, String time) {
        this.postText = postText;
        this.imgName = imgName;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public String getImgName() {
        return imgName;
    }

    public String getPostText() {
        return postText;
    }

    public String getTime() {
        return time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setTime(String time) {
        this.time = time;
    }
}