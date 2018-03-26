package derek.aut.ParsingGoogleAPIs;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import derek.aut.ParsingGoogleAPIs.dao.ItemDao;
import derek.aut.ParsingGoogleAPIs.dao.ItemDaoFactory;
import derek.aut.ParsingGoogleAPIs.dto.Item;
import derek.aut.ParsingGoogleAPIs.dto.Result;

/**
 * handlingJSON
 *
 */
public class App {
	private static Logger logger = LoggerFactory.getLogger(App.class);

	private static Map<String, Object> map = null;
	private static JsonObject jsonObject = null;
	private static boolean flag = true;
	
	private static boolean test = false;
	
	private static String value= ""; 

	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();

		String url = "https://www.googleapis.com/discovery/v1/apis?parameters";
		Result rootJson = null;
		
		if(!test) {
			rootJson = restTemplate.getForObject(url, Result.class);
		}

		try {
//			ItemDao itemDao = new ItemDaoFactory().userDao();
//			itemDao.connectionTest();
			
			if (rootJson != null) {
				List<Item> array = rootJson.getItems();

				// for (Item item : array) { item.getDiscoveryRestUrl()
				jsonObject = jsonParser.parse(restTemplate
						.getForObject("https://www.googleapis.com/discovery/v1/apis/books/v1/rest", String.class))
						.getAsJsonObject();

				if (false) {
					// Extract resources
					map = gson.fromJson(jsonObject.getAsJsonObject("resources"), Map.class);
					getJsonObject(map, false);
				}

				if (true) {
					// Extract schemas
					HashSet<String> hashset = new HashSet<String>();

					map = gson.fromJson(jsonObject.getAsJsonObject("schemas"), Map.class);
					getResponseJsonObject(map, hashset, false);

					Iterator<String> iterator = hashset.iterator();

					logger.info("-- START -- ");
					while (iterator.hasNext()) {
						iterator.next();
						//logger.info(iterator.next().toString());
					}
					logger.info("-- END -- ");
				}

				map = null;
				jsonObject = null;
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static boolean getResponseJsonObject(Map<String, Object> map, HashSet<String> hashset, boolean dataPutYn)
			throws ClassNotFoundException, SQLException {

		if (!map.isEmpty()) {
			value = "";

			for (String key : map.keySet()) { // The key is keys in schemas

				if(key.equals("id")) { // Extracting ID
					value = map.get(key).toString();
							
					if(!value.contains("{")) {
						logger.info("[Response Type]:" + value);
					}
				}
				
				if(key.equals("parameters")) { // Extracting PARAMETERS
					Map<String, Object> submap = (Map<String, Object>) map.get(key);

					for (String subkey : submap.keySet()) {
						Map<String, Object> secondSubmap = (Map<String, Object>) submap.get(subkey);

						for (String secondSubkey : secondSubmap.keySet()) {
							if (secondSubkey.equals("description")) {
								// logger.info("desc" + secondSubmap.get(secondSubkey).toString());
							} else if (secondSubkey.equals("type")) {
								// logger.info("type" + secondSubmap.get(secondSubkey).toString());
							}
						}
					}

					// flag = false;
				}
				
				if (key.equals("type")) { // Extracting METHOD
					value = map.get(key).toString(); // Get from each key in schemas
					//logger.info("\r\n[Method]:" + value);

					hashset.add(value);
				}

				value = "";

				if (map.get(key) instanceof Map) {
					if (getResponseJsonObject((Map<String, Object>) map.get(key), hashset, dataPutYn)) {
						break;
					}
				}
			}
		} else {
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public static boolean getJsonObject(Map<String, Object> map, boolean dataPutYn)
			throws ClassNotFoundException, SQLException {

		if (!map.isEmpty()) {
			value = "";

			for (String key : map.keySet()) { // resources name
				value = map.get(key).toString();

				if (key.equals("id")) { // Extracting METHOD
					// logger.info("\r\n[Method]:" + value);

					if (dataPutYn) {
						ItemDao itemDao = new ItemDaoFactory().userDao();
						itemDao.addMethodList(key, value);
					}
				} else if (key.equals("description")) { // Extracting DESCRIPTION
					// logger.info("- DESC:" + value);
				} else if (flag && key.equals("parameters")) { // Extracting PARAMETERS
					Map<String, Object> submap = (Map<String, Object>) map.get(key);

					for (String subkey : submap.keySet()) {
						// logger.info("[param] " + subkey);

						Map<String, Object> secondSubmap = (Map<String, Object>) submap.get(subkey);

						for (String secondSubkey : secondSubmap.keySet()) {
							if (secondSubkey.equals("description")) {
								// logger.info("desc" + secondSubmap.get(secondSubkey).toString());
							} else if (secondSubkey.equals("type")) {
								// logger.info("type" + secondSubmap.get(secondSubkey).toString());
							}
						}
					}

					// flag = false;
				} else if (key.equals("response")) { // Extracting RESPONSE
					Map<String, Object> submap = (Map<String, Object>) map.get(key);

					for (String subkey : submap.keySet()) {
						logger.info("response : " + subkey + "/" + submap.get(subkey).toString());
					}
				}

				value = "";

				if (map.get(key) instanceof Map) {
					if (getJsonObject((Map<String, Object>) map.get(key), dataPutYn)) {
						break;
					}
				}
			}
		} else {
			return true;
		}

		return false;
	}
}
