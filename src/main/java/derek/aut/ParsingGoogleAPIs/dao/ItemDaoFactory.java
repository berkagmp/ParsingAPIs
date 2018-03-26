package derek.aut.ParsingGoogleAPIs.dao;

public class ItemDaoFactory {
	public ItemDao userDao() {
		ItemDao dao = new ItemDao(connectionMaker());
		return dao;
	}

	public ConnectionMaker connectionMaker() {
		ConnectionMaker connectionMaker = new MySQLConnectionMaker();
		return connectionMaker;
	}
}
