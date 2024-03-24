package com.mziuri;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ImageManager {
    public static String extractImageName(final Part imagePart) {
        return Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
    }

    public static void saveImageToFile(String path, Part imagePart, String fileName) throws IOException {
        Path imagePath = Paths.get(path, fileName);
        try (InputStream imageContent = imagePart.getInputStream()) {
            Files.copy(imageContent, imagePath);
        }
    }

    public static List<String> encodeImagesToBase64(String imagePath) throws IOException {
        return Files.list(Paths.get(imagePath))
                .filter(Files::isRegularFile)
                .map(ImageManager::encodeImageToBase64)
                .collect(Collectors.toList());
    }

    public static String extractFileName(Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static void saveImage(String path, Part filePart, String fileName) throws IOException, ServletException {
        InputStream fileContent = null;
        OutputStream outputStream = null;
        try {
            fileContent = filePart.getInputStream();
            outputStream = new FileOutputStream(path + fileName);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            if (fileContent != null) {
                try {
                    fileContent.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String encodeImageToBase64(Path imagePath) {
        try {
            byte[] imageData = Files.readAllBytes(imagePath);
            return Base64.getEncoder().encodeToString(imageData);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
