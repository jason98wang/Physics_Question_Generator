package data_structures;
/*
 * [Node.java]
 * Node Class
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Nov. 20, 2018
 */
public class Node<T>{
	
	private T item;
	private Node<T> next;
	
	/**
	 * Node
	 * This constructor declares the item and set the next to null
	 * @param item, the item that is being added
	 */
	public Node(T item) {
		this.item = item;
		this.next = null;
	}

	/**
	 * Node
	 * This constructor declares the item and set the next to a given node
	 * @param item, the item that is being added
	 * @param next, the node that is connected to 
	 */
	public Node(T item, Node<T> next) {
		this.item = item;
		this.next = next;
	}

	/**
	 * getNext
	 * This method returns the next node connected
	 * @return next, the next node in the list
	 */
	public Node<T> getNext() {
		return this.next;
	}
	
	/**
	 * setNext
	 * This method connects the current node to a next one
	 * @param next, the node that you would like to be connected
	 */
	public void setNext(Node<T> next) {
		this.next = next;
	}

	/**
	 * getItem
	 * This method returns the item within the node
	 * @return item, returns the item in the node
	 */
	public T getItem() {
		return this.item;
	}

}