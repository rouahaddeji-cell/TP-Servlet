package ex1;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ShowSession")
public class ShowSession extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();

        Integer visits = (Integer) session.getAttribute("visits");

        if (visits == null) {
            visits = 1;
        } else {
            visits++;
        }

        session.setAttribute("visits", visits);

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<title>Birthday Session</title></head><body>");

        String fullName = (String) session.getAttribute("fullName");
        String birthday = (String) session.getAttribute("birthday");

        if (fullName == null || birthday == null) {
            out.println("<h2>Welcome on my site!</h2>");
            out.println("<p>Please enter your information:</p>");

            out.println("<form method='post' action='ShowSession'>");

            out.println("<label>Full name:</label>");
            out.println("<input type='text' name='fullName' required>");
            out.println("<br><br>");

            out.println("<label>Date of birth:</label>");
            out.println("<input type='date' name='birthday' required>");
            out.println("<br><br>");

            out.println("<button type='submit'>Submit</button>");
            out.println("</form>");
        } else {
            LocalDate today = LocalDate.now();
            MonthDay birthMonthDay =
                    MonthDay.from(LocalDate.parse(birthday));

            LocalDate nextBirthday =
                    birthMonthDay.atYear(today.getYear());

            if (nextBirthday.isBefore(today)) {
                nextBirthday = birthMonthDay.atYear(today.getYear() + 1);
            }

            long days = ChronoUnit.DAYS.between(today, nextBirthday);

            out.println("<h2>Welcome back!</h2>");
            out.println("<p>Hi, " + escapeHtml(fullName) + ".</p>");
            out.println("<p>There are " + days
                    + " days to your birthday.</p>");
        }

        out.println("<p>Number of visits: " + visits + "</p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String birthday = request.getParameter("birthday");

        if (fullName == null || fullName.trim().isEmpty()
                || birthday == null || birthday.isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Full name and birthday are required."
            );
            return;
        }

        try {
            LocalDate.parse(birthday);
        } catch (java.time.format.DateTimeParseException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid birthday."
            );
            return;
        }

        HttpSession session = request.getSession();

        session.setAttribute("fullName", fullName.trim());
        session.setAttribute("birthday", birthday);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<title>Information Saved</title></head><body>");
        out.println("<h2>Your information has been saved!</h2>");
        out.println("<p><a href='ShowSession'>Continue</a></p>");
        out.println("</body></html>");
    }

    private String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}