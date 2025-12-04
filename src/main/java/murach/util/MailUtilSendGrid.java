package murach.util;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;

public class MailUtilSendGrid {

    public static void sendMail(String to,
                                String subject,
                                String body,
                                boolean bodyIsHTML) throws IOException {

        String apiKey = System.getenv("SENDGRID_API_KEY");
        String from = System.getenv("SENDGRID_FROM");

        if (apiKey == null || from == null) {
            throw new IOException("SENDGRID_API_KEY or SENDGRID_FROM is not set.");
        }

        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);

        Content content = bodyIsHTML
                ? new Content("text/html", body)
                : new Content("text/plain", body);

        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        System.out.println("STATUS: " + response.getStatusCode());
        System.out.println("BODY: " + response.getBody());
        System.out.println("HEADERS: " + response.getHeaders());
    }
}
