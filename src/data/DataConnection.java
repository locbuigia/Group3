package data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


import javax.swing.JOptionPane;


/**
 * This class has the information to connect to the cssgate server.
 * to customize for your own project.
 * @author Loc Bui
 */


public class DataConnection {

	private static String userName = "locbui";
	private static String password = "DuwavUg";
	private static String serverName = "cssgate.insttech.washington.edu";
	public static Connection sConnection;

	// Creates once instance of the connection to be reused in the different places in the
	// system.
	private static void createConnection() throws SQLException {
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);


		sConnection =  DriverManager
				.getConnection("jdbc:mysql://" + serverName + "/"
						+ userName + "?user=" + userName + "&password=" + password);

		//For debugging - System.out.println("Connected to database");
	}

	/**
	 * Returns a connection to the database so that queries can be executed.
	 * @return Connection to the database
	 * @throws SQLException
	 */
	public static Connection getConnection() {
		if (sConnection == null) {
			try {
				createConnection();
			} catch (SQLException e) {
//				e.printStackTrace(); //For debugging.
				JOptionPane.showMessageDialog(null, "Unable to connect to the server!"
								+ "\nPlease check your internet connection and restart the program!",
						"Login failed" , JOptionPane.WARNING_MESSAGE);
			}
		}
		return sConnection;
	}

	/**
	 * Close the connection to the database when done.
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException {
		if (sConnection != null && !sConnection.isClosed()) {
			sConnection.close();
		}
	}


}
