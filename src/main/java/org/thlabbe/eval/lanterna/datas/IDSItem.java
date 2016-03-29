package org.thlabbe.eval.lanterna.datas;

import java.util.List;

public interface IDSItem {
	final static String PATH_SEPARATOR = "/";
	public String getLabel();
	IDSItem obtainsDSItem(String path, String name);

	String getName();

	String getRoot();

	void setRoot(String root);

	String getPath();

	void setName(String name);

	List<DSItem> getContent();

	boolean addContent(DSItem item);

	boolean equals(Object obj);

	int hashCode();

	String toString();

	int getDepth();

	void setDepth(int depth);

}