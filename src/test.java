
public class test {

	public static void main(String[] args) {
		SimpleLinkedList<Integer> test = new SimpleLinkedList<Integer>() ;
		
		
		test.add(1);
		test.add(2);
		test.add(5);
		test.add(3);
		test.add(4);
		test.add(5);
		
		test.display();

		SimpleLinkedList<Symbol> formula = new SimpleLinkedList<>();

		formula.add(new Variable("2"));
		formula.add(new Operation("+"));
        formula.add(new Variable("2"));
        formula.add(new Operation("/"));
        formula.add(new Variable("3"));

		Question question = new Question("Hello", formula);

		System.out.println(test.contain(1));

	}

}
