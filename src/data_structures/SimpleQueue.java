package data_structures;
/*
 * [SimpleQueue.java]
 * Simple Queue data structure
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Nov. 20, 2018
 */
public class SimpleQueue<T> {

    private Node<T> head = null;
    private Node<T> tail = null;

    public T dequeue() {
        if (head == null) {
            return null;
        } else {
            Node<T> tempNode = head;
            head = head.getNext();
            return tempNode.getItem();
        }
    }

    public void enqueue(T item) {
        if (tail == null) {
            head = new Node<T>(item, null);
            tail = head;
        } else {
            Node<T> tempNode = new Node<T>(item, null);
            tail.setNext(tempNode);
            tail = tempNode;
        }
    }

    public T peek() {
    	if (head == null) return null;
        return head.getItem();
    }

    public void clear() {
        head = null;
        tail = null;
    }

}
