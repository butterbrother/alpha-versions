/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murach.download;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import murach.beans.User;

/**
 *
 * @author somebody
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
		HttpServletResponse response) {

		String productCode = request.getParameter("productCode");
		HttpSession session = request.getSession();

		String lock = session.getId().intern();
		synchronized (lock) {

			session.setAttribute("productCode", productCode);
			User user = (User)session.getAttribute("user");

			String url;
			if (user == null) {
				Cookie cookies[] = request.getCookies();
				
			}

			return null;
		}

	}

	private String getCookieValue()

	private String registerUser(HttpServletRequest request,
		HttpServletResponse response) {

		return null;
	}
}
