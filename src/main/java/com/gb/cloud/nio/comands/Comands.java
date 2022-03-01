package com.gb.cloud.nio.comands;

import com.gb.cloud.nio.Exceptions.NotFoundFileOrDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Comands {



    public String getHelp() throws  IOException {
        Path path = Paths.get("/Users/kirill/IdeaProjects/cloud-storage/src/main/java/com/gb/cloud/nio/comands/help.txt");

        byte[] bytes = Files.readAllBytes(path);

        return new String(bytes);

    }

    public String getFilesOfDirectory(Path path) throws IOException {

        return Files.list(path).map(p->p.getFileName().toString())
                .collect(Collectors.joining("\n"))+"\n\r";

    }

    public Path changeDirectory(Path path,String arg) {
       Path nextPath= path.resolve(arg);
        return nextPath.normalize();
    }

    public void createDir(Path path) throws IOException {
        Files.createDirectories(path);
    }

    public String getText(Path path, String arg) throws IOException {
        Path file = path.resolve(arg);
        byte[] bytes = Files.readAllBytes(file);
        return new String(bytes);
    }

    public void createFile(Path path) throws IOException {
        Files.createFile(path);
    }

    public static void main(String[] args) throws IOException {

            ffff();


    }
    public static void ffff() throws IOException {
        Path currentDirs = Paths.get("server");
        currentDirs = Paths.get("hello.txt");
        byte[] bytes = Files.readAllBytes(currentDirs);
        System.out.println(bytes.toString());
    }

}
