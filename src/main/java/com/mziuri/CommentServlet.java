package com.mziuri;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;



@WebServlet("/comment-handler")
public class CommentServlet extends HttpServlet {
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final ImageManager imageManager = new ImageManager();
    private final JsonManager jsonManager = new JsonManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        int postId = Integer.parseInt(request.getParameter("postId"));

        List<String> imageBase64List = imageManager.encodeImagesToBase64(getServletContext().getRealPath("/images"));

        ObjectNode mergedJsonNode = jsonManager.getPostWithCommentsAndImage(databaseManager, imageBase64List, new GetPostRequest(postId));

        PrintWriter writer = response.getWriter();
        writer.println(convertNodeToString(mergedJsonNode));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        int postId = Integer.parseInt(request.getParameter("postId"));
        String commentText = request.getParameter("comment");

        databaseManager.addComment(postId, commentText);

        List<GetCommentsResponse> commentsResponses = databaseManager.getComments(new GetCommentsRequest(postId));

        PrintWriter writer = response.getWriter();
        writer.println(convertCommentsResponseToJson(commentsResponses));
    }

    private String convertNodeToString(ObjectNode node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(node);
    }

    private String convertCommentsResponseToJson(List<GetCommentsResponse> commentsResponses) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(commentsResponses);
    }
}
