package data;

import student.EmploymentData;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods to access the Employment
 *
 * @author Nico Tandyo
 * @author Yau Tsang
 */
public class EmploymentDB {

    /**
     * Connection to the database.
     */
    private static Connection myConnection;

    /**
     * List of the employment.
     */
    private List<EmploymentData> myEmploymentList;

    /**
     * Adds a new employment to the Employment table.
     *
     * @param theEmployment Contains employment's information.
     * @return Returns true if adding success, false otherwise.
     */
    public static boolean addEmployment(final EmploymentData theEmployment) {
        String sql = "insert into Employment(`sid`,`company`,`position`,`description`,"
                + "`skillUsed`,`startDay`,`endDay`, `type`, `salary`) values "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?); ";

        if (myConnection == null) {
            myConnection = DataConnection.getConnection();
        }

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = myConnection.prepareStatement(sql);
            preparedStatement.setString(1, theEmployment.getSID());
            preparedStatement.setString(2, theEmployment.getCompany());
            preparedStatement.setString(3, theEmployment.getPosition());
            preparedStatement.setString(4, theEmployment.getDescription());
            preparedStatement.setString(5, theEmployment.getSkill());
            preparedStatement.setString(6, theEmployment.getStartDate());
            preparedStatement.setString(7, theEmployment.getEndDate());
            preparedStatement.setString(8, theEmployment.getMyType());
            preparedStatement.setInt(9, theEmployment.getSalary());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to connect to the server!"
                            + "\nPlease check your internet connection and restart the program!",
                    "Failed Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Modifies the data on an Employment .
     *
     * @param theEmployment EmploymentData object.
     * @param theCol        column name.
     * @param theData       the change of input.
     * @return Returns a message with success or failure.
     */
    public static boolean updateEmployment(final EmploymentData theEmployment,
                                           final String theCol, final Object theData) {

        int employmentid = Integer.parseInt(theEmployment.getMyEmploymentId());

        String sql = "update Employment set `" + theCol
                + "` = ?  where employmentid = ?";
        // For debugging - System.out.println(sql);
        PreparedStatement preparedStatement = null;
        if (myConnection == null) {
            myConnection = DataConnection.getConnection();
        }
        try {
            preparedStatement = myConnection.prepareStatement(sql);
            if (theData instanceof String)
                preparedStatement.setString(1, (String) theData);
            else if (theData instanceof Integer)
                preparedStatement.setInt(1, (Integer) theData);
            preparedStatement.setInt(2, employmentid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
//            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * Retrieves all Employment from the Employment table.
     *
     * @return list of employments.
     * @throws SQLException It throws exception if error occur.
     */
    public List<EmploymentData> getEmployments() throws SQLException {
        if (myConnection == null) {
            myConnection = DataConnection.getConnection();
        }
        Statement stmt = null;
        String query = "select Student.name , Employment.*\n" +
                "from Student join Employment\n" +
                "on Student.sid = Employment.sid;";

        myEmploymentList = new ArrayList<>();
        try {
            if (myConnection == null) {
                return null;
            }
            stmt = myConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                String sid = rs.getString("sid");
                String company = rs.getString("company");
                String pos = rs.getString("position");
                String desc = rs.getString("description");
                String skill = rs.getString("skillUsed");
                String sd = rs.getString("startDay");
                String ed = rs.getString("endDay");
                String type = rs.getString("type");
                String eid = rs.getString("employmentid");
                int salary = rs.getInt("salary");
                EmploymentData employment = null;
                employment = new EmploymentData(sid, company, pos, desc,
                        skill, salary, type, sd, ed);
                employment.setMyStudentName(name);
                employment.setMyEmploymentId(eid);
                myEmploymentList.add(employment);
            }
        } catch (SQLException e) {
//            e.printStackTrace(); //For debugging
            JOptionPane.showMessageDialog(null, "Unable to connect to the server!"
                            + "\nPlease check your internet connection and restart the program!",
                    "Failed Warning", JOptionPane.WARNING_MESSAGE);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return myEmploymentList;
    }

    /**
     * Returns all employments that contain the search keyword in the name or description.
     *
     * @param theSearch keyword of searching.
     * @return list of employment that match.
     * @throws SQLException It throws exception if error occur.
     */
    public List<EmploymentData> searchEmployments(String theSearch) throws SQLException {
        List<EmploymentData> filterList = new ArrayList<>();
        if (myEmploymentList == null) {
            getEmployments();
        }
        theSearch = theSearch.toLowerCase();
        // searching name
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getMyStudentName().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        // student ID
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getSID().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        // company name
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getCompany().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        // position
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getPosition().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        // skill
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getSkill().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        // start date
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getStartDate().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        // end date
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getEndDate().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        // type - job or internship
        for (EmploymentData emp : myEmploymentList) {
            if (emp.getMyType().toLowerCase().contains(theSearch)
                    && !filterList.contains(emp)) {
                filterList.add(emp);
            }
        }
        return filterList;
    }

}