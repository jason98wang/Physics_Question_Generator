class SimpleLinkedList<E> {
	private Node<E> head;
	private Node<E> tail;

	public void add(E item) {
		Node<E> node = new Node<E>(item, null);
		if (tail != null) {
			tail.setNext(node);
			tail = node;
		} else {
			head = node;
			tail = node;
		}
	}

	public E get(int index) {
		Node<E> tempNode = head;

		int counter = 0;
		while (tempNode != null) {

			if (counter == index) {
				return tempNode.getItem();
			}

			tempNode = tempNode.getNext();
			counter++;
		}

		return null;
	}

	public int indexOf(E item) {
		Node<E> tempNode = head;
		int counter = 0;

		while (tempNode != null) {

			if (tempNode.getItem().equals(item)) {
				return counter;
			}

			tempNode = tempNode.getNext();

			counter++;
		}

		return -1;
	}

	public void remove(int index) {

		Node<E> tempNode = head;
		if (index == 0) {
			head = head.getNext();
		} else {
			for (int i = 0; i < index - 1; i++) {
				tempNode = tempNode.getNext();
			}
			tempNode.setNext(tempNode.getNext().getNext());
		}
	}

	public boolean remove(E item) {
		if (head == null) {
			return false;
		} else if (head.getItem().equals(item)) {
			head = head.getNext();
			return true;
		}

		Node<E> cur = head;
		while (cur.getNext() != null) {
			if (cur.getNext().getItem().equals(item)) {
				Node<E> n = cur.getNext();
				cur.setNext(n.getNext());

				return true;
			}
			cur = cur.getNext();
		}
		return false;
	}

	public void clear() {
		head = null;
	}

	public int size() {

		Node<E> tempNode = head;

		int counter = 0;

		while (tempNode != null) {
			counter++;
			tempNode = tempNode.getNext();
		}

		return counter;
	}

	public void display() {
		Node<E> tempNode = head;
		while (tempNode != null) {
			System.out.println(tempNode.getItem());
			tempNode = tempNode.getNext();
		}
	}

}
