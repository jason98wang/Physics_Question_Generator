class SimpleLinkedList<E> {
	private Node<E> head;

	public void add(E item) {
		Node<E> tempNode = head;

		if (head == null) {
			head = new Node<E>(item, null);
			return;
		}

		while (tempNode.getNext() != null) {
			tempNode = tempNode.getNext();
		}

		tempNode.setNext(new Node<E>(item, null));
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
