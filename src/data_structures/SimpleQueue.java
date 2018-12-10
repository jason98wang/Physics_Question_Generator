package data_structures;
/**
 * [SimpleQueue.java]
 * Simple Queue data structure
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Nov. 20, 2018
 */
public class SimpleQueue<T> {

    private Node<T> head; //Head node (start)
    private Node<T> tail; //Tail node (end)

    /**
     * SimpleQueue
     * Constructor that creates a queue with no items (blank)
     */
    public SimpleQueue() {
        head = null;
        tail = null;
    } //End of constructor

    /**
     * dequeue
     * Removes the item at the head of the queue and returns it
     * @return the item at the head of the queue
     */
    public T dequeue() {
        if (head == null) {
            return null; //Return null if queue is empty
        } else {
            Node<T> tempNode = head;
            head = head.getNext();
            return tempNode.getItem();
        }
    } //End of dequeue

    /**
     * enqueue
     * Enqueues an item (puts in at the tail of the queue)
     * @param item, the item to be enqueued
     */
    public void enqueue(T item) {
        if (tail == null) {
            head = new Node<T>(item, null); //Set new node as head and tail if queue is empty
            tail = head;
        } else {
            Node<T> tempNode = new Node<T>(item, null);
            tail.setNext(tempNode); //Set new node as tail
            tail = tempNode;
        }
    } //End of enqueue

    /**
     * peek
     * This method returns the item at the front of the queue
     * @return head.getItem(), the item at the head
     */
    public T peek() {
    	if (head == null) {
    	    return null;
        }
        return head.getItem();
    } //End of peek

    /**
     * clear
     * Clears the queue
     */
    public void clear() {
        head = null;
        tail = null;
    } //End of clear

} //End of class
