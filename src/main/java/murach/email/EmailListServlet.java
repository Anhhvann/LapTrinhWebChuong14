package murach.email;

import java.io.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import murach.business.User;
import murach.data.UserDB;
import murach.util.*;

public class EmailListServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "join";
        }

        String url = "/index.jsp";

        if (action.equals("join")) {
            url = "/index.jsp";
        }
        else if (action.equals("add")) {

            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            User user = new User(firstName, lastName, email);
            UserDB.insert(user);
            request.setAttribute("user", user);

            // Gmail SMTP
            String to = email;
            String from = System.getenv("Gmail_User");
            String subject = "Welcome to our email list";
            String body =
                    "Dear " + firstName + ",\n\n" +
                            "Thanks for joining our email list.\n\n" +
                            "Best regards.";

            boolean isBodyHTML = false;

            if (from == null || from.isEmpty()) {
                String errorMessage =
                        "ERROR: Gmail_User environment variable is not set.";
                request.setAttribute("errorMessage", errorMessage);
            } else {
                try {
                    MailUtilGmail.sendMail(to, from, subject, body, isBodyHTML);
                } catch (MessagingException e) {
                    String errorMessage =
                            "ERROR: Unable to send email.<br>" +
                                    "ERROR MESSAGE: " + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                }
            }

            url = "/thanks.jsp";
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
