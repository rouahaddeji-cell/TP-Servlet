package ex1;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/VisitCounter")
public class VisitCounter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        int count = 1;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("visitCount".equals(cookie.getName())) {
                    try {
                        count = Integer.parseInt(cookie.getValue()) + 1;
                    } catch (NumberFormatException e) {
                        count = 1;
                    }
                    break;
                }
            }
        }

        Cookie visitCookie = new Cookie(
                "visitCount", String.valueOf(count)
        );

        visitCookie.setMaxAge(7 * 24 * 60 * 60);
        visitCookie.setPath("/TP_Servlet");
        response.addCookie(visitCookie);

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<title>Visit Counter</title></head><body>");
        out.println("<h2>Visit Counter</h2>");
        out.println("<p>Number of visits: " + count + "</p>");
        out.println("<p><a href='VisitCounter'>Count another visit</a></p>");
        out.println("<p><a href='FORM.html'>Back to form</a></p>");
        out.println("</body></html>");
    }
}