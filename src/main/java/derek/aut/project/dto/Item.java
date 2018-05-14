package derek.aut.project.dto;

public class Item {
	private String kind;
	private String id;
	private String name;
	private String version;
	private String title;
	private String description;
	private String discoveryRestUrl;
	private String documentationLink;
	private String preferred;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiscoveryRestUrl() {
		return discoveryRestUrl;
	}

	public void setDiscoveryRestUrl(String discoveryRestUrl) {
		this.discoveryRestUrl = discoveryRestUrl;
	}

	public String getDocumentationLink() {
		return documentationLink;
	}

	public void setDocumentationLink(String documentationLink) {
		this.documentationLink = documentationLink;
	}

	public String getPreferred() {
		return preferred;
	}

	public void setPreferred(String preferred) {
		this.preferred = preferred;
	}

}
