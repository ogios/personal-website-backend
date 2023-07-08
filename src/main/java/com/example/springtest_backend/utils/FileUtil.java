package com.example.springtest_backend.utils;

import cn.hutool.core.io.FileTypeUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FileUtil {

    public static String IMAGE_PATH =  "/raw/images";
    public static String TEXT_PATH = "raw/text";

    public static Set<String> IMAGE_TYPE = new HashSet<>();
    static {
        IMAGE_TYPE.addAll(List.of("jpg", "png", "gif", "webp", "tiff", "bmp", "ico", "apng", "tif", "avif"));
    }

    public static boolean isImage(MultipartFile file) throws IOException {
        String c = FileTypeUtil.getType(file.getInputStream());
        System.out.println("c = " + c);
        return IMAGE_TYPE.contains(c);
    }

    public static String getImageFilePath(int id, String base, String filename){
        String path = base + IMAGE_PATH;
        String suffix = "";
        try {
            suffix = filename.substring(filename.lastIndexOf("."));
        } catch (Exception e){
            e.printStackTrace();
        }
        String name = String.format("/%d_%s%s", id, UUID.randomUUID(), suffix);
        return path + name;
    }

    public static String getImageRelativePath(String name){
        return IMAGE_PATH + "/" + name;
    }

    public static String getStringFilePath(int id, String base){
        String path = base + TEXT_PATH;
        String suffix = ".txt";
        String name = String.format("/%d_%s%s", id, UUID.randomUUID(), suffix);
        return path + name;
    }
    public static String getStringFilePath(String base, String name){
        return base + TEXT_PATH + "/" + name;
    }

    public static String getStringRelativePath(String name){
        return TEXT_PATH + "/" + name;
    }

//    public static String getImageRelativePath(int id, String filename){
//
//    }

    public static String saveFile(MultipartFile file, String path) throws IOException {
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(file.getInputStream()));
        return _saveFile(inputStream, path);
    }

    public static String saveFile(String string, String path) throws IOException {
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8))));
        return _saveFile(inputStream, path);
    }

    public static String  _saveFile(DataInputStream inputStream, String path) throws IOException {
        File file = new File(path);
        createDirIfNotExist(file.getParent());
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] tmp = new byte[1024];
        int read;
        System.out.println("Writing..." + file.getAbsolutePath());
        while (true){
            read = inputStream.read(tmp);
            if (read == -1){
                break;
            }
            outputStream.write(tmp, 0, read);
            outputStream.flush();
        }
        outputStream.close();
        inputStream.close();
        return file.getName();
    }

    public static void createDirIfNotExist(String path) throws IOException {
        Path paths = Paths.get(path);
        if (!Files.exists(paths)){
            System.out.println("Creating dir: " + path);
            Files.createDirectories(paths);
        }

    }

    public static String getTextByName(String base, String name) throws IOException {
        String path = base + TEXT_PATH + "/" + name;
        File file = new File(path);
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        String str = new String(inputStream.readAllBytes());
        inputStream.close();
        return str;
    }
}