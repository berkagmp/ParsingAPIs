package derek.aut.project;

import static java.lang.System.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;

public class APIGeeParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		out.println(getFiles());

	}

	public static int getFiles() {
		File file = null;
		File[] dirList = null;

		Document doc;
		SAXBuilder builder = new SAXBuilder();
		
		int totalCnt = 0;

		file = new File("C:\\Users\\berka\\Google Drive\\00-2.AUT\\01.Research Project\\APIGeeAPIs\\test");
		dirList = file.listFiles();

		for (int f = 0; f < dirList.length; f++) {
			if (dirList[f].getName().substring(dirList[f].getName().lastIndexOf(".") + 1).equals("xml")) {
				out.println(dirList[f].getName());

				try {
					doc = builder.build(new FileInputStream(dirList[f].getPath()));

					Iterator<Element> iter = doc.getDescendants(new ElementFilter("method"));
					while(iter.hasNext()) {
						Content c =(Content)iter.next();
						
						if(c instanceof Element) {
							Element e = (Element)c;
							
							List<Attribute> attrList = e.getAttributes();
							out.println();
							for(Attribute attr: attrList) {
								out.println(attr.getName() + ":" + attr.getValue());
							}
							
							Iterator<Element> iterDoc = e.getDescendants(new ElementFilter("doc"));
							while(iterDoc.hasNext()) {
								out.println("desc:" + iterDoc.next().getValue().trim().replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("  ", " "));
							}
							
							Iterator<Element> iterParam = e.getDescendants(new ElementFilter("param"));
							while(iterParam.hasNext()) {
								out.println("param:" +iterParam.next().getAttributeValue("name"));
								
								Iterator<Element> iterParamDoc = e.getDescendants(new ElementFilter("doc"));
								while(iterParamDoc.hasNext()) {
									out.println("desc:" + iterParamDoc.next().getValue().trim().replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("  ", " "));
								}
							}
						}
						
						totalCnt++;
					}					
					
					//Element root = doc.getRootElement();
					// out.println(root.getName());

					// resources
					/*List<Element> rootList = root.getChildren();
					Element resources = null;

					for (Element element : rootList) {
						if (element.getName().equalsIgnoreCase("resources")) {
							resources = element;
						}
					}*/
					// out.println(resources.getName());

					// resource
					/*List<Element> resourceList = resources.getChildren();
					for (Element element : resourceList) {
						if (element.getName().equalsIgnoreCase("resource")) {

							for (Element resourceEle : resourceList) {
								if (resourceEle.getName().equalsIgnoreCase("resource")) {

									for (Element methodEle : resourceEle.getChildren()) {
										if (methodEle.getName().equalsIgnoreCase("method")) {
										 out.println(methodEle.getAttribute("id").getValue());
										 out.println(methodEle.getAttribute("apigee"));
										 out.println(methodEle.getAttributeValue("name"));
										 out.println(methodEle.getChildren("doc").size());
										}
									}
								}
							}

						}
					}*/
					// out.println(resource.getName());
				} catch (JDOMException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return totalCnt;
	}

	public static Element getElement(Element baseElement, String targetName) {
		List<Element> elementList = baseElement.getChildren();

		for (Element element : elementList) {
			if (element.getName().equalsIgnoreCase(targetName)) {
				return element;
			}
		}

		return null;
	}
}
