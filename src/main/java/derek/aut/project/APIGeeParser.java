package derek.aut.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;

import derek.aut.project.dao.ItemDao;
import derek.aut.project.dao.ItemDaoFactory;
import derek.aut.project.dto.Method;
import derek.aut.project.dto.RequestParameter;

public class APIGeeParser {
	private static final String dirPath = "C:\\APIGeeAPIs\\";

	public static void main(String[] args) {
		List<Method> methodList = getFiles();

		methodList.forEach(System.out::println);

		ItemDao itemDao = new ItemDaoFactory().userDao();
		itemDao.insertApiMethodAndReqeustParameter(methodList);
	}

	public static List<Method> getFiles() {
		File file = null;
		File[] dirList = null;

		Document doc;
		SAXBuilder builder = new SAXBuilder();

		file = new File(dirPath);
		dirList = file.listFiles();

		Method method = null;
		List<Method> methodList = new ArrayList<>();
		RequestParameter requestParameter = null;
		List<RequestParameter> requestParameterList = null;

		for (int f = 0; f < dirList.length; f++) {
			if (dirList[f].getName().substring(dirList[f].getName().lastIndexOf(".") + 1).equals("xml")) {
				System.out.println(dirList[f].getName());

				try {
					doc = builder.build(new FileInputStream(dirList[f].getPath()));

					Iterator<Element> iter = doc.getDescendants(new ElementFilter("method"));
					while (iter.hasNext()) {
						method = new Method();
						requestParameterList = new ArrayList<>();

						method.setApi(dirList[f].getName().substring(0, dirList[f].getName().lastIndexOf(".")));

						Content c = (Content) iter.next();

						if (c instanceof Element) {
							Element e = (Element) c;

							method.setType("a");

							List<Attribute> attrList = e.getAttributes();
							for (Attribute attr : attrList) {
								if (attr.getName().equals("displayName")) {
									method.setMethod(attr.getValue());
								} else if (attr.getName().equals("id")) {
									method.setMethodRealname(attr.getValue());
								} else if (attr.getName().equals("name")) {
									method.setHttpMethod(attr.getValue());
								}
							}

							List<Element> iterDoc = e.getChildren();
							for (Element ele : iterDoc) {
								if (ele.getName().toString().equalsIgnoreCase(("doc"))) {
									method.setDescription(ele.getValue().toString());

								}
							}

							Iterator<Element> iterParam = e.getDescendants(new ElementFilter("param"));
							while (iterParam.hasNext()) {
								requestParameter = new RequestParameter();
								requestParameter.setParam(iterParam.next().getAttributeValue("name"));

								Iterator<Element> iterParamDoc = e.getDescendants(new ElementFilter("doc"));
								while (iterParamDoc.hasNext()) {
									requestParameter.setDescription(iterParamDoc.next().getValue());
								}

								requestParameterList.add(requestParameter);
							}

							method.setRequestParameterList(requestParameterList);
						}

						methodList.add(method);
					}

				} catch (JDOMException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return methodList;
	}

}
