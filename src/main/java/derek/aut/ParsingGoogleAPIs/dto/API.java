package derek.aut.ParsingGoogleAPIs.dto;

public class API {
	private String methodName;
	private String APIDescription;
	private Method[] method;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getAPIDescription() {
		return APIDescription;
	}

	public void setAPIDescription(String aPIDescription) {
		APIDescription = aPIDescription;
	}

	public Method[] getMethod() {
		return method;
	}

	public void setMethod(Method[] method) {
		this.method = method;
	}
}
