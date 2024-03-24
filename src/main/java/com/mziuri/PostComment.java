package com.mziuri;

import jakarta.persistence.*;

@Entity
@Table(name = "post_comment")
public class PostComment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "post_id")
    private Integer postId;
    @Column(name = "comment_id")
    private Integer commentId;

    public PostComment(Integer id, Integer postId, Integer commentId) {
        this.id = id;
        this.postId = postId;
        this.commentId = commentId;
    }

    public PostComment(Integer postId, Integer commentId) {
        this.postId = postId;
        this.commentId = commentId;
    }

    public PostComment() {

    }

    public Integer getId() {
        return id;
    }

    public Integer getPostId() {
        return postId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
}
