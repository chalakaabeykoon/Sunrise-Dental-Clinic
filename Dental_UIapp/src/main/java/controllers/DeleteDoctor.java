package controllers;

import Libs.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "DeleteDoctorServlet", urlPatterns = "/DeleteDoctorServlet")
public class DeleteDoctor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String doctorId = request.getParameter("doctorId");
        if (doctorId == null || doctorId.trim().isEmpty()) {
            response.sendRedirect("staff_dashboard.jsp?status=doc_error");
            return;
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM doctors WHERE doctor_id = ?")) {
            stmt.setString(1, doctorId.trim());
            response.sendRedirect("staff_dashboard.jsp?status=" + (stmt.executeUpdate() > 0 ? "doc_deleted" : "doc_error"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staff_dashboard.jsp?status=doc_error");
        }
    }
}
