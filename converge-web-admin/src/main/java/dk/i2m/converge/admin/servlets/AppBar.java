/*
 * Copyright (C) 2012 Interactive Media Management
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.converge.admin.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that generates the JavaScript representing the application bar in the
 * top of the page for all Converge applications.
 *
 * @author Allan Lykke Christensen
 */
@WebServlet(name = "AppBar", urlPatterns = {"/AppBar.js"})
public class AppBar extends HttpServlet {

    /**
     * Application logger.
     */
    private static final Logger LOG = Logger.getLogger(AppBar.class.getName());
    /**
     * Name of the parameter containing the ID of the application calling the
     * AppBar.
     */
    private static final String PARAM_APP_ID = "appId";

    /**
     * Processes requests for both HTTP {@code GET} and {@code POST} methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appId = request.getParameter(PARAM_APP_ID);

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.log(Level.FINEST, "AppBar invoked by application: {0}", appId);
        }

        if (appId == null) {
            appId = "";
        }

        response.setContentType("text/javascript;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.print("document.write('<div class=\"navbar navbar-fixed-top\">');");
            out.print("document.write('    <div class=\"navbar-inner\">');");
            
            out.print("document.write('        <div class=\"container\">');");
            out.print("document.write('            <a class=\"btn btn-navbar\" data-toggle=\"collapse\" data-target=\".nav-collapse\">');");
            out.print("document.write('                <span class=\"icon-bar\"></span>');");
            out.print("document.write('                <span class=\"icon-bar\"></span>');");
            out.print("document.write('                <span class=\"icon-bar\"></span>');");
            out.print("document.write('            </a>');");
            out.print("document.write('            <a class=\"brand\" href=\"#\">');");
            out.print("document.write('                <img class=\"logo\" src=\"/converge-kernel/resources/images/converge.png\" />');");
            out.print("document.write('            </a>');");
            out.print("document.write('            <div class=\"nav-collapse\">');");
            out.print("document.write('                <ul class=\"nav\">');");

            if (appId.equals("converge-dashboard")) {
                out.print("document.write('                    <li class=\"active\">');");
            } else {
                out.print("document.write('                    <li>');");
            }

            out.print("document.write('                        <a href=\"/converge-dashboard\">Dashboard</a>');");
            out.print("document.write('                    </li>');");

            if (appId.equals("converge-crm")) {
                out.print("document.write('                    <li class=\"active\">');");
            } else {
                out.print("document.write('                    <li>');");
            }

            out.print("document.write('                        <a href=\"/converge-crm\">CRM</a>');");
            out.print("document.write('                    </li>');");

            if (appId.equals("converge-web")) {
                out.print("document.write('                    <li class=\"active\">');");
            } else {
                out.print("document.write('                    <li>');");
            }

            out.print("document.write('                        <a href=\"/converge-kernel\">Administration</a>');");
            out.print("document.write('                    </li>');");
            out.print("document.write('                </ul>');");
            out.print("document.write('                <ul class=\"nav pull-right\">');");
            out.print("document.write('                </ul>');");
            out.print("document.write('           </div>');");
            out.print("document.write('        </div>');");
            out.print("document.write('    </div>');");
            out.print("document.write('</div>');");
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP {@code GET} method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP {@code POST} method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return {@link String} containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet responsible for generating the Converge application bar";
    }
}
