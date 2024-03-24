package com.mziuri;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;


import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements DatabaseController {
    private final DatabaseConnector databaseConnector = DatabaseConnector.getInstance();

    @Override
    public List<GetPostResponse> getPosts() {
        databaseConnector.initializeCriteria();

        CriteriaQuery<Post> select = databaseConnector.getCriteriaQuery().select(
                databaseConnector.getPostRoot()
        );

        TypedQuery<Post> typedQuery = databaseConnector.getEntityManager().createQuery(select);

        List<Post> posts = typedQuery.getResultList();
        List<GetPostResponse> postResponses = new ArrayList<>();
        posts.forEach(post -> postResponses.add(new GetPostResponse(post.getId(), post.getPostText(), post.getImgName(), post.getTime())));

        return postResponses;
    }

    @Override
    public void addPost(AddPostRequest postRequest) {
        databaseConnector.initializeCriteria();

        try {
            databaseConnector.getEntityTransaction().begin();

            Post post = new Post(postRequest.title(), postRequest.imgName(), postRequest.time());
            databaseConnector.getEntityManager().persist(post);

            databaseConnector.getEntityTransaction().commit();
        } catch (RuntimeException e) {
            if (databaseConnector.getEntityTransaction().isActive()) {
                databaseConnector.getEntityTransaction().rollback();
            }
        }
    }

    @Override
    public Post getPost(GetPostRequest postRequest) {
        databaseConnector.initializeCriteria();

        CriteriaQuery<Post> select = databaseConnector.getCriteriaQuery().select(
                databaseConnector.getPostRoot()
        ).where(databaseConnector.getCriteriaBuilder().equal(databaseConnector.getPostRoot().get("id"), postRequest.postId()));

        TypedQuery<Post> typedQuery = databaseConnector.getEntityManager().createQuery(select);

        return typedQuery.getSingleResult();
    }

    @Override
    public List<GetCommentsResponse> getComments(GetCommentsRequest getCommentsRequest) {
        databaseConnector.initializePostCommentCriteria();

        CriteriaQuery<PostComment> postComments = databaseConnector.getPostCommentCriteriaQuery().select(
                databaseConnector.getPostCommentRoot()
        ).where(databaseConnector.getCriteriaBuilder().equal(databaseConnector.getPostCommentRoot().get("postId"), getCommentsRequest.postId()));

        TypedQuery<PostComment> postCommentTypedQuery = databaseConnector.getEntityManager().createQuery(postComments);

        List<PostComment> postCommentList = postCommentTypedQuery.getResultList();

        databaseConnector.initializeCommentCriteria();

        List<GetCommentsResponse> getCommentsResponses = new ArrayList<>();

        for (PostComment postComment : postCommentList) {
            CriteriaQuery<Comment> comments = databaseConnector.getCommentCriteriaQuery().select(
                    databaseConnector.getCommentRoot()
            ).where(databaseConnector.getCriteriaBuilder().equal(databaseConnector.getCommentRoot().get("id"), postComment.getCommentId()));

            TypedQuery<Comment> commentTypedQuery = databaseConnector.getEntityManager().createQuery(comments);

            Comment comment = commentTypedQuery.getSingleResult();
            getCommentsResponses.add(new GetCommentsResponse(comment.getId(), comment.getComment()));
        }

        return getCommentsResponses;
    }

    @Override
    public void addComment(int postId, String comment) {
        databaseConnector.initializeCommentCriteria();

        try {
            databaseConnector.getEntityTransaction().begin();

            Comment commentObject = new Comment(comment);
            databaseConnector.getEntityManager().merge(commentObject);

            databaseConnector.getEntityTransaction().commit();
        } catch (RuntimeException e) {
            if (databaseConnector.getEntityTransaction().isActive()) {
                databaseConnector.getEntityTransaction().rollback();
            }
        }

        CriteriaQuery<Comment> selectComment = databaseConnector.getCriteriaBuilder().createQuery(Comment.class);
        Root<Comment> commentRoot = selectComment.from(Comment.class);

        selectComment.select(commentRoot).orderBy(databaseConnector.getCriteriaBuilder().desc(commentRoot.get("id")));

        TypedQuery<Comment> selectCommentTypedQuery = databaseConnector.getEntityManager().createQuery(selectComment);
        selectCommentTypedQuery.setMaxResults(1);

        Comment lastComment = selectCommentTypedQuery.getSingleResult();

        databaseConnector.initializePostCommentCriteria();
        try {
            databaseConnector.getEntityTransaction().begin();

            PostComment postComment = new PostComment(postId, lastComment.getId());
            databaseConnector.getEntityManager().persist(postComment);

            databaseConnector.getEntityTransaction().commit();
        } catch (RuntimeException e) {
            if (databaseConnector.getEntityTransaction().isActive()) {
                databaseConnector.getEntityTransaction().rollback();
            }
        }
    }
}
