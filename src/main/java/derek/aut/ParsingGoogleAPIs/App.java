package derek.aut.ParsingGoogleAPIs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import derek.aut.ParsingGoogleAPIs.dao.ItemDao;
import derek.aut.ParsingGoogleAPIs.dao.ItemDaoFactory;
import derek.aut.ParsingGoogleAPIs.dto.Item;
import derek.aut.ParsingGoogleAPIs.dto.Method;
import derek.aut.ParsingGoogleAPIs.dto.RequestParameter;
import derek.aut.ParsingGoogleAPIs.dto.ResponseObject;
import derek.aut.ParsingGoogleAPIs.dto.ResponseProperty;
import derek.aut.ParsingGoogleAPIs.dto.Result;

/**
 * handlingJSON
 *
 */
public class App {
	private static Logger logger = LoggerFactory.getLogger(App.class);

	private static Map<String, Object> map = null;
	private static JsonObject jsonObject = null;

	private static boolean dataPutYn = true;
	private static boolean test = false;

	private static String value = "";

	private static Method method = new Method();
	private static List<Method> methodList = new ArrayList<>();
	private static RequestParameter requestParameter = new RequestParameter();
	private static List<RequestParameter> requestParameterList = new ArrayList<>();

	private static ResponseObject responseObject = new ResponseObject();
	private static List<ResponseObject> responseObjectList = new ArrayList<>();
	private static ResponseProperty responseProperty = new ResponseProperty();
	private static List<ResponseProperty> responsePropertyList = new ArrayList<>();

	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();

		String url = "https://www.googleapis.com/discovery/v1/apis?parameters";
		Result rootJson = null;

		ItemDao itemDao = null;

		if (!test) {
			rootJson = restTemplate.getForObject(url, Result.class);
		}

		try {
			// ItemDao itemDao = new ItemDaoFactory().userDao();
			// itemDao.connectionTest();

			if (dataPutYn) {
				itemDao = new ItemDaoFactory().userDao();
			}

			if (rootJson != null) {
				List<Item> array = rootJson.getItems();

				// for (Item item : array) { item.getDiscoveryRestUrl()
				jsonObject = jsonParser.parse(restTemplate
						.getForObject("https://www.googleapis.com/discovery/v1/apis/books/v1/rest", String.class))
						.getAsJsonObject();

				String apiName = "books:v1";

				if (false) {
					// Extract resources recursively
					map = gson.fromJson(jsonObject.getAsJsonObject("resources"), Map.class);
					getJsonObject(map, apiName);

					if (dataPutYn) {
						// Insert to Database
						itemDao.insertApiMethodAndReqeustParameter(methodList);
					}
				}

				if (true) {
					// Extract schemas
					map = gson.fromJson(jsonObject.getAsJsonObject("schemas"), Map.class);
					getResponseJsonObject(map, apiName);

					if (dataPutYn) {
						// Insert to Database
						itemDao.insertResponseObjectAndResponseProperty(responseObjectList);
					}
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
	public static boolean getResponseJsonObject(Map<String, Object> map, String api)
			throws ClassNotFoundException, SQLException {

		if (!map.isEmpty()) {
			value = "";

			for (String key : map.keySet()) { // The key is keys in schemas

				if (key.equals("id")) { // Extracting ID
					value = map.get(key).toString();

					if (!value.contains("{")) {
						// System.out.println("[Response Type]:" + value);
						responseObject.setApi(api);
						responseObject.setObjectName(value);
					}
				}

				if (key.equals("properties")) { // Extracting PROPERTIES
					Map<String, Object> submap = (Map<String, Object>) map.get(key);

					for (String subkey : submap.keySet()) {
						Map<String, Object> secondSubmap = (Map<String, Object>) submap.get(subkey);
						// System.out.println("\r\n[name] " + subkey);
						responseProperty = new ResponseProperty();
						responseProperty.setResName(subkey);

						for (String secondSubkey : secondSubmap.keySet()) {
							if (secondSubkey.equals("description")) {
								// System.out.println(secondSubkey + ": " +
								// secondSubmap.get(secondSubkey).toString());
								responseProperty.setDescription(secondSubmap.get(secondSubkey).toString());
							} else if (secondSubkey.equals("type") || secondSubkey.equals("$ref")) {
								responseProperty.setResType(secondSubmap.get(secondSubkey).toString());
							}
						}

						responsePropertyList.add(responseProperty);
					}

				}

				if (key.equals("type")) { // Extracting METHOD
					value = map.get(key).toString(); // Get from each key in schemas
					// System.out.println("\r\n[Method]:" + value);
				}

				value = "";

				if (!StringUtils.isEmpty(responseObject.getObjectName())) {
					responseObject.setResponsePropertyList(responsePropertyList);
					responseObjectList.add(responseObject);

					// System.out.println("\r\n" + responseObject.toString());

					responseObject = new ResponseObject();
					responsePropertyList = new ArrayList<>();
				}

				if (map.get(key) instanceof Map) {
					if (getResponseJsonObject((Map<String, Object>) map.get(key), api)) {
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
	public static boolean getJsonObject(Map<String, Object> map, String api)
			throws ClassNotFoundException, SQLException {

		if (!map.isEmpty()) {
			value = "";

			for (String key : map.keySet()) { // resources name
				value = map.get(key).toString();

				if (key.equals("id")) { // Extracting METHOD
					// System.out.println("\r\n[Method]:" + value);
					method.setApi(api);
					method.setMethod(value);
				} else if (key.equals("description")) { // Extracting DESCRIPTION
					// System.out.println("- DESC:" + value);
					method.setDescription(value);
				} else if (key.equals("parameters")) { // Extracting PARAMETERS
					Map<String, Object> submap = (Map<String, Object>) map.get(key);

					for (String subkey : submap.keySet()) {
						// System.out.println("[param] " + subkey);
						requestParameter = new RequestParameter();
						requestParameter.setParam(subkey);

						Map<String, Object> secondSubmap = (Map<String, Object>) submap.get(subkey);

						for (String secondSubkey : secondSubmap.keySet()) {
							if (secondSubkey.equals("description")) {
								// System.out.println(secondSubkey + ": " +
								// secondSubmap.get(secondSubkey).toString());
								requestParameter.setDesciption(secondSubmap.get(secondSubkey).toString());
							}
						}

						// System.out.println(rp.toString());
						requestParameterList.add(requestParameter);
					}

				} else if (key.equals("response")) { // Extracting RESPONSE
					Map<String, Object> submap = (Map<String, Object>) map.get(key);

					// System.out.println(submap.get("$ref").toString());
					method.setResponseType(submap.get("$ref").toString());
				}

				value = "";

				if (!StringUtils.isEmpty(method.getResponseType())) {
					method.setRequestParameterList(requestParameterList);
					methodList.add(method);

					// System.out.println("\r\n" + m.toString());

					method = new Method();
					requestParameterList = new ArrayList<>();
				}

				if (map.get(key) instanceof Map) {
					if (getJsonObject((Map<String, Object>) map.get(key), api)) {
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
