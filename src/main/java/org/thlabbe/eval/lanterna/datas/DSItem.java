package org.thlabbe.eval.lanterna.datas;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public abstract  class DSItem implements IDSItem {

	private String name;
	private String root;
	private List<DSItem> content = new ArrayList<>();
	private int depth = 0;

	public DSItem(String root, String name, int i) {
		this.root = root;
		this.name = name;
		this.depth = i;
	}

	@Override
	public String getLabel() {
		return getClass().getSimpleName().substring(2);
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#obtainsDSItem(java.lang.String, java.lang.String)
	 */
	@Override
	public IDSItem obtainsDSItem(String path, String name) {
		// if the asked DSItem already exists returns a reference to it
		// else return a new intance
		IDSItem tmp = null ; //new DSItem(path, name, this.depth + 1);
		switch (this.depth) {
		case 0:
				tmp = new DSDate(path, name);
			break;
		case 1:
			tmp = new DSJob(path, name);
		break;
		case 2:
			tmp = new DSRegion(path, name);
		break;

		default:
			tmp = new DSItemLeaf(path, name);
		}
		for (IDSItem i : this.content) {
			if (i.equals(tmp)) {
				return i;
			}
		}
		return tmp;
	}
	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#getRoot()
	 */
	@Override
	public String getRoot() {
		return this.root;
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#setRoot(java.lang.String)
	 */
	@Override
	public void setRoot(String root) {
		this.root = root;
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#getPath()
	 */
	@Override
	public String getPath() {
		return this.root + PATH_SEPARATOR + this.name;
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#getContent()
	 */
	@Override
	public List<DSItem> getContent() {
		return this.content;
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#addContent(org.thlabbe.eval.lanterna.datas.DSItem)
	 */
	@Override
	public boolean addContent(DSItem item) {
		if (item.getDepth() != this.depth + 1) {
			return false;
		}
		boolean found = false;
		for (IDSItem i : this.content) {
			if (found || i.equals(item)) {
				found = true;
				return false;
			}
		}
		return content.add(item);
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#equals(java.lang.Object)
	 */
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DSItem other = (DSItem) obj;
		return Objects.equal(this.root, other.root) && Objects.equal(this.name, other.name)
				&& Objects.equal(this.depth, other.depth);
	};

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.root, this.name, this.depth);
	};

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("root", root)
				.add("name",name)
				.add("content",content != null ? content.size() : 0)
				.toString();
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#getDepth()
	 */
	@Override
	public int getDepth() {
		return depth;
	}

	/* (non-Javadoc)
	 * @see org.thlabbe.eval.lanterna.datas.IDSItem#setDepth(int)
	 */
	@Override
	public void setDepth(int depth) {
		this.depth = depth;
	}

}
