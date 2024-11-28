package hotel_database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;



public class ManageDB {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Shreyansh@7";

    /***
     * Created a menu() method that displays the options that are
     * available in the project.
     */
    public static void menu(){
        System.out.println("HOTEL MANAGEMENT SYSTEM ");
        System.out.println("1.Reserve a Room ");
        System.out.println("2.View Reservation");
        System.out.println("3.Get Room Number");
        System.out.println("4.Update Reservation");
        System.out.println("5.Delete Reservation");
        System.out.println("6.Exit");
    }

    /***
     * This method is used to reserver a room for the guest.
     * @param kb object of Scanner class
     * @param statement object of Statement class
     */
    private static void reserverRoom(Scanner kb,Statement statement) {
        try {
            System.out.println("Enter the name :");
            kb.nextLine();
            String guest_name = kb.nextLine();
            System.out.println("Enter the room number :");
            int room_num = kb.nextInt();
            System.out.println("Enter the contact number :");
            long contact_num = kb.nextLong();

            String insert_query = "INSERT INTO reservations(guest_name,room_number,contact_number) " +
                    "VALUES ('" + guest_name + "',"+ room_num + ",'"+contact_num +"')";
            try{
                int affectedrows = statement.executeUpdate(insert_query);

                if (affectedrows>0) System.out.println("Reservation Successful...");
                else System.out.println("Reservation failed...");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }catch (InputMismatchException ex){
            System.out.println(ex.getMessage());
        }
    }

    /***
     * This method allows the user to view the data in the reservation table.
     * @param statement object of Statement class.
     */

    public static void viewReservation (Statement statement){
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";
        try{
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("Current Reservation :");
            System.out.println("+-----------------------------------------------------------------------------------------------+");
            System.out.printf("| %-14s | %-15s | %-13s | %-17s | %-20s |\n",
                    "Reservation ID", "Guest Name", "Room Number", "Contact Number", "Reservation Date");
            System.out.println("+-----------------------------------------------------------------------------------------------+");

            while(rs.next()){
                int id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int roomnum = rs.getInt("room_number");
                String contact = rs.getString("contact_number");
                String date = rs.getTimestamp("reservation_date").toString();
                System.out.printf("| %-14s | %-15s | %-13s | %-17s | %-22s |\n",
                        id, name , roomnum , contact , date );

            }
            System.out.println("+-----------------------------------------------------------------------------------------------+");

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    /***
     * Created a method that prints the room number for provided reservation id.
     * @param statement object of Statement class.
     * @param kb object of Scanner class.
     */

    public static void getRoomNumber (Statement statement,Scanner kb) {
        System.out.println("Enter the Reservation Id :");
        int id = kb.nextInt();
        System.out.println("Enter the name :");
        kb.nextLine();
        String name = kb.nextLine();
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations WHERE reservation_id = "+ id +" AND guest_name = '"+ name +"'";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                System.out.println("Room of the reservaton id " + id + " with name " + name + " is : " + rs.getInt("room_number"));
            }else System.out.println("There is not room for the provided name and id!!");
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    /***
     * Created a method to update the reservation data.
     * @param kb object of Scanner class.
     * @param statement object of Statement class.
     */

    public static void updateReservation (Scanner kb,Statement statement) {
        System.out.println("Enter the reservation id that you want to update : ");
        int reservation_id = kb.nextInt();
        if (!reservationexist(statement,reservation_id)){
            System.out.println("Reservation id not found for input "+reservation_id);
            return;
        }
        System.out.println("Enter the name to be updated :");
        kb.nextLine();
        String new_guest_name = kb.nextLine();
        System.out.println("Enter the room number to be updated :");
        int new_room_num = kb.nextInt();
        System.out.println("Enter the contact number to be updated :");
        kb.nextLine();
        String new_contact_num = kb.nextLine();
        String sql = "UPDATE reservations SET guest_name = '"+ new_guest_name +"', " +
                "contact_number = '"+ new_contact_num+"', "+"room_number = "+new_room_num +
                " WHERE reservation_id = "+reservation_id;
        try{
            int affectedrows = statement.executeUpdate(sql);
            if (affectedrows>0){
                System.out.println("Details updated successfully...");
            }else System.out.println("Updation failed...");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /***
     * Created a method to delete the reservation of the guest for given reservation id.
     * @param statement object of Statement class.
     * @param kb object of Scanner class.
     */

    public static void deleteReservation (Statement statement,Scanner kb) {
        System.out.println("Enter the reservation for deletion : ");
        int reservation_id = kb.nextInt();
        if (!reservationexist(statement,reservation_id)){
            System.out.println("Reservation id not found for input "+reservation_id);
            return;
        }
        String sql = "DELETE from reservations"+
                " WHERE reservation_id = "+reservation_id;
        try{
            int affected_rows = statement.executeUpdate(sql);
            if (affected_rows>0){
                System.out.println("Deletion successfully...");
            }else System.out.println("Deletion failed...");
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    /***
     * Created a method that checks whether the entered reservation id is present or not.
     * @param statement Object of Statement class.
     * @param id reservation id of the guest.
     * @return true if reservation id is present in the database else return false.
     */

    public static boolean reservationexist(Statement statement,int id) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + id;

            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * Created a method to exit the application.
     * @throws InterruptedException Can arise from the Thread.sleep() method.
     */

    public static void exit() throws InterruptedException{
        int i=0;
        System.out.print("Exiting System");
        while(i<5){
            System.out.print(".");
            Thread.sleep(450);
            i++;
        }
        System.out.println();
        System.out.println("THANKYOU FOR USING HOTEL MANAGEMENT SYSTEM");
        System.exit(0);
    }

    public static void main (String[]args) throws ClassNotFoundException, SQLException {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Driver can't be connected " + ex.getMessage());
            }
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                Scanner kb = new Scanner(System.in);
                while (true) {
                    ManageDB.menu();
                    System.out.println("Choose an option : ");
                    int choice = kb.nextInt();
                    switch (choice) {
                        case 1:
                            reserverRoom(kb,statement);
                            break;
                        case 2:
                            viewReservation(statement);
                            break;
                        case 3:
                            getRoomNumber(statement,kb);
                            break;
                        case 4:
                            ManageDB.updateReservation(kb,statement);
                            break;
                        case 5:
                            ManageDB.deleteReservation(statement,kb);
                            break;
                        case 6:
                            try {
                                exit();
                            }catch (InterruptedException ex){
                                System.out.println(ex.getMessage());
                            }
                        default:
                            System.out.println("Invalid input!!!!");
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Connection failed : " + ex.getMessage());
            }

        }
    }
