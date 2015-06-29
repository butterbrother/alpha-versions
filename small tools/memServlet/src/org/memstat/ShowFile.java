package org.memstat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Get memory info
 */
public class ShowFile extends HttpServlet {
    private Runtime currentRuntime = Runtime.getRuntime();

    /*
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            StringBuilder exitValue = new StringBuilder();
            exitValue.append(currentRuntime.maxMemory()).append("\n");
            exitValue.append(currentRuntime.freeMemory()).append("\n");
            exitValue.append((long)(currentRuntime.freeMemory() * 100.0 / currentRuntime.maxMemory()));
            out.println(exitValue);
        }
    }
    */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        String parameter = req.getParameter("parameter");
        session.setAttribute("parameter", parameter);
        resp.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            StringBuilder exitValue = new StringBuilder();
            exitValue.append(currentRuntime.maxMemory()/1024).append("\n");
            exitValue.append(currentRuntime.freeMemory()/1024).append("\n");
            exitValue.append((long)(currentRuntime.freeMemory() * 100.0 / currentRuntime.maxMemory()));
            out.append(exitValue);
        }
    }

}
