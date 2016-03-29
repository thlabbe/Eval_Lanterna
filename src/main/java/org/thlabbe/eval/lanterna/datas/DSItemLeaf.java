package org.thlabbe.eval.lanterna.datas;

public class DSItemLeaf extends DSItem {

	private String timestamp = "";

	public DSItemLeaf(String root, String name) {
		super(root, name, 4);
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
