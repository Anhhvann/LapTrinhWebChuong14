package murach.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtilSendGrid {

    public static void sendMail(String to,
                                String subject,
                                String body,
                                boolean bodyIsHTML)
            throws MessagingException {

        String apiKey = System.getenv("SENDGRID_API_KEY");
        String from = System.getenv("SENDGRID_FROM");

        if (apiKey == null || from == null) {
            throw new MessagingException("SENDGRID_API_KEY or SENDGRID_FROM is not set.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("apikey", apiKey);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        if (bodyIsHTML) {
            message.setContent(body, "text/html");
        } else {
            message.setText(body);
        }

        Transport.send(message);
    }
}
