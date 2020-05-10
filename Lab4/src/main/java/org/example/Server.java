package org.example;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{

    public static void main(String[] args) throws IOException {

        int port = 8383;

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("Порт занят: " + port);
            System.exit(-1);
        }

        //Socket clientSocket = null;
        for(int j=0;j<2;j++){
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ThreadClientHandler(clientSocket,"Client" + j + ": ")).start();
                System.out.print("New client wants to connect to our server\n");

            } catch (IOException e) {
                System.out.println("Ошибка при подключении к порту: " + port);
                System.exit(-1);
            }
        }
    }

    static class ThreadClientHandler implements Runnable {

        private static Socket ClientSocket;
        private static String Name;

        public ThreadClientHandler(Socket client,String name) {
            ThreadClientHandler.ClientSocket = client;
            ThreadClientHandler.Name= name;
        }

        @Override
        public void run() {

            InputStream in = null;
            try {
                in = ClientSocket.getInputStream();
            } catch (IOException e) {
                System.out.println("Не удалось получить поток ввода.");
                System.exit(-1);
            }

            OutputStream out = null;
            try {
                out = ClientSocket.getOutputStream();
            } catch (IOException e) {
                System.out.println("Не удалось получить поток вывода.");
                System.exit(-1);
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String ln = null;

            try {
                String ln2 = reader.readLine();
                if (ln2.equals("Hello")) {
                    writer.write("HTTP/1.1 200 OK\n");
                    writer.flush();
                    while ((ln = reader.readLine()) != null) {
                        System.out.println(Name + ln);
                        System.out.flush();
                    }
                } else {
                    writer.write("HTTP/1.1 401 NOT AUTHORISED\n");
                    writer.flush();
                    reader.close();
                    writer.close();
                    ClientSocket.close();
                }

            } catch (IOException e) {
                System.out.println("Ошибка при чтении сообщения.");
                System.out.println("Клиент закончил общение с сервером");
            }
        }
    }
}
