package derek.aut.project;

import static java.lang.System.out;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import derek.aut.project.dao.ItemDao;
import derek.aut.project.dao.ItemDaoFactory;
import derek.aut.project.dto.GoogleAPI;
import derek.aut.project.dto.Item;
import derek.aut.project.dto.Method;
import derek.aut.project.dto.RequestParameter;

/**
 * handlingJSON
 *
 */
public class GoogleAPIParser {
	private static Map<String, Object> map = null;
	private static JsonObject jsonObject = null;

	private static String apiName = "";
	private static String canonicalName = "";

	private static Method method = new Method();
	private static List<Method> methodList = new ArrayList<>();
	private static RequestParameter requestParameter = new RequestParameter();
	private static List<RequestParameter> requestParameterList = new ArrayList<>();

	private static boolean insertDB = false;

	private static final String googleAPIURL = "https://www.googleapis.com/discovery/v1/apis?parameters";
	
	// The path of local directory to save APIs
	private static final String dirPath = "C:\\GoogleAPIs\\";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Download APIs to local directory
		downLoadAPIs();

		// Parsing APIs and insert DB
		insertDB = false;
		parsingJson();
	}

	public static void downLoadAPIs() {
		RestTemplate restTemplate = new RestTemplate();
		GoogleAPI googleAPI = restTemplate.getForObject(googleAPIURL, GoogleAPI.class);
		List<Item> list = Arrays.asList(googleAPI.getItems());
		out.println(list.size());

		URL localUrl;
		String path;
		String fileName;

		for (Item item : list) {
			path = item.getDiscoveryRestUrl();
			fileName = item.getId().replaceAll(":", ".");

			if (new File(dirPath + fileName + ".json").exists()) {
				fileName += "_" + new java.text.SimpleDateFormat("HHmmss").format(new Date());
			}

			File newFile = new File(dirPath + fileName + ".json");

			try {
				localUrl = new URL(path);
				FileUtils.copyURLToFile(localUrl, newFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void parsingJson() {
		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();

		try {
			ItemDao itemDao = new ItemDaoFactory().userDao();
			File[] dirList = new File(dirPath).listFiles();

			for (int f = 0; f < dirList.length; f++) {
				if (dirList[f].getName().substring(dirList[f].getName().lastIndexOf(".") + 1).equals("json")) {
					out.println(dirList[f].getName());

					jsonObject = jsonParser.parse(new FileReader(dirList[f])).getAsJsonObject();

					apiName = jsonObject.get("name").toString().replaceAll("\"", "").trim();
					canonicalName = jsonObject.get("title") == null ? "NoName " + apiName
							: jsonObject.get("title").toString().replaceAll("API", "").replaceAll("\"", "").trim();

					// Extract resources recursively
					map = gson.fromJson(jsonObject.getAsJsonObject("resources"), Map.class);
					getJsonObject(map, apiName, canonicalName);

					map = null;
					jsonObject = null;
				}

			}

			methodList.forEach(System.out::println);

			if (insertDB) {
				// Insert to Database
				itemDao.insertApiMethodAndReqeustParameter(methodList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean getJsonObject(Map<String, Object> map, String apiName, String canonicalName)
			throws ClassNotFoundException, SQLException {

		if (!map.isEmpty()) {
			for (String key : map.keySet()) { // resources name
				if (key.equals("id") && map.get("id").toString().indexOf("{") < 0
						&& map.get("id").toString().indexOf("}") < 0) {
					method.setApi(canonicalName);
					method.setMethod(map.get(key).toString().replaceAll("\\.", " ").replaceAll(apiName, canonicalName));
					method.setHttpMethod(map.get("httpMethod").toString());
					method.setMethodRealname(map.get(key).toString());
					method.setType("g");

					if (map.get("description") != null) {
						method.setDescription(map.get("description").toString());
					} else {
						method.setDescription("");
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
									requestParameter.setDescription(secondSubmap.get(secondSubkey).toString());
								}
							}

							requestParameterList.add(requestParameter);
						}

						method.setRequestParameterList(requestParameterList);
					}

					methodList.add(method);

					method = new Method();
					requestParameterList = new ArrayList<>();
				}

				if (map.get(key) instanceof Map) {
					if (getJsonObject((Map<String, Object>) map.get(key), apiName, canonicalName)) {
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
