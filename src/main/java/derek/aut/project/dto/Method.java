package derek.aut.project.dto;

import java.util.List;

public class Method {
	private String api;
	private String method;
	private String methodRealname;
	private String description;

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
		this.method = method.toLowerCase();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.toLowerCase();
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

	@Override
	public String toString() {
		return "Method [api=" + api + ", method=" + method + ", methodRealname=" + methodRealname + ", description="
				+ description + ", requestParameterList=" + requestParameterList + "]";
	}

}
