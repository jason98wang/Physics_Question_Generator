package data_structures;
/*
 * [Stack.java]
 * Stack data structure
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Nov. 20, 2018
 */
public class Stack<T> {

    private Node<T> head;

    public Stack() {
        head = null;
    }

    public void push(T item) {
        head = new Node<T>(item, head);
    }

    public T pop() {
        if (head == null) {
            return null;
        } else {
            Node<T> tempNode = head;
            head = head.getNext();
            return tempNode.getItem();
        }
    }

    public T peek() {
        if (head == null) {
            return null;
        } else {
            return head.getItem();
        }
    }

    public void clear() {
        head = null;
    }

}