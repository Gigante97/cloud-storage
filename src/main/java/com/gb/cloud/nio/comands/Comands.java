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
        path.resolve("help.txt");
        byte[] bytes = Files.readAllBytes(path);
        String msg = new String(bytes);
        return msg;

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

    public static void main(String[] args) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"));
        System.out.println(path.toAbsolutePath().getFileName().toString());


    }

}
