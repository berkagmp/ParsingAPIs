package derek.aut.project.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLConnectionMaker implements ConnectionMaker {
	private static Logger logger = LoggerFactory.getLogger(MySQLConnectionMaker.class);

	private String url = "";
	private String id = "";
	private String pwd = "";

	public MySQLConnectionMaker() {
		Properties prop = new Properties();
		InputStream inputStream = null;
		String propFileName = "config.properties";

		logger.info("MySQLConnectionMaker Class");

		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);

			// load a properties file
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("The property file '" + propFileName + "' not found in the classpath");
			}

			url = prop.getProperty("mySQLURL");
			id = prop.getProperty("mySQLID");
			pwd = prop.getProperty("mySQLPWD");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection c = DriverManager.getConnection(url, id, pwd);

		return c;
	}

	public void releaseConnection(Connection c) {
		try {
			if (c != null)
				c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void releaseConnection(PreparedStatement p) {
		try {
			if (p != null)
				p.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void releaseConnection(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
