package derek.aut.project.dto;

public class RequestParameter {
	private String param;
	private String desciption;

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	@Override
	public String toString() {
		return "RequestParameter [param=" + param + ", desciption=" + desciption + "]";
	}

}
