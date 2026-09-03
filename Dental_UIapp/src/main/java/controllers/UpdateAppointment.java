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

@WebServlet("/UpdateAppointmentServlet")
public class UpdateAppointment extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get values submitted from the form
        String appointmentNum = request.getParameter("appointmentNum");
        String status = request.getParameter("status");

        // Validate input values
        if (appointmentNum == null || appointmentNum.trim().isEmpty()
                || status == null || status.trim().isEmpty()) {

            response.sendRedirect("doctor_dashboard.jsp?msg=error");
            return;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {

            // Create database connection
            connection = DBUtil.getConnection();

            // SQL query for updating appointment status
            String updateSQL =
                    "UPDATE appointments SET status = ? WHERE appointment_num = ?";

            // Prepare SQL statement
            preparedStatement = connection.prepareStatement(updateSQL);

            // Set values
            preparedStatement.setString(1, status.trim());
            preparedStatement.setString(2, appointmentNum.trim());

            // Execute update
            int result = preparedStatement.executeUpdate();

            // Check whether the appointment was updated
            if (result > 0) {

                response.sendRedirect(
                        "doctor_dashboard.jsp?msg=updated"
                );

            } else {

                response.sendRedirect(
                        "doctor_dashboard.jsp?msg=error"
                );
            }

        } catch (Exception e) {

            // Print error for debugging
            e.printStackTrace();

            // Redirect to dashboard with error message
            response.sendRedirect(
                    "doctor_dashboard.jsp?msg=error"
            );

        } finally {

            // Close PreparedStatement
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Close database connection
            if (connection != null) {
                DBUtil.closeConnection(connection);
            }
        }
    }
}