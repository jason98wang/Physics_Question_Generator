public class Question {

	// init vars
	private String problemStatement;
	private SimpleLinkedList<Symbol> formula;
	private Double answer = 0.0;

	//Stacks (for shunting yard alg)
	private Stack<Operation> operators;
	private SimpleQueue<Symbol> output;

	// constructor
	Question(String problemStatement, SimpleLinkedList<Symbol> formula){
		this.problemStatement = problemStatement;
		this.formula = formula;
		operators = new Stack<>();
		output = new SimpleQueue<>();
		toRPN();
		calcAnswer();
	}

	// setters
	public void setProblemStatement(String problemStatement) {
		this.problemStatement = problemStatement;
	}

	public void setFormula(SimpleLinkedList<Symbol> formula) {
		this.formula = formula;
	}

	// getters
	public String getProblemStatement() {
		return problemStatement;
	}

	public SimpleLinkedList<Symbol> getFormula() {
		return formula;
	}

	public double getAnswer() {
		return answer;
	}

	/**
	 * toRPN
	 * This method converts the symbol list storing the formula into a queue sorted by calculation order
	 *    in postfix notation (RPN)
	 */
	private void toRPN() {

		//Split symbols in list into operator stack and output queue
		for (int i = 0; i < formula.size(); i++) {
			if (formula.get(i) instanceof Variable) {
				output.enqueue(formula.get(i));
			}
			if (formula.get(i) instanceof Operation) {
				if (((Operation)(formula.get(i))).getOperation().equals("sqrt")) {
					operators.push((Operation)formula.get(i));
				} else if ((!(((Operation)(formula.get(i))).getOperation().equals("("))) && !(((Operation)(formula.get(i))).getOperation().equals(")"))) {
				    if (operators.peek() != null) {
                        while (((operators.peek().getOperation().equals("sqrt")) || ((((Operation) (formula.get(i))).getPrecedence() < operators.peek().getPrecedence())) || (((((Operation) (formula.get(i))).getPrecedence() == operators.peek().getPrecedence())) && (((Operation) (formula.get(i))).getOperation() != "^"))) && (operators.peek().getOperation() != "(")) {
                            output.enqueue(operators.pop());
                        }
                    }
					operators.push((Operation)formula.get(i));
				}
				if (((Operation)(formula.get(i))).getOperation().equals("(")) {
					operators.push((Operation)formula.get(i));
				} else if (((Operation)(formula.get(i))).getOperation().equals(")")) {
					while (!operators.peek().getOperation().equals("(")) {
						output.enqueue(operators.pop());
					}
					operators.pop();
					//POSSIBLE FORMAT ISSUE - If brackets do not match, the above loop will pop everything, encounter error
				}
			}
		}

		//Pop rest of operators in operator stack, enqueue them into output queue (in order popped)
		while (operators.peek() != null) {
			output.enqueue(operators.pop());
		}

	}

	/**
	 * calcAnswer
	 * Calculates the answer that corresponds to the formula stored in the question
	 *   after the calculation order has been determined and stored in postfix notation
	 */
	private void calcAnswer() {

		Stack<Double> calc = new Stack<>();
		double tempVariable;

		while (output.peek() != null) {
			if (output.peek() instanceof Variable) {
				calc.push(((Variable)(output.dequeue())).getValue());
			} else {
				tempVariable = calc.pop();
				if (((Operation)(output.peek())).getOperation().equals("+")) {
					output.dequeue();
					calc.push(calc.pop() + tempVariable);
				} else if (((Operation)(output.peek())).getOperation().equals("-")) {
					output.dequeue();
					calc.push(calc.pop() - tempVariable);
				} else if (((Operation)(output.peek())).getOperation().equals("*")) {
					output.dequeue();
					calc.push(calc.pop() * tempVariable);
				} else if (((Operation)(output.peek())).getOperation().equals("/")) {
					output.dequeue();
					calc.push(calc.pop() / tempVariable);
				} else if (((Operation)(output.peek())).getOperation().equals("^")) {
					output.dequeue();
					calc.push(Math.pow(calc.pop(), tempVariable));
				} else if (((Operation)(output.peek())).getOperation().equals("sqrt")) {
					output.dequeue();
					calc.push(Math.sqrt(tempVariable));
				}
			}
		}

		answer = calc.pop();

	}

    /**
     * toString
     * This method converts a SimpleLinkedList of Symbols into a String
     * @return stringFormula, the formula (of symbols) as a String
     */
    public String toString() {

        String stringFormula = "";

        for (int i = 0; i < formula.size(); i++) {
            stringFormula += " ";
            stringFormula += formula.get(i).getId();
        }

        stringFormula += " ";

        return stringFormula;
    }

    /**
     * toSymbol
     * This method converts a String representing a formula into a SimpleLinkedList of Symbols
     * @param stringFormula, the formula (of symbols) as a String
     */
    public void toSymbol(String stringFormula) {

        if (stringFormula.length() > 1) {
            if ((stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("+")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("-")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("*")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("/")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("sqrt")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("^")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("(")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals(")"))) {
                formula.add(new Operation(stringFormula.substring(1,stringFormula.indexOf(" ",1))));
            } else {
                formula.add(new Variable(stringFormula.substring(1,stringFormula.indexOf(" ",1))));
            }
            toSymbol(stringFormula.substring(stringFormula.indexOf(" ",1)));
        }

    }

}