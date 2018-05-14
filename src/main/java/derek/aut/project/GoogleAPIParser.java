package derek.aut.project;

import static java.lang.System.out;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import derek.aut.project.dto.Item;
import derek.aut.project.dto.Method;
import derek.aut.project.dto.RequestParameter;
import derek.aut.project.dto.Result;

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
	
	private static int MethodCount = 0;
	
	private static boolean dataPutYn = true;
	private static boolean testYn = false;

	/**
	 * @param args
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		JsonParser jsonParser = new JsonParser();
		Gson gson = new Gson();
		
		File file = null;
		File[] dirList = null;

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
			
			List<Item> array = result.getItems();
			
			/*for (int i = 0; i < array.size(); i++) {
				out.println(array.get(i).getDiscoveryRestUrl());
				out.println(array.get(i).getId	());
				DownloadFiles(array.get(i).getDiscoveryRestUrl(), array.get(i).getId().replaceAll(":", "."));
			}*/

			file = new File("C:\\Users\\berka\\Google Drive\\00-2.AUT\\01.Research Project\\GoogleAPIs");
			dirList = file.listFiles();
			
			for (int f = 0; f < dirList.length; f++) {
				if (dirList[f].getName().substring(dirList[f].getName().lastIndexOf(".") + 1).equals("json")) {
					out.println(dirList[f].getName());

					jsonObject = jsonParser.parse(new FileReader(dirList[f])).getAsJsonObject();
					
					apiName = jsonObject.get("name").toString().replaceAll("\"", "").trim();
					canonicalName = jsonObject.get("title") == null ? "NoName " + apiName : jsonObject.get("title").toString().replaceAll("API", "").replaceAll("\"", "").trim();
					
					//out.println(canonicalName + "/" + apiName);
					
					// Extract resources recursively
					map = gson.fromJson(jsonObject.getAsJsonObject("resources"), Map.class);
					getJsonObject(map, apiName, canonicalName);

					map = null;
					jsonObject = null;
				}

			}
			
			if (dataPutYn) {
				// Insert to Database
				itemDao.insertApiMethodAndReqeustParameter(methodList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		out.println("MethodCount : "+ MethodCount);
		out.println("MethodObjCount : "+ methodList.size());
	}
	
	@SuppressWarnings("unchecked")
	public static boolean getJsonObject(Map<String, Object> map, String apiName, String canonicalName)
			throws ClassNotFoundException, SQLException {
		
		if (!map.isEmpty()) {
			for (String key : map.keySet()) { // resources name
				if (key.equals("id") && map.get("id").toString().indexOf("{") < 0 && map.get("id").toString().indexOf("}") < 0 ) {
					method.setApi(canonicalName);
					method.setMethod(map.get(key).toString().replaceAll("\\.", " ").replaceAll(apiName, canonicalName));
					method.setMethodRealname(map.get(key).toString());

					if (map.get("description") != null) {
						method.setDescription(map.get("description").toString().trim().replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("  ", " "));
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
									requestParameter.setDesciption(secondSubmap.get(secondSubkey).toString().trim().replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("  ", " "));
								}
							}

							requestParameterList.add(requestParameter);
						}

						method.setRequestParameterList(requestParameterList);
					}

					methodList.add(method);

					//out.println("\r\n" + method.toString());

					method = new Method();
					requestParameterList = new ArrayList<>();
					
					MethodCount++;
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
	
	public static void DownloadFiles(String path, String fileName) {
		URL url;
		
		if(new File("C:\\GoogleAPIs\\" + fileName + ".json").exists()) {
			fileName += "_" + new java.text.SimpleDateFormat("HHmmss").format(new Date());
		}
		
		File newFile = new File("C:\\GoogleAPIs\\" + fileName + ".json");

		try {
			url = new URL(path);
			FileUtils.copyURLToFile(url, newFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
