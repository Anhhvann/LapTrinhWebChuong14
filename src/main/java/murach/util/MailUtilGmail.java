package murach.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailUtilGmail {

    public static void sendMail(
            String to, String from,
            String subject, String body,
            boolean bodyIsHTML) throws MessagingException {

        // 1. Gmail SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 2. Authentication (email + app password)
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("anhyeuem1phutthoi@gmail.com", System.getenv("Gmail_Pass"));
            }
        };

        Session session = Session.getInstance(props, auth);
        session.setDebug(true);

        // 3. Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);

        if (bodyIsHTML) {
            message.setContent(body, "text/html; charset=UTF-8");
        } else {
            message.setText(body);
        }

        // 4. Send email
        Transport.send(message);
    }
}
