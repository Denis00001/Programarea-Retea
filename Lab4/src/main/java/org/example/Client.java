package org.example;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
public class Client
{
    public static void main(String[] args) {

        String host = "localhost";
        int port = 8383;

        Socket socket = null;
        try {
            socket = new Socket(host, port);
        }
        catch (UnknownHostException e) {
            System.out.println("Неизвестный хост: " + host);
            System.exit(-1);
        }
        catch (IOException e) {
            System.out.println("Ошибка ввода/вывода при создании сокета " + host + ":" + port);
            System.exit(-1);
        }


        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));

        OutputStream out = null;
        try {
            out = socket.getOutputStream();
        }
        catch (IOException e) {
            System.out.println("Не удалось получить поток вывода.");
            System.exit(-1);
        }

        InputStream in = null;
        try {
            in = socket.getInputStream();
        }
        catch (IOException e) {
            System.out.println("Не удалось получить поток ввода.");
            System.exit(-1);
        }

        BufferedReader reader1 = new BufferedReader(new InputStreamReader(in));
        String ln1 = null;

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        String ln = null;
        try {
            if ((ln = reader.readLine()) != null) {
                writer.write(ln + "\n");
                writer.flush();
                if ((ln1 = reader1.readLine()) != null) {
                    System.out.println(ln1);
                    System.out.flush();
                }
            }
                while ((ln = reader.readLine()) != null) {
                    writer.write(ln + "\n");
                    writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи сообщения.");
            System.exit(-1);
        }

    }

}
