package derek.aut.project.dto;

import derek.aut.project.util.Utils;

public class RequestParameter {
	private String param;
	private String description;

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = Utils.dataProcessing(param, true);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = Utils.dataProcessing(description, false);
	}

	@Override
	public String toString() {
		return "RequestParameter [param=" + param + ", description=" + description + "]";
	}

}
