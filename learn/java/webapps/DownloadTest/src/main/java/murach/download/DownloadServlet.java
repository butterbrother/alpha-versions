package murach.download;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import murach.beans.User;
import org.jetbrains.annotations.Nullable;

/**
 * Created by somebody on 19.06.2017.
 */
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "viewAlbums";
        }

        String url = "/index.jsp";
        if (action.equals("checkUser")) {
            url = checkUser(request, response);
        }

        getServletContext().
                getRequestDispatcher(url)
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {

        String action = request.getParameter("action");

        String url = action != null && action.equals("registerUser") ?
                registerUser(request, response) : "/index.jsp";

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    private String checkUser(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        String productCode = request.getParameter("productCode");
        HttpSession session = request.getSession();

        String lock = session.getId().intern();
        synchronized (lock) {

            session.setAttribute("productCode", productCode);
            User user = (User)session.getAttribute("user");

            String url;
            if (user == null) {
                Cookie cookies[] = request.getCookies();

                String emailAddress = getCookieValue(cookies, "emailCookie");

                if (emailAddress == null || emailAddress.isEmpty()) {
                    url = "/register.jsp";
                } else {
                    ServletContext servletContext = getServletContext();
                    String path = servletContext.getRealPath("/WEB-INF/EmailList.txt");
                    user = UserIO.getUser(emailAddress, path);

                    if (user == null) {
                        url = "/register.jsp";
                    } else {
                        session.setAttribute("user", user);
                        url = "/downloadPage.jsp";
                    }
                }
            } else {
                url = "/downloadPage.jsp";
            }


            return url;
        }
    }

    @Nullable
    private String getCookieValue(Cookie cookies[], String searchName) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(searchName))
                return cookie.getValue();
        }

        return null;
    }

    private String registerUser(HttpServletRequest request,
                                HttpServletResponse response) {

        return null;
    }
}
