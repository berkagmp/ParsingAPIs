package derek.aut.project.dto;

import java.util.List;

import derek.aut.project.util.Utils;

public class Method {
	private String api;
	private String method;
	private String methodRealname;
	private String description;
	private String httpMethod;
	private String type;

	private List<RequestParameter> requestParameterList;

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api.toLowerCase();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		method = method.replaceAll("POST", "Post")
				.replaceAll("GET", " get")
				.replaceAll("PUT", " put")
				.replaceAll("URL", " url")
				.replaceAll("POST", " post")
				.replaceAll("DELETE", " delete")
				.replaceAll("MMS", " mms")
				.replaceAll("SMS", " sms")
				.replaceAll("PROJECT", " project")
				.replaceAll("ID", " id")
				.replaceAll("XML", " xml")
				.replaceAll("JSON", " json")
				.replaceAll("SSH", " ssh")
				.replaceAll("UPC", " upc")
				.replaceAll("ISRC", " isrc")
				.replaceAll("IP", " ip")
				.replaceAll("AP", " ap")
				.replaceAll("API", " api")
				.replaceAll("PIN", " pin");
		
		this.method = Utils.dataProcessing(method, true);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = Utils.dataProcessing(description, false);
	}

	public String getMethodRealname() {
		return methodRealname;
	}

	public void setMethodRealname(String methodRealname) {
		this.methodRealname = methodRealname;
	}

	public List<RequestParameter> getRequestParameterList() {
		return requestParameterList;
	}

	public void setRequestParameterList(List<RequestParameter> requestParameterList) {
		this.requestParameterList = requestParameterList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	@Override
	public String toString() {
		return "Method [api=" + api + ", method=" + method + ", methodRealname=" + methodRealname + ", description="
				+ description + ", httpMethod=" + httpMethod + ", type=" + type + ", requestParameterList="
				+ requestParameterList + "]";
	}

}
