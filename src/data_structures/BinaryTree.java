package data_structures;

public class BinaryTree<E extends Comparable<E>> {

	private TreeNode<E> root;

	public void add(E item) {
		TreeNode<E> n = new TreeNode<E>(item);
		if (root == null) {
			root = n;
		} else {
			TreeNode<E> cur = root;

			boolean added = false;
			while (!added) {
				if (cur.compareTo(n) == 0) {
					added = true;
				} else if (cur.compareTo(n) > 0) {
					if (cur.getLeft() == null) {
						cur.setLeft(n);
						added = true;
					} else {
						cur = cur.getLeft();
					}
				} else {
					if (cur.getRight() == null) {
						cur.setRight(n);
						added = true;
					} else {
						cur = cur.getRight();
					}
				}
			}
		}
	}

	public boolean remove(E item) {
		TreeNode<E> prev = null;
		TreeNode<E> rm = root;
		boolean last = false;

		boolean found = false;
		while (!found && rm != null) {
			if (rm.getItem().compareTo(item) == 0) {
				found = true;
			} else {
				prev = rm;
				if (rm.getItem().compareTo(item) > 0) {
					rm = rm.getLeft();
					last = false;
				} else {
					rm = rm.getRight();
					last = true;
				}
			}
		}

		if (found) {
			if (rm.isLeaf()) {
				System.out.println("is leaf");
				prev.setItem(rm.getItem());
				if (last) {
					prev.setRight(null);
				} else {
					prev.setLeft(null);
				}
			} else {
				prev = rm;
				if (rm.getLeft() == null) {
					rm = rm.getRight();
					if (rm.isLeaf()) {
						prev.setItem(rm.getItem());
						prev.setRight(null);
					} else if (rm.getLeft() == null) {
						prev.setItem(rm.getItem());
						prev.setRight(rm.getRight());
					} else {
						while (!rm.getLeft().isLeaf()) {
							rm = rm.getLeft();
						}
						prev.setItem(rm.getLeft().getItem());
						rm.setLeft(null);
					}

				} else {
					rm = rm.getLeft();
					if (rm.isLeaf()) {
						prev.setItem(rm.getItem());
						prev.setLeft(null);
					} else if (rm.getRight() == null) {
						prev.setItem(rm.getItem());
						prev.setLeft(rm.getLeft());
					} else {
						while (!rm.getRight().isLeaf()) {
							rm = rm.getRight();
						}
						prev.setItem(rm.getRight().getItem());
						rm.setRight(null);
					}
				}
			}
			return true;
		}

		return false;
	}

	public TreeNode<E> getRoot() {
		return root;
	}

	// public inner class TreeNode
	public class TreeNode<T extends Comparable<T>> {
		private T item;
		private TreeNode<T> left;
		private TreeNode<T> right;

		TreeNode(T item) {
			this.item = item;
		}

		public TreeNode<T> getLeft(){
			return this.left;
		}

		public TreeNode<T> getRight(){
			return this.right;
		}

		public void setLeft(TreeNode<T> left){
			this.left = left;
		}

		public void setRight(TreeNode<T> right){
			this.right = right;
		}

		public T getItem(){
			return this.item;
		}

		public void setItem(T item){
			this.item = item;
		}

		public boolean isLeaf() {
			if (this.getLeft() == null && this.getRight() == null) {
				return true;
			}
			return false;
		}

		public int compareTo(TreeNode<T> o) {
			return this.getItem().compareTo(o.getItem());
		}
	}
}
