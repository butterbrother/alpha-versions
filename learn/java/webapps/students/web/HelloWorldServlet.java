package students.web;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		// Это простой пример
		/*resp.setContentType("text/html;charset=utf-8");

		PrintWriter pw = resp.getWriter();
		pw.println("<H1>Hello, world! или привет мир</H1>");
		*/

		// Обработка jsp
		getServletContext().getRequestDispatcher("/hello.jsp").forward(req, resp);
	}
}
