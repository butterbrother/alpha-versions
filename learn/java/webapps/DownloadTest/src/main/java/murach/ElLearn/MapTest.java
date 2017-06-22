package murach.ElLearn;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by somebody on 22.06.2017.
 */
@WebServlet(name = "MapTest", urlPatterns = "/mapTest.do")
public class MapTest extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        Map<String, String> someMap = new HashMap<>();

        someMap.put("test", "data value");
        someMap.put("another", "Another text");

        request.setAttribute("vals", someMap);

        getServletContext()
                .getRequestDispatcher("/mapTest.jsp")
                .forward(request, response);
    }
}
