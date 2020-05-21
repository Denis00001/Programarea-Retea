package org;

import java.net.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import net.coobird.thumbnailator.Thumbnails;

public class Server {

    private DatagramSocket socket;
    private MainFrame frame = new MainFrame();

    private Thread receiving = new Thread(){
        public void run(){
            try{
                while(true){
                    receiveImage();
                }
            }
            catch(Exception ioe){
                System.out.println("Excpetion form thread:"+ioe);
            }
        }
    };


    public Server(){
        try{
            socket = new DatagramSocket(8383);
        }
        catch(Exception ioe){
            System.out.println(ioe);
        }
    }


    public void receiveImage(){
        try{
            System.out.println("Receiving image");
            byte[] packetLength = new byte[20];

            DatagramPacket packet = new DatagramPacket(packetLength, packetLength.length);
            socket.receive(packet);

            String maxLength = new String(packetLength, 0, packet.getLength());
            System.out.println("String maxLength:"+ maxLength);
            if(maxLength.contains("Count")){
                String[] buffArray = maxLength.split(" ");
                Integer maxData = Integer.valueOf(buffArray[1]);
                byte[] fullLength = new byte[maxData];
                int byteCounter = 0;
                for (int i = 0; i <= (maxData /1024); i++) {
                    byte[] dataInUnitPacket = new byte[1024];
                    DatagramPacket unitPacket = new DatagramPacket(dataInUnitPacket, dataInUnitPacket.length);
                    socket.receive(unitPacket);
                    for (byte b : dataInUnitPacket) {
                        fullLength[byteCounter++] = b;
                        if (byteCounter == maxData) {
                            break;
                        }
                    }
                }
                System.out.println("All data received");
                InputStream in = new ByteArrayInputStream(fullLength);
                BufferedImage bImageFromConvert = ImageIO.read(in);
                BufferedImage thumbnail = Thumbnails.of(bImageFromConvert).size(1366, 768).asBufferedImage();
                frame.setPanel(toBytes(thumbnail));
            }
            else{
                System.out.println("Something wrong.");
            }
        }
        catch(Exception ioe){
            System.out.println("Exception:"+ioe);
        }
    }


    private byte[] toBytes(BufferedImage image){
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image,"jpeg", out);
            byte[] imageBytes = out.toByteArray();
            return imageBytes;
        }
        catch(Exception ioe){
            System.out.println(ioe);
            return null;
        }
    }


    public static void main(String[] args) {
        Server se = new Server();
        se.receiving.start();
        System.out.println("Object created");
    }

}
