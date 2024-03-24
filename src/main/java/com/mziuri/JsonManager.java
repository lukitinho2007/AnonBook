package com.mziuri;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.IOException;
import java.util.List;

public class JsonManager {
    public static ObjectNode mergePostsAndImages(DatabaseManager controller, List<String> imageList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode dataArray = mapper.createArrayNode();
        for (GetPostResponse post : controller.getPosts()) {
            dataArray.add(mapper.readTree(String.valueOf(post)));
        }

        ArrayNode imageArray = mapper.createArrayNode();
        for (String image : imageList) {
            imageArray.add(image);
        }

        ObjectNode mergedJsonNode = mapper.createObjectNode();
        mergedJsonNode.set("data", dataArray);
        mergedJsonNode.set("imageBase64", imageArray);

        return mergedJsonNode;
    }

    public static ObjectNode getPostWithCommentsAndImage(DatabaseManager controller, List<String> imageList, GetPostRequest postRequest) throws IOException {
        ObjectMapper mapper = new ObjectMapper();


        String postDataJson = mapper.writeValueAsString(controller.getPost(postRequest));
        JsonNode postData = mapper.readTree(postDataJson);


        JsonNode commentsData = mapper.readTree((JsonParser) controller.getComments(new GetCommentsRequest(postRequest.postId())));

        ObjectNode mergedJsonNode = mapper.createObjectNode();
        mergedJsonNode.set("data", postData);
        mergedJsonNode.set("comments", commentsData);

        try {
            String imageBase64 = imageList.get(postRequest.postId() - 1);
            mergedJsonNode.put("imageBase64", imageBase64);
        } catch (IndexOutOfBoundsException ignored) {

        }

        return mergedJsonNode;
    }
}
