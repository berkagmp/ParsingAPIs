package derek.aut.ParsingGoogleAPIs.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public interface ConnectionMaker {

	public abstract Connection makeConnection() throws ClassNotFoundException, SQLException;

	public abstract void releaseConnection(Connection c);

	public abstract void releaseConnection(PreparedStatement p);

	public abstract void releaseConnection(ResultSet rs);
}