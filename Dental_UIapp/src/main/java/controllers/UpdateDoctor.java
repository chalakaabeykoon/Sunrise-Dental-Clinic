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

@WebServlet(name = "UpdateDoctorServlet", urlPatterns = "/UpdateDoctorServlet")
public class UpdateDoctor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String doctorId = request.getParameter("doctorId");
        String doctorName = request.getParameter("doctorName");
        String location = request.getParameter("location");
        String telNo = request.getParameter("telNo");

        if (isBlank(doctorId) || isBlank(doctorName) || isBlank(location) || isBlank(telNo)) {
            response.sendRedirect("staff_dashboard.jsp?status=doc_error");
            return;
        }

        String sql = "UPDATE doctors SET doctor_name = ?, location = ?, tel_no = ? WHERE doctor_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorName.trim());
            stmt.setString(2, location.trim());
            stmt.setString(3, telNo.trim());
            stmt.setString(4, doctorId.trim());
            response.sendRedirect("staff_dashboard.jsp?status=" + (stmt.executeUpdate() > 0 ? "doc_updated" : "doc_error"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staff_dashboard.jsp?status=doc_error");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
