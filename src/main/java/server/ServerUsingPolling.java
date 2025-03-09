package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ServerUsingPolling {

    public static void main(String[] args) {

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.socket().bind(new InetSocketAddress(4000));
            serverChannel.configureBlocking(false);
            System.out.println("Server is listening on port " + serverChannel.socket().getLocalPort());

            List<SocketChannel> clientChannels = new ArrayList<>();

            while (true) {
                SocketChannel clientChannel = serverChannel.accept();

                if (clientChannel != null) {
                    clientChannel.configureBlocking(false);
                    clientChannels.add(clientChannel);
                    System.out.println("Client %s connected from %s:%d".formatted(
                            clientChannel.getRemoteAddress(),
                            clientChannel.socket().getInetAddress(),
                            clientChannel.socket().getPort()
                    ));
                }

                ByteBuffer buffer = ByteBuffer.allocate(1024);

                for (int idx = 0; idx < clientChannels.size(); idx++) {
                    SocketChannel channel = clientChannels.get(idx);
                    int readBytes = channel.read(buffer);

                    if (readBytes > 0) {
                        buffer.flip();

                        channel.write(ByteBuffer.wrap("Echo from server: ".getBytes()));
                        while (buffer.hasRemaining()) channel.write(buffer);
                        buffer.clear();
                    } else if (readBytes == -1) {
                        System.out.printf("Connection to %s lost%n", channel.socket().getRemoteSocketAddress());
                        channel.close();
                        clientChannels.remove(idx);
                    }
                }
            }
        } catch (IOException exception) {
            System.out.println("Server exception: " + exception.getMessage());
        }
    }
}