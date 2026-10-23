
package ex1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/FormServlet", "/Form_Servlet"})
public class firstservlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthDate = request.getParameter("birthDate");
        String sex = request.getParameter("sex");

        addCookie(response, "firstName", firstName);
        addCookie(response, "LastName", lastName);
        addCookie(response, "birthDay", birthDate);
        addCookie(response, "Sex", sex);

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Submitted Information</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>Submitted Information</h2>");
        out.println("<h2>VERSION TEST SEX</h2>");

        out.println("<table border='1' cellpadding='10'>");

        out.println("<tr>");
        out.println("<th>First Name</th>");
        out.println("<th>Last Name</th>");
        out.println("<th>Birth Date</th>");
        out.println("<th>Sex</th>");
        out.println("</tr>");

        out.println("<tr>");
        out.println("<td>" + escapeHtml(firstName) + "</td>");
        out.println("<td>" + escapeHtml(lastName) + "</td>");
        out.println("<td>" + escapeHtml(birthDate) + "</td>");
        out.println("<td>" + escapeHtml(sex) + "</td>");
        out.println("</tr>");

        out.println("</table>");

        out.println("<p><a href='ReadCookie'>Read my cookies</a></p>");
        out.println("<p><a href='FORM.html'>Back to form</a></p>");

        out.println("</body>");
        out.println("</html>");
    }

    private void addCookie(HttpServletResponse response,
                           String name, String value) {

        String encoded = URLEncoder.encode(
                value == null ? "" : value,
                StandardCharsets.UTF_8
        );

        Cookie cookie = new Cookie(name, encoded);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/TP_Servlet");

        response.addCookie(cookie);
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
