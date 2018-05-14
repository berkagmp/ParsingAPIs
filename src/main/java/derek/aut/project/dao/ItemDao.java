package derek.aut.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import derek.aut.project.dto.Item;
import derek.aut.project.dto.Method;
import derek.aut.project.dto.RequestParameter;
import derek.aut.project.dto.ResponseObject;
import derek.aut.project.dto.ResponseProperty;

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

	public void insertApiMethodAndReqeustParameter(List<Method> methodList) {
		Connection c = null;

		PreparedStatement seq_ps = null;
		PreparedStatement ps = null;
		PreparedStatement sub_ps = null;

		ResultSet rs = null;

		int index = 1;
		int primaryKey = 0;

		String seq_sql = "select IFNULL(MAX(CAST(id AS DECIMAL)), 0) + 1 as id from api_method";
		String sql = "insert into api_method(id, api, method, description, method_realname) values(?, ?, ?, ?, ?)";
		String sub_sql = "insert into request_parameter(param, description, id) values(?, ?, ?)";

		try {
			c = this.connectionMaker.makeConnection();
			c.setAutoCommit(false);

			seq_ps = c.prepareStatement(seq_sql);
			ps = c.prepareStatement(sql);
			sub_ps = c.prepareStatement(sub_sql);
			//System.out.println("methodList Cnt\r\n" + methodList.size());
			
			for (Method m : methodList) {
				rs = seq_ps.executeQuery();

				if (rs.next()) {
					primaryKey = rs.getInt("id");
				}
				
				System.out.println("\r\n" + m.toString());
				index = 1;
				ps.setInt(index++, primaryKey);
				ps.setString(index++, m.getApi());
				ps.setString(index++, m.getMethod());
				ps.setString(index++, m.getDescription());
				ps.setString(index++, m.getMethodRealname());

				ps.executeUpdate();

				List<RequestParameter> requestParameterList = m.getRequestParameterList();

				if (requestParameterList != null && requestParameterList.size() > 0) {
					for (RequestParameter r : requestParameterList) {
						index = 1;
						sub_ps.setString(index++, r.getParam());
						sub_ps.setString(index++, r.getDesciption());
						sub_ps.setInt(index++, primaryKey);

						sub_ps.executeUpdate();
					}
				}
			}

			c.commit();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			try {
				c.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			connectionMaker.releaseConnection(rs);
			connectionMaker.releaseConnection(seq_ps);
			connectionMaker.releaseConnection(sub_ps);
			connectionMaker.releaseConnection(ps);
			connectionMaker.releaseConnection(c);
		}
	}

	public void insertResponseObjectAndResponseProperty(List<ResponseObject> responseObjectList) {
		Connection c = null;

		PreparedStatement seq_ps = null;
		PreparedStatement ps = null;
		PreparedStatement sub_ps = null;

		ResultSet rs = null;

		int index = 1;
		int primaryKey = 0;

		String seq_sql = "select IFNULL(MAX(CAST(rid AS DECIMAL)), 0) + 1 as id from response_object";
		String sql = "insert into response_object(rid, api, object_name) values (?, ?, ?)";
		String sub_sql = "insert into response_property(res_name, res_type, description, rid) values(?, ?, ?, ?)";

		try {
			c = this.connectionMaker.makeConnection();
			c.setAutoCommit(false);

			logger.info("Size: " + String.valueOf(responseObjectList.size()));

			seq_ps = c.prepareStatement(seq_sql);
			ps = c.prepareStatement(sql);
			sub_ps = c.prepareStatement(sub_sql);

			for (ResponseObject r : responseObjectList) {
				rs = seq_ps.executeQuery();

				if (rs.next()) {
					primaryKey = rs.getInt("id");
				}

				index = 1;
				ps.setInt(index++, primaryKey);
				ps.setString(index++, r.getApi());
				ps.setString(index++, r.getObjectName());

				ps.executeUpdate();

				List<ResponseProperty> responsePropertyList = r.getResponsePropertyList();

				if (responsePropertyList != null && responsePropertyList.size() > 0) {
					for (ResponseProperty rp : responsePropertyList) {
						index = 1;
						sub_ps.setString(index++, rp.getResName());
						sub_ps.setString(index++, rp.getResType());
						sub_ps.setString(index++, rp.getDescription());
						sub_ps.setInt(index++, primaryKey);

						sub_ps.executeUpdate();
					}
				}
			}

			c.commit();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			try {
				c.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			connectionMaker.releaseConnection(rs);
			connectionMaker.releaseConnection(seq_ps);
			connectionMaker.releaseConnection(sub_ps);
			connectionMaker.releaseConnection(ps);
			connectionMaker.releaseConnection(c);
		}
	}

	public void add(List<Item> array) {
		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = this.connectionMaker.makeConnection();

			ps = c.prepareStatement(
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
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connectionMaker.releaseConnection(ps);
			connectionMaker.releaseConnection(c);
		}
	}
}
