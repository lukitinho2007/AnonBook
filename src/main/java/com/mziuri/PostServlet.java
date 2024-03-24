package com.mziuri;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.mziuri.ImageManager.extractFileName;
import static com.mziuri.ImageManager.saveImage;


@WebServlet("/post")
@MultipartConfig
public class PostServlet extends HttpServlet {
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final ImageManager imageManager = new ImageManager();
    private final JsonManager jsonManager = new JsonManager();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        List<String> imageBase64List = imageManager.encodeImagesToBase64(getServletContext().getRealPath("/images"));
        ObjectNode mergedJsonNode = jsonManager.mergePostsAndImages(databaseManager, imageBase64List);

        PrintWriter writer = response.getWriter();
        writer.println(convertNodeToString(mergedJsonNode));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String postTitle = request.getParameter("post_title");
        String fileName = "";

        try {
            Part imagePart = request.getPart("image");
            fileName = extractFileName(imagePart);
            saveImage(getServletContext().getRealPath("/images") + "/", imagePart, fileName);
        } catch (ServletException ignored) {
        }

        Instant now = Instant.now();
        ZonedDateTime dateTime = now.atZone(ZoneId.systemDefault());
        String timestamp = dateTime.toString();

        AddPostRequest newPost = new AddPostRequest(postTitle, fileName, timestamp);
        databaseManager.addPost(newPost);

        List<String> imageBase64List = imageManager.encodeImagesToBase64(getServletContext().getRealPath("/images"));
        ObjectNode mergedJsonNode = jsonManager.mergePostsAndImages(databaseManager, imageBase64List);

        PrintWriter writer = response.getWriter();
        writer.println(convertNodeToString(mergedJsonNode));
    }



    private String convertNodeToString(ObjectNode node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(node);
    }
}