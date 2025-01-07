package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

import static java.lang.Class.forName;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "jigesh123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patients patients = new Patients(connection, scanner);
            Doctors doctors = new Doctors(connection, scanner);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your Choice :");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        patients.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patients.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctors.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patients,doctors,connection,scanner);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patients patients, Doctors doctors, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient Id");
        int p_id = scanner.nextInt();
        System.out.println("Enter Doctor Id");
        int d_id = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD");
        String a_date = scanner.next();
        if (patients.getPatientById(p_id) && doctors.getDoctorsById(d_id)) {
            if (checkDoctorAvailabitlity(d_id, a_date,connection)) {
                String query = "insert into appointment(patients_id,doctors_id,appointment_date)values(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, p_id);
                    preparedStatement.setInt(2, d_id);
                    preparedStatement.setString(3, a_date);
                    int row_affected = preparedStatement.executeUpdate();
                    if (row_affected > 0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Failed to book appointment");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Doctor not Available");

            }
        } else {
            System.out.println("Either doctor or patient doesnt exist");
        }
    }

    public static boolean checkDoctorAvailabitlity(int doctorId, String appointmentDate, Connection connection) {
        String query = "select count(*) from appointment where doctors_id=? and appointment_date=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
