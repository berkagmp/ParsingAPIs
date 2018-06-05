package derek.aut.project.dto;

public class GoogleAPI {

	private String kind;
	private String discoveryVersion;
	private Item[] items;

	public GoogleAPI(String kind, String discoveryVersion, Item[] itemList) {
		super();
		this.kind = kind;
		this.discoveryVersion = discoveryVersion;
		this.items = itemList;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getDiscoveryVersion() {
		return discoveryVersion;
	}

	public void setDiscoveryVersion(String discoveryVersion) {
		this.discoveryVersion = discoveryVersion;
	}

	public Item[] getItems() {
		return items;
	}

	public void setItems(Item[] itemList) {
		this.items = itemList;
	}

}
