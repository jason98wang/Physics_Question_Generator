package data_structures;

/*
 * [SimpleLinkedList.java]
 * Simple Linked List data structure
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Nov. 20, 2018
 */
public class SimpleLinkedList<E> {
	private Node<E> head; //Head node (start)
	private Node<E> tail; //Tail node (end)

	/**
	 * add
	 * this method adds items in to the simple linked list
	 * @param item, item of type E that is being added to the list
	 */
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

	/**
	 * addToFront
	 * this method adds items to the front of the simple linked list
	 * @param item, item of type E that is being added to the list
	 */
	public void addToFront(E item) {
		head = new Node<E>(item, head); //setting the new node as head and head after it 
	}

	/**
	 * get
	 * this method returns the item in the specific location of the linked list
	 * @param index, the index of the item being returned
	 * @return item, the item that is in the specific index of the list
	 */
	public E get(int index) {
		Node<E> tempNode = head;
		int counter = 0;
		while (tempNode != null) {
			if (counter == index) {
				return tempNode.getItem(); //return once the counter hits the specifed index
			}
			tempNode = tempNode.getNext();
			counter++;
		}

		return null;
	}

	/**
	 * indexOf
	 * This method returns the index of the item that is given
	 * @param item, the item that the user would like the find the index of 
	 * @return counter, the index of the item, -1 if not found
	 */
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

	/**
	 * remove
	 * This method removes the item in the given index
	 * @param index, the index of the item that you would like the remove
	 */
	public void remove(int index) {

		Node<E> tempNode = head;
		if (index == 0) {
			head = head.getNext();
		} else {
			for (int i = 0; i < index - 1; i++) {
				tempNode = tempNode.getNext();
			}
			if (tail == tempNode.getNext())
				tail = tempNode;
			tempNode.setNext(tempNode.getNext().getNext());
		}
		if (head == null || tail == null) {
			tail = head;
		}
	}

	/**
	 * remove
	 * This method removes the item given the item
	 * @param item, the item that you would like to remove
	 * @param true if removed, false if not removed
	 */
	public boolean remove(E item) {
		if (head == null) {
			return false;  //if empty return false
		} else if (head.getItem().equals(item)) {
			head = head.getNext(); //if its the first item return it
			return true;
		}

		Node<E> cur = head;
		while (cur.getNext() != null) {
			if (cur.getNext().getItem().equals(item)) {
				Node<E> n = cur.getNext(); //keep getting next until found the item
				cur.setNext(n.getNext());
				return true;
			}
			cur = cur.getNext();
		}
		//if item never found return
		return false;
	}

	/**
	 * clear
	 * This method clears the simple linked list
	 */
	public void clear() {
		head = null; //set head to null and break the connection
	}

	/**
	 * size
	 * This method returns the size of the simple linked list
	 * @param counter, the size the the linked list
	 */
	public int size() {
	Node<E> tempNode = head;

		int counter = 0; 

		while (tempNode != null) {
			counter++;
			tempNode = tempNode.getNext();
		}
		return counter;
	}

	/**
	 * display
	 * This method prints the simple linked list
	 */
	public void display() {
		Node<E> tempNode = head;
		while (tempNode != null) {
			System.out.println(tempNode.getItem()); 
			tempNode = tempNode.getNext();
		}
	}

	/**
	 * contain
	 * This method determine if the item is contained in the linked list or not 
	 * @param item, the item that the user checks if it's contain in the linked list or not
	 * @return true, false, return true if contained, return false if not contain
	 */
	public boolean contain(E item) {
		Node<E> tempNode = head;
		for (int i = 0; i < size(); i++) {
			if (tempNode.getItem().equals(item)) {
				return true; //if the item is found in the list return true
			}
			tempNode = tempNode.getNext();
		}
		return false;

	}

}
