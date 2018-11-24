public class Stack<T> {

    private Node<T> head = null;

    public Stack() {
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

}