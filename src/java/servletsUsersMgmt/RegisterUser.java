package servletsUsersMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author miguelcasillas
 */
public class RegisterUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                //This database must be created before running the project
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/AdminUsuarios", "administrador", "administrador");
                Statement query = con.createStatement();
                String username = request.getParameter("usuario");
                String password = request.getParameter("password");
                ResultSet rs = query.executeQuery("SELECT * FROM USERS WHERE USERNAME = '" + username + "'");
                String next = "";
                if (!rs.next()) {
                    request.setAttribute("fromRegisterUser", (Object)"YES");
                    out.print("User registered correctly.");
                    query.executeUpdate("INSERT INTO USERS(USERNAME,PASSWORD) VALUES('" + username + "','" + password + "')");
                    
                    next = "/index.jsp";
                } else {
                    request.setAttribute("fromRegisterUser", (Object)"NO");
                    next = "/signup.jsp";
                }
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(next);
                dispatcher.forward(request,response);
                con.close();
            }
            catch (SQLException ex) {
                Logger.getLogger(RegisterUser.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex) {
                Logger.getLogger(RegisterUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
