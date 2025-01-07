package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
        private Connection connection;
        private Scanner scanner;

        public Doctors(Connection connection, Scanner scanner){
            this.connection=connection;
            this.scanner=scanner;
        }
        public void viewDoctors(){
            try{
                String query="select * from doctors";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                ResultSet resultSet=preparedStatement.executeQuery();
                System.out.println("Doctors :");
                System.out.println("+------------+-------------------------+----------------+");
                System.out.printf("| Doctors Id |      Doctors Name       | Specialization |\n");
                System.out.println("+------------+-------------------------+----------------+");
                while (resultSet.next()){
                    int id=resultSet.getInt("id");
                    String name=resultSet.getString("name");
                    String specialization=resultSet.getString("specialization");
                    System.out.printf("| %-11s| %-22s| %-17s|\n",id,name,specialization);
                    System.out.println("+------------+-------------------------+----------------+");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        public boolean getDoctorsById(int id){
            String query="select * from doctors where id=?";
            try{
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setInt(1,id);
                ResultSet resultSet=preparedStatement.executeQuery();
                if(resultSet.next()){
                    return true;
                }
                else {
                    return false;
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            return false;
        }
    }
