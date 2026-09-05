package controllers;

import Libs.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/LoginServlet")
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        if (user == null || pass == null || user.trim().isEmpty() || pass.isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String query = "SELECT full_name, role FROM users WHERE username = ? AND password = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.trim());
                stmt.setString(2, pass);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        request.setAttribute("errorMessage", "Invalid username or password.");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                        return;
                    }

                    String fullName = rs.getString("full_name");
                    String role = rs.getString("role");

                    HttpSession session = request.getSession(true);
                    session.setAttribute("user", user.trim());
                    session.setAttribute("fullName", fullName);
                    session.setAttribute("role", role);

                    String contextPath = request.getContextPath();
                    if ("DOCTOR".equalsIgnoreCase(role)) {
                        response.sendRedirect(contextPath + "/doctor_dashboard.jsp");
                    } else {
                        response.sendRedirect(contextPath + "/staff_dashboard.jsp");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Unable to connect to the database.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } finally {
            DBUtil.closeConnection(conn);
        }
    }
}