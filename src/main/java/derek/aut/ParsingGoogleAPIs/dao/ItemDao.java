package derek.aut.ParsingGoogleAPIs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import derek.aut.ParsingGoogleAPIs.dto.Item;

public class ItemDao {
	private static Logger logger = LoggerFactory.getLogger(ItemDao.class);
			
	private ConnectionMaker connectionMaker;

	public ItemDao(ConnectionMaker simpleConnectionMaker) {
		this.connectionMaker = simpleConnectionMaker;
	}

	public void connectionTest() throws SQLException, ClassNotFoundException {
		Connection c = this.connectionMaker.makeConnection();
		
		logger.info("Connection Success");
		
		c.close();
	}

	public void addMethodList(String key, String value) throws ClassNotFoundException, SQLException {
		Connection c = this.connectionMaker.makeConnection();

		PreparedStatement ps = c.prepareStatement("insert into methodList(method, value) values(?,?)");

		ps.setString(1, key);
		ps.setString(2, value);

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	public void add(List<Item> array) throws ClassNotFoundException, SQLException {
		Connection c = this.connectionMaker.makeConnection();

		PreparedStatement ps = c.prepareStatement(
				"insert into item(kind, id, name, version, title, description, discoverRestUrl, documentationLink, preferred) values(?,?,?,?,?,?,?,?,?)");

		int parameterIndex = 1;
		for (Item item : array) {
			parameterIndex = 1;

			ps.setString(parameterIndex++, item.getKind());
			ps.setString(parameterIndex++, item.getId());
			ps.setString(parameterIndex++, item.getName());
			ps.setString(parameterIndex++, item.getVersion());
			ps.setString(parameterIndex++, item.getTitle());
			ps.setString(parameterIndex++, item.getDescription());
			ps.setString(parameterIndex++, item.getDiscoveryRestUrl());
			ps.setString(parameterIndex++, item.getDocumentationLink());
			ps.setString(parameterIndex++, item.getPreferred());

			ps.executeUpdate();
		}

		ps.close();
		c.close();
	}

}
