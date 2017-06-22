package murach.download;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.beans.Product;
import murach.beans.User;
import org.jetbrains.annotations.Nullable;

/**
 * Created by somebody on 19.06.2017.
 */
public class DownloadServlet extends HttpServlet {

    private String emailListPath = null;
    private String emailCookieName = null;

    @Override
    public void init() throws ServletException {
        super.init();

        emailListPath = getInitParameter("emailListFile");
        emailCookieName = getInitParameter("emailCookieName");

        try {
            String productListFile = getServletContext().getRealPath(getInitParameter("productsListFile"));
            ProductIO.initProductList(productListFile);
        } catch (IOException err) {
            throw new ServletException(err);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "viewAlbums";
        }

        String url = "/index.jsp";
        if (action.equals("checkUser")) {
            url = checkUser(request);
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

    private String checkUser(HttpServletRequest request) throws IOException {

        String productCode = request.getParameter("productCode");
        HttpSession session = request.getSession();

        String lock = session.getId().intern();
        synchronized (lock) {

            session.setAttribute("productCode", productCode);
            User user = (User)session.getAttribute("user");

            String url;
            if (user == null) {
                Cookie cookies[] = request.getCookies();

                String emailAddress = getEmailCookieValue(cookies);

                if (emailAddress == null || emailAddress.isEmpty()) {
                    url = "/register.jsp";
                } else {
                    ServletContext servletContext = getServletContext();
                    String path = servletContext.getRealPath(emailListPath);
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
    private String getEmailCookieValue(Cookie cookies[]) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(emailCookieName))
                return cookie.getValue();
        }

        return null;
    }

    private String registerUser(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {

        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        User user = new User(email, firstName, lastName);

        if (user.isFillAllFields()) {

            ServletContext sc = getServletContext();
            String path = sc.getRealPath(emailListPath);

            UserIO.addUser(user, path);

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            Cookie cookie = new Cookie(emailCookieName, user.getEmail());
            cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
            cookie.setPath("/");

            response.addCookie(cookie);

            Product product = ProductIO.search(sc.getAttribute("productCode"));
            if (product == null) product = new Product("test", "Test file", "test.mp3", "MP3");

            request.setAttribute("product", product);

            return "/downloadPage.jsp";
        } else {
            String url = "/register.jsp";
            request.setAttribute("user", user);
            request.setAttribute("message", "You need fill all fields");

            return url;
        }
    }
}
