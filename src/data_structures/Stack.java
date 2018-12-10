package data_structures;
/*
 * [Stack.java]
 * Stack data structure
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Nov. 20, 2018
 */
public class Stack<T> {

    private Node<T> head;
    
	/**
	 * Stack
	 * this constructor is used to declare head as null
	 */
    public Stack() {
        head = null;
    }

	/**
	 * push
	 * this method is used to push an item to the stack
	 * @param item, adding the item to the top of the stack
	 */
    public void push(T item) {
        head = new Node<T>(item, head);
    }

	/**
	 * pop
	 * this method is used to pop the top item from a stack
	 * @return item, returns the item from the top of the list that got popped
	 */
    public T pop() {
        if (head == null) {
            return null;
        } else {
            Node<T> tempNode = head;
            head = head.getNext();
            return tempNode.getItem();
        }
    }

	/**
	 * peek
	 * this method is used to return the top of the stack
	 * @return item, returns the item from the top of the list without popping it
	 */
    public T peek() {
        if (head == null) {
            return null;
        } else {
            return head.getItem();
        }
    }

	/**
	 * clear
	 * this method is used to clear the stack
	 */
    public void clear() {
        head = null;
    }

}