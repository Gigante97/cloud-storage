package com.gb.cloud.nio;

import com.gb.cloud.nio.Exceptions.NotFoundFileOrDirectory;
import com.gb.cloud.nio.comands.Comands;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

public class EchoServerNio {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer buffer;
    public Comands comands;
    private Path currentDir;

    public EchoServerNio() throws IOException, NotFoundFileOrDirectory {
        comands = new Comands();
        currentDir = Paths.get("server");

        buffer = ByteBuffer.allocate(5);
        serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();

        serverSocketChannel.bind(new InetSocketAddress(8189));
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (serverSocketChannel.isOpen()) {
            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey currentKey = iterator.next();
                if(currentKey.isAcceptable()) {
                    handleAccept();
                }
                if (currentKey.isReadable()){
                    handleRead(currentKey);
                }
                iterator.remove();
            }


        }

    }

    private void handleAccept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        String msg = "Welcome in KIR terminal\n" +currentDir +"->";
        socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        System.out.println("Client accepted ...");

    }

    private void handleRead(SelectionKey currentKey) throws IOException, NotFoundFileOrDirectory {
        SocketChannel channel = (SocketChannel) currentKey.channel();
        StringBuilder reader = new StringBuilder();

        while (true) {
            int count = channel.read(buffer);
            if (count ==0) {
                break;
            }
            if (count == -1) {
                channel.close();
                return;
            }
            buffer.flip();
            while (buffer.hasRemaining()){
                reader.append((char)buffer.get());

            }
            buffer.clear();
        }
        if ("--help\r\n".equals(reader.toString())){

            String msg = comands.getHelp()  + "\n"+ currentDir +" ->";
            channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        }
        if ("ls\r\n".equals(reader.toString())){
            String msg = comands.getFilesOfDirectory(currentDir)  + "\n"+ currentDir +" ->";
            channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        }

        if(reader.toString().startsWith("cd")) {
            String arg = reader.delete(0,3)
                                .delete(reader.length()-2,reader.length()).toString();
            if ("...".equals(arg)){
                currentDir = Paths.get("server");
                String msg = currentDir.normalize() + " ->";
                channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            } else if (Files.isDirectory(currentDir.resolve(arg))){

            currentDir=comands.changeDirectory(currentDir, arg).normalize();
            String msg = currentDir.normalize() + " ->";
            channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            } else {
                String msg = "Not found directory\r\n" +currentDir+" ->";
                channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            }
        }
        if(reader.toString().startsWith("cat")){

            String arg = reader.delete(0,4).delete(reader.length()-2,reader.length()).toString();
            if (Files.isDirectory(Paths.get(arg))){
               String msg = "it\'s not\'t file!\r\n->";
                channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            } else {
                try {
                    String msg = comands.getText(currentDir, arg) + "\r\n" + currentDir + " ->";
                    channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                } catch (Exception e) {
                    String msg = "NOT FOUND FILE\r\n" + currentDir + " ->";
                    channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                    e.printStackTrace();
                }
            }
        }

        if (reader.toString().startsWith("mkdir")) {
            String arg = reader.delete(0,6).delete(reader.length()-2,reader.length()).toString();
            Path dir = Paths.get(currentDir.toString(),arg);
            comands.createDir(dir);
            String msg = currentDir.normalize() + " ->";
            channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        }

        if (reader.toString().startsWith("touch")) {
            String arg = reader.delete(0,6).delete(reader.length()-2,reader.length()).toString();
            Path dir = Paths.get(currentDir.toString(),arg);
            comands.createFile(dir);
            String msg = currentDir.normalize() + " ->";
            channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        }

    }

    public static void main(String[] args) throws IOException, NotFoundFileOrDirectory {
        new EchoServerNio();
    }

}
