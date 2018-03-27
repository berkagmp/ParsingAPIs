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
	private static boolean testYn = false;

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
		Result result = null;

		ItemDao itemDao = null;

		if (!testYn) {
			result = restTemplate.getForObject(url, Result.class);
		}

		try {
			// ItemDao itemDao = new ItemDaoFactory().userDao();
			// itemDao.connectionTest();

			if (dataPutYn) {
				itemDao = new ItemDaoFactory().userDao();
			}

			if (result != null) {
				List<Item> array = result.getItems();
				for (Item item : array) {
					if (item.getId().indexOf("alpha") < 0 && item.getId().indexOf("beta") < 0
							&& item.getId().indexOf("sandbox") < 0) {
						// String apiName = "books:v1";
						String apiName = item.getId();

						if (apiName.substring(0, 1).equalsIgnoreCase("C")) {
							// "https://www.googleapis.com/discovery/v1/apis/books/v1/rest"
							jsonObject = jsonParser
									.parse(restTemplate.getForObject(item.getDiscoveryRestUrl(), String.class))
									.getAsJsonObject();

							if (!StringUtils.isEmpty(apiName)) {
								System.out.println(apiName);

								if (!testYn) {
									// Extract resources recursively
									map = gson.fromJson(jsonObject.getAsJsonObject("resources"), Map.class);
									getJsonObject(map, apiName);

									if (dataPutYn) {
										// Insert to Database
										itemDao.insertApiMethodAndReqeustParameter(methodList);
									}
								}

								if (!testYn) {
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
							}
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean getResponseJsonObject(Map<String, Object> map, String api)
			throws ClassNotFoundException, SQLException {

		if (map != null) {
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

					System.out.println("\r\n" + responseObject.toString());

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
			for (String key : map.keySet()) { // resources name
				if (key.equals("id")) { // Extracting METHOD
					method.setApi(api);
					method.setMethod(map.get(key).toString());

					if (map.get("description") != null) {
						method.setDescription(map.get("description").toString());
					} else {
						method.setDescription("");
					}

					if (map.get("response") != null) {
						method.setResponseType(((Map<String, Object>) map.get("response")).get("$ref").toString());
					} else {
						method.setResponseType("");
					}

					// Extracting PARAMETERS
					Map<String, Object> submap = (Map<String, Object>) map.get("parameters");

					if (submap != null) {
						for (String subkey : submap.keySet()) {
							requestParameter = new RequestParameter();
							requestParameter.setParam(subkey);

							Map<String, Object> secondSubmap = (Map<String, Object>) submap.get(subkey);

							for (String secondSubkey : secondSubmap.keySet()) {
								if (secondSubkey.equals("description")) {
									requestParameter.setDesciption(secondSubmap.get(secondSubkey).toString());
								}
							}

							requestParameterList.add(requestParameter);
						}

						method.setRequestParameterList(requestParameterList);
					}

					methodList.add(method);

					System.out.println("\r\n" + method.toString());

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
