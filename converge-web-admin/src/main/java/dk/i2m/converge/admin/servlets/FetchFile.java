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

import dk.i2m.converge.ejb.services.ConfigurationKey;
import dk.i2m.converge.ejb.services.ConfigurationServiceLocal;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tika.Tika;

/**
 * Servlet for fetching previously stored files on the local file system.
 *
 * @author Allan Lykke Christensen
 */
@WebServlet(name = "FetchFile", urlPatterns = {"/FetchFile"})
public class FetchFile extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(FetchFile.class.getName());

    private static final String PARAM_ID = "id";

    private static final String PARAM_LIB = "lib";

    @EJB
    private ConfigurationServiceLocal cfgService;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = getParameter(request, PARAM_ID, "");
        String lib = getParameter(request, PARAM_LIB, "");
        String dir = cfgService.getString(ConfigurationKey.WORKING_DIRECTORY);

        LOG.log(Level.FINEST, "Processing FetchFile for {0} in {2}/{1}", new Object[]{id, lib, dir});

        File folder = new File(dir, lib);
        if (folder.exists()) {
            File file = new File(folder, id);
            if (file.exists()) {
                Tika t = new Tika();
                String contentType = t.detect(file);
                byte[] content = getBytes(file);
                
                sendBinary(response, id, contentType, content);

            } else {
                LOG.log(Level.WARNING, "{0} does not exist", file.getAbsolutePath());
                throw new IOException("Unknown file");
            }
        } else {
            LOG.log(Level.WARNING, "{0} does not exist", folder.getAbsolutePath());
            throw new IOException("Unknown folder");
        }
    }

    public static void sendBinary(HttpServletResponse response, String filename,
            String contentType, byte[] filedata) throws IOException {
        response.setHeader("Content-Disposition", "inline; filename=\""
                + filename + "\"");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Pragma", "private");
        response.setHeader("cache-control", "private, must-revalidate");

        ServletOutputStream outs = response.getOutputStream();
        outs.write(filedata);
        outs.flush();
        outs.close();
        response.flushBuffer();
    }

    public static String getParameter(final HttpServletRequest request,
            final String parameter, final String defValue) {

        if (request.getParameter(parameter) == null) {
            return defValue;
        } else {
            return request.getParameter(parameter);
        }
    }

    /**
     * Turns a {@link java.io.File} into a byte array.
     *
     * @param file
     *          {@link java.io.File} to turn into a byte array
     * @return Byte array of the {@link java.io.File}
     * @throws java.io.IOException
     *              If the file could not be converted
     */
    public static byte[] getBytes(java.io.File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File size is too big");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset,
                bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
