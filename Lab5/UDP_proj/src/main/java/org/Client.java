package org;

import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Client {

    public String host;
    public int port;
    private Robot robot;
    private DatagramSocket socket;

    public Thread sending = new Thread() {
        public void run() {
            while (true) {
                try {
                    BufferedImage ss = robot.createScreenCapture(new Rectangle(1366, 768));
                    sendImage(toBytes(ss),host);
                     //sleep(3000);
                } catch (Exception ioe) {
                    System.out.println("Exception from thread:" + ioe);
                }
            }
        }
    };


    Client(String host, int port) {
        try {
            socket = new DatagramSocket();
            robot = new Robot();
            this.host=host;
            this.port=port;
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }


    private byte[] toBytes(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", out);
            byte[] imageBytes = out.toByteArray();
            return imageBytes;
        } catch (Exception ioe) {
            System.out.println(ioe);
            return null;
        }
    }

    public void sendImage(byte[] mainImage, String hostname) throws Exception {

        byte[] image = mainImage;
        System.out.println("Split image");
        String imageLength = "Count " + image.length;
        byte[] maxPacketsBytes = imageLength.getBytes();
        System.out.println("Total data:" + image.length);
        System.out.println("MaxPackets:" + (image.length / 1024));
        System.out.println("After dividing:" + (image.length / 1024) * 1024);

        InetAddress host = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(maxPacketsBytes, maxPacketsBytes.length, host, port);
        socket.send(packet);

        int byteCounter = 0;
        boolean condition = true;
        int i;
        while (condition) {
            byte[] buffer = new byte[1024];
            i = 0;
            while (byteCounter < image.length && i < 1024) {
                buffer[i] = image[byteCounter];
                byteCounter++;
                i++;
            }

            DatagramPacket packetToBeSent = new DatagramPacket(buffer, buffer.length, host, port);
            socket.send(packetToBeSent);

            if (byteCounter == image.length) {
                condition = false;
            }
        }
        System.out.println("Total bytes sent:" + image.length);
    }


 /*   public static void main(String[] args) {
        Client re = new Client();
        System.out.println("Created object");
        re.sending.start);
    }
 */
}
