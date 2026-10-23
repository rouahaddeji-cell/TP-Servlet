package ex1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ReadCookie")
public class ReadCookie extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String[] names = {
            "firstName", "LastName", "birthDay", "Sex"
        };

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<title>Read Cookies</title></head><body>");
        out.println("<h2>Cookie Information</h2>");
        out.println("<table border='1' cellpadding='10'>");
        out.println("<tr><th>Cookie</th><th>Value</th></tr>");

        Cookie[] cookies = request.getCookies();

        for (String name : names) {
            String value = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(name)) {
                        value = URLDecoder.decode(
                                cookie.getValue(),
                                StandardCharsets.UTF_8
                        );
                        break;
                    }
                }
            }

            out.println("<tr><td>" + name + "</td><td>"
                    + (value == null
                    ? "Not found"
                    : escapeHtml(value))
                    + "</td></tr>");
        }

        out.println("</table>");
        out.println("<p><a href='FORM.html'>Back to form</a></p>");
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