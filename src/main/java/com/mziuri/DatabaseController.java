package com.mziuri;

import java.util.List;

public interface DatabaseController {
    List<GetPostResponse> getPosts();

    void addPost(AddPostRequest postRequest);

    Post getPost(GetPostRequest postRequest);

    List<GetCommentsResponse> getComments(GetCommentsRequest getCommentsRequest);

    void addComment(int postId, String comment);
}
