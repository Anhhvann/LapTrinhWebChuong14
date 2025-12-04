package murach.email;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import murach.business.User;
import murach.data.UserDB;
import murach.util.MailUtilSendGrid;

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

            // Get form data
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");

            User user = new User(firstName, lastName, email);
            UserDB.insert(user);

            request.setAttribute("user", user);

            // Email info
            String to = email;
            String subject = "Welcome to our email list";
            String body =
                    "Dear " + firstName + ",\n\n" +
                            "Thanks for joining our email list.\n" +
                            "We'll send you updates about new products.\n\n" +
                            "Best regards.";

            boolean isBodyHTML = false;

            // Send email via SendGrid
            try {
                MailUtilSendGrid.sendMail(to, subject, body, isBodyHTML);
            }
            catch (Exception e) {   // <-- SendGrid dùng IOException + RuntimeException

                String errorMessage =
                        "ERROR: Unable to send email.<br>" +
                                "ERROR MESSAGE: " + e.getMessage();

                request.setAttribute("errorMessage", errorMessage);

                this.log(
                        "Unable to send email.\n" +
                                "Details:\n" +
                                "TO: " + to + "\n" +
                                "SUBJECT: " + subject + "\n" +
                                "BODY:\n" + body + "\n" +
                                "ERROR: " + e.getMessage()
                );
            }

            url = "/thanks.jsp";
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }
}
