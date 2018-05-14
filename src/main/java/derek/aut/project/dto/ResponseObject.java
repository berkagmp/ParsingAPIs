package derek.aut.project.dto;

import java.util.List;

public class ResponseObject {
	private String api;
	private String objectName;
	private List<ResponseProperty> responsePropertyList;

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public List<ResponseProperty> getResponsePropertyList() {
		return responsePropertyList;
	}

	public void setResponsePropertyList(List<ResponseProperty> responsePropertyList) {
		this.responsePropertyList = responsePropertyList;
	}

	@Override
	public String toString() {
		return "ResponseObject [api=" + api + ", objectName=" + objectName + ", responsePropertyList="
				+ responsePropertyList + "]";
	}

}
