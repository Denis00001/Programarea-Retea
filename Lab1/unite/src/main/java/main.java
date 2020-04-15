import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class main {
    final static String[] str = new String[71];
    final static String[] str2 = new String[71];
    public static void main(String[] args) throws Exception {
        InetAddress addr = InetAddress.getByName("www.unite.md");
        Socket socket = new Socket(addr, 80);
        PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // отправляем HTTP запрос на сервер
        out.println("GET / HTTP/1.1");
        out.println("Host: www.unite.md:80");
        out.println("Connection: Close");
        out.println("Accept: image");
        out.println("Accept-Language: en");
        out.println("Content-Length: 8096");
        out.println("Accept-Encoding: compress, gzip");
        out.println();

        // читаем ответ
        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        socket.close();
        String s = sb.toString();
        Pattern pattern = Pattern.compile("([/|.|\\w|%|\\s|-|:|-])+\\.(?:jpg|gif|png)");
        //Pattern pattern = Pattern.compile("((http(s?):)|(images))([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)");
        Matcher matcher = pattern.matcher(s);
        int i=0;
        while (matcher.find()) {
            str[i] = s.substring(matcher.start(), matcher.end());
            Pattern pattern2 = Pattern.compile("^[/]");
            Matcher matcher2 = pattern2.matcher(str[i]);
            str2[i] = matcher2.replaceAll("http://unite.md/");
            System.out.println(str2[i]);
            i++;
        }

        Semaphore sem = new Semaphore(4);
        int k=0;
        int j=0;
        while(k<str2.length) {
            new Thread(new Download(sem,k,j)).start();
            k++;
            j++;
        }
    }

    static class Download implements Runnable {
        Semaphore sem; // семафор
        int k;
        int j;
        // в качестве параметров конструктора передаем семафор
        Download(Semaphore sem,int k,int j) {
            this.sem = sem;
            this.k = k;
            this.j = j;
        }

        public void run() {
            try {
                //Запрашиваем у семафора разрешение на выполнение
                sem.acquire();
                downloadFiles(str2[k], "E:\\Download\\" + "myfile" + j + ".png");
                sem.release();
            } catch (InterruptedException e) {
                System.out.println("Программа сломана");
            }
        }
    }

    public static void downloadFiles(String filename, String strPath)  {
        try {
            Socket socket = new Socket("www.unite.md", 80);
            DataOutputStream bw = new DataOutputStream(socket.getOutputStream());
            bw.writeBytes("GET "+filename+" HTTP/1.1\r\n");
            bw.writeBytes("Host: www.unite.md:80\r\n\r\n");
            bw.flush();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            OutputStream dos = new FileOutputStream(strPath);
            boolean headerEnded = false;

            byte[] bytes = new byte[2048];
            int length;
            while ((length = in.read(bytes)) != -1) {
                // если конец хедера получен, то записываем байты в файл как надо
                if (headerEnded)
                    dos.write(bytes, 0, length);

                // проверяем последние 4 байта, и, если это конец хедера(\r\n\r\n, что в числовом представлении равно 13 10 13 10)
                // то ставим флаг и записываем байты до конца указанной длины массива
                else {
                    for (int i = 0; i < 2045; i++) {
                        if (bytes[i] == 13 && bytes[i + 1] == 10 && bytes[i + 2] == 13 && bytes[i + 3] == 10) {
                            headerEnded = true;
                            dos.write(bytes, i+4 , 2048-i-4);
                            break;
                        }
                    }
                }
            }
            in.close();
            dos.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}