package derek.aut.ParsingGoogleAPIs.dto;

import java.util.List;

public class Method {
	private String api;
	private String method;
	private String description;
	private String responseType;
	private List<RequestParameter> requestParameterList;

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public List<RequestParameter> getRequestParameterList() {
		return requestParameterList;
	}

	public void setRequestParameterList(List<RequestParameter> requestParameterList) {
		this.requestParameterList = requestParameterList;
	}

	@Override
	public String toString() {
		String str = "Method [api=" + api + ", method=" + method + ", description=" + description + ", responseType="
				+ responseType + "]\r\n";

		if (requestParameterList != null) {
			str += "Param size - " + requestParameterList.size() + "\r\n";
			for (RequestParameter rp : requestParameterList) {
				str += rp.toString() + "\r\n";
			}
		}

		return str;
	}

}
