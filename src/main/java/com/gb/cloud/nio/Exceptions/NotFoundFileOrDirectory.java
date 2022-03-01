package com.gb.cloud.nio.Exceptions;

public class NotFoundFileOrDirectory extends Exception{
    String file;

    public NotFoundFileOrDirectory(String file) {
        this.file = file;
    }
    public String helpMsg(){
        return "ls : Can not access  " + "'" + file + "'" + " - " + "No such option\n" +
                "command : ls [option] [extension] [path]\n" +
                "For more help type  : ls --help ";
    }
}
