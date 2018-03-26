package derek.aut.ParsingGoogleAPIs.dto;

public class Method {
	private String api;
	private String method;
	private String description;
	private String responseType;
	private ReqeustParameter requestParameter;

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

	public ReqeustParameter getRequestParameter() {
		return requestParameter;
	}

	public void setRequestParameter(ReqeustParameter requestParameter) {
		this.requestParameter = requestParameter;
	}

}
