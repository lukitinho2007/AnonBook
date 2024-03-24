package com.mziuri;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class DatabaseConnector {
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;
    private final EntityTransaction entityTransaction;

    private final CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Post> criteriaQuery;
    private CriteriaQuery<Comment> commentCriteriaQuery;
    private CriteriaQuery<PostComment> postCommentCriteriaQuery;
    private Root<Post> postRoot;
    private Root<Comment> commentRoot;
    private Root<PostComment> postCommentRoot;

    public DatabaseConnector() {
        entityManagerFactory = Persistence.createEntityManagerFactory("anonbook");
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();

        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Post.class);
        commentCriteriaQuery = criteriaBuilder.createQuery(Comment.class);
        postCommentCriteriaQuery = criteriaBuilder.createQuery(PostComment.class);
        postRoot = criteriaQuery.from(Post.class);
        commentRoot = commentCriteriaQuery.from(Comment.class);
        postCommentRoot = postCommentCriteriaQuery.from(PostComment.class);
    }

    public static DatabaseConnector instance;

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public void initializeCriteria() {
        criteriaQuery = criteriaBuilder.createQuery(Post.class);
        postRoot = criteriaQuery.from(Post.class);
    }

    public void initializeCommentCriteria() {
        commentCriteriaQuery = criteriaBuilder.createQuery(Comment.class);
        commentRoot = commentCriteriaQuery.from(Comment.class);
    }

    public void initializePostCommentCriteria() {
        postCommentCriteriaQuery = criteriaBuilder.createQuery(PostComment.class);
        postCommentRoot = postCommentCriteriaQuery.from(PostComment.class);
    }
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityTransaction getEntityTransaction() {
        return entityTransaction;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    public CriteriaQuery<Post> getCriteriaQuery() {
        return criteriaQuery;
    }

    public CriteriaQuery<Comment> getCommentCriteriaQuery() {
        return commentCriteriaQuery;
    }

    public CriteriaQuery<PostComment> getPostCommentCriteriaQuery() {
        return postCommentCriteriaQuery;
    }

    public Root<Comment> getCommentRoot() {
        return commentRoot;
    }

    public Root<PostComment> getPostCommentRoot() {
        return postCommentRoot;
    }

    public Root<Post> getPostRoot() {
        return postRoot;
    }
}