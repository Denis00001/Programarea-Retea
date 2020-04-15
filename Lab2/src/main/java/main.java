import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Scanner;

import static java.lang.System.exit;

class Main {

    public static void main(String[] args) throws IOException, MessagingException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Аутентификация\nВведите ваш логин(часть до @gmail.com)");
        String username = scan.nextLine();
        System.out.println("Введите пароль:");
        String password = scan.nextLine();

        while (true){

            System.out.println("\nМеню:\n1-Отправить сообщение\n2-Прочитать сообщение\n0-Выйти из программы\n\nВаш выбор:");
            int number = scan.nextInt();

            switch (number){
                case 1:

                    Scanner sc = new Scanner(System.in);
                    System.out.println("Введите e-mail пользователя, которому будем отправлять сообщение(прим. : example.com)");
                    String to = sc.nextLine();
                    System.out.println("Введите тему сообщения:");
                    String subject = sc.nextLine();
                    System.out.println("Введите текст сообщения:");
                    String body = sc.nextLine();

                    sendEmail(username, password, to, subject, body);

                    break;
                case 2:

                    ReadEmail(username, password);

                    break;
                default:
                    exit (0);
            }
        }
    }

    static void sendEmail(String from, String pass, String to, String subject, String body) {
        // Создание свойств, получение сессии
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", host);

        Session session = Session.getDefaultInstance(props);
        // Создание объекта сообщения
        MimeMessage message = new MimeMessage(session);

        try {
            // Установка атрибутов сообщения
            message.setFrom(new InternetAddress(from));
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setText(body);
            // Отправка сообщения
            // Получение объекта транспорта для передачи электронного сообщения
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }

    static void ReadEmail(String email, String pass) throws MessagingException, IOException {

        String IMAP_Server = "imap.gmail.com";
        String IMAP_Port = "993";
        // Создание свойств
        Properties properties = new Properties();
        properties.put("mail.debug", "false");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", IMAP_Port);

        Session session = Session.getInstance(properties);
        Store store = session.getStore();

        //подключаемся к почтовому серверу
        store.connect(IMAP_Server , email, pass);

        //получаем папку с входящими сообщениями
        Folder inbox = store.getFolder("INBOX");

        //открываем её только для чтения
        inbox.open(Folder.READ_ONLY);

        //получаем количество сообщений на почте
        System.out.println("Сейчас на почте находится: " + inbox.getMessageCount() + " сообщений");
        Scanner scan = new Scanner(System.in);
        System.out.println("Какое сообщение прочитать?");
        int number = scan.nextInt();
        Message m = inbox.getMessage(number);
        Object content = m.getContent();
        if (content instanceof String)
        {
            String body = (String)content;
            System.out.println(body);
        }
        else if (content instanceof Multipart)
        {
            Multipart mp = (Multipart)content;
            BodyPart bp = mp.getBodyPart(0);
            System.out.println(bp.getContent());
        }

    }
}