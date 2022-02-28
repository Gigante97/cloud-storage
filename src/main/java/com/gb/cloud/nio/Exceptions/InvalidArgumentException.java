package com.gb.cloud.nio.Exceptions;

public class InvalidArgumentException extends Exception{
    String arg;

    public InvalidArgumentException(String arg) {
        this.arg = arg;
    }
    public String helpMsg(){
        return "ls : " + "'" + arg + "'" + " - " + "No such option\n" +
        "command : ls [option] [extension] [path]\n" +
                "For more help type  : ls --help ";
    }
}
