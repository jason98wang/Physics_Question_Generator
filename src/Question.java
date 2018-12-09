import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import data_structures.SimpleLinkedList;
import data_structures.SimpleQueue;
import data_structures.Stack;

public class Question {

	// init vars
	private String problemStatement;
	private SimpleLinkedList<Symbol> formula;
	private Double answer = 0.0;

	private SimpleLinkedList<String> specificQuestions;
	private SimpleLinkedList<String> specificAnswers;
	private SimpleLinkedList<String> possibleAnswers;

	private BufferedImage image;

	private boolean preset;
	private boolean numerical;

	//Stacks (for shunting yard alg)
	private Stack<Operation> operators;
	private SimpleQueue<Symbol> output;

	// constructor
	Question(String problemStatement, SimpleLinkedList<Symbol> formula){
		this.problemStatement = problemStatement;
		this.formula = formula;
		operators = new Stack<>();
		output = new SimpleQueue<>();
		numerical = true;
	}

	Question(String problemStatement, SimpleLinkedList<Symbol> formula, BufferedImage image){
		this.image = image;
		this.problemStatement = problemStatement;
		this.formula = formula;
		operators = new Stack<>();
		output = new SimpleQueue<>();
		numerical = true;
	}

	Question(String problemStatement, SimpleLinkedList<String> specificQuestions, SimpleLinkedList<String> specificAnswers, SimpleLinkedList<String> possibleAnswers) {
		this.specificQuestions = specificQuestions;
		this.specificAnswers = specificAnswers;
		this.possibleAnswers = possibleAnswers;
		preset = true;
	}

	Question(String problemStatement, SimpleLinkedList<String> specificQuestions, SimpleLinkedList<String> specificAnswers, SimpleLinkedList<String> possibleAnswers, BufferedImage image) {
		this.problemStatement = problemStatement;
		this.specificQuestions = specificQuestions;
		this.specificAnswers = specificAnswers;
		this.possibleAnswers = possibleAnswers;
		this.image = image;
		preset = true;
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

	public BufferedImage getImage() {
		return image;
	}

	public SimpleLinkedList<String> getSpecificQuestions() {
		return specificQuestions;
	}

	public SimpleLinkedList<String> getSpecificAnswers() {
		return specificAnswers;
	}

	public SimpleLinkedList<String> getPossibleAnswers() {
		return possibleAnswers;
	}

	//Returns in format (for each row): Question, Answer, FalseAnswer1, FalseAnswer2, FalseAnswer3
	//Make sure that the max value of 'num' entered is the num of specific questions (prevent doubling)
	public String[][] getStringQuestions(int num) {
		String[][] questions = new String[num][5];
		int rand;
		boolean repeatedQuestion;
		
		for (int i = 0; i < num; i++) {
			do {
				repeatedQuestion = false;
				rand = (int)(Math.round(Math.random()*(specificQuestions.size()-1)));
				for (int j = 0; j < specificQuestions.size(); j++) {
					if (rand != j && specificQuestions.get(j).equals(specificQuestions.get(rand))) {
						repeatedQuestion = true;
					}
				}
			} while (repeatedQuestion);
			questions[i][0] = specificQuestions.get(rand);
			questions[i][1] = specificAnswers.get(rand);
			if (possibleAnswers.size() == 1) {
				questions[i][2] = possibleAnswers.get(0);
				questions[i][3] = possibleAnswers.get(0);
				questions[i][4] = possibleAnswers.get(0);
			} else if (possibleAnswers.size() == 2) {
				questions[i][2] = possibleAnswers.get(0);
				questions[i][3] = possibleAnswers.get(1);
				questions[i][4] = possibleAnswers.get(0);
			} else if (possibleAnswers.size() == 3) {
				questions[i][2] = possibleAnswers.get(0);
				questions[i][3] = possibleAnswers.get(1);
				questions[i][4] = possibleAnswers.get(2);
			} else { //Randomize all possibleChoices
				boolean repeatedChoice;
				for (int j = 2; j < 5; j++) {
					do {
						repeatedChoice = false;
						rand = (int)(Math.round(Math.random()*(possibleAnswers.size()-1)));
						for (int k = 2; k < 5; k++) {
							if (questions[i][k] != null) {
								if (questions[i][k].equals(possibleAnswers.get(rand))) {
									repeatedChoice = true;
								}
							}
						}
						if ((possibleAnswers.get(rand)).equals(questions[i][1])) {
							repeatedChoice = true;
						}
					} while (repeatedChoice);
					questions[i][j] = possibleAnswers.get(rand);
				}
			}
			
		}
		return questions;
	}

	public boolean isPreset() {
		return preset;
	}

	public boolean isNumerical() {
		return numerical;
	}

	public int numSpecificQuestions() {
		return specificQuestions.size();
	}

	/**
	 * getAnswer
	 * This method randomizes values of variables in the formula, calculates and returns the answer
	 * @return answer, the double value representing the answer to the question with the randomized variable values
	 */
	public double getAnswer() {
		
		boolean previouslyFound;
		int previouslyFoundAt = 0;
		
		//Randomize values of variables in formula (besides pi, which remains as pi)
		for (int i = 0; i < formula.size(); i++) {
			previouslyFound = false; //Reset flag to false
			if (formula.get(i) instanceof Variable) {
				if (!(((Variable)formula.get(i)).isConstant())) {
					for (int j = 0; j < i; j++) {
						if (formula.get(i).getId().equals(formula.get(j).getId())) {
							previouslyFound = true; //Flag as occurring previously in formula (already randomized)
							previouslyFoundAt = j;
						}
					}
					if (!previouslyFound) {
						((Variable)(formula.get(i))).setValue(Math.random() * 99 + 1); //Randomize value
					} else {
						((Variable)(formula.get(i))).setValue(((Variable)(formula.get(previouslyFoundAt))).getValue());
					}
				}
			}
		}

		//Calculate and return the answer with the randomized values
		toRPN();
		calcAnswer();
		operators.clear(); //Make sure operators stack is cleared
		output.clear(); //Make sure calculation queue is cleared
		return answer;
	}

	/**
	 * getFalseAnswers
	 * This method generates 3 false answers for the randomized values of variables (with the given formula)
	 * @return falseAnswers, an array of 3 doubles representing the 3 incorrect MC answers given
	 */
	public double[] getFalseAnswers() {

		double[] falseAnswers = new double[3];
		double[] randInts = new double[3];
		boolean flag;

		for (int i = 0; i < 3; i++) {
			int j = 0;
			do {
				flag = false;
				j = (int)(Math.random()*9);
				for (int h = 0; h < 3; h ++) {
					if (j == randInts[h]) {
						flag = true;
					}
				}
			} while (flag);
			randInts[i] = j;
			if (j == 0) {
				falseAnswers[i] = answer*2.0;
			} else if (j == 1) {
				falseAnswers[i] = answer/2.0;
			} else if (j == 2) {
				falseAnswers[i] = Math.pow(answer,2.0);
			} else if (j == 3) {
				falseAnswers[i] = Math.sqrt(answer);
			} else if (j == 4) {
				falseAnswers[i] = answer*3.0;
			} else if (j == 5) {
				falseAnswers[i] = answer/3.0;
			} else if (j == 6) {
				falseAnswers[i] = answer/Math.PI;
			} else if (j == 7) {
				falseAnswers[i] = answer*Math.PI;
			} else if (j == 8) {
				falseAnswers[i] = answer*Math.E;
			} else if (j == 9) {
				falseAnswers[i] = answer/Math.E;
			}
		}

		return falseAnswers;
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
							if (operators.peek() == null) {
								break;
							}
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
				} else if (((Operation)(output.peek())).getOperation().equals("mul")) {
					output.dequeue();
					calc.push(calc.pop() * tempVariable);
				} else if (((Operation)(output.peek())).getOperation().equals("div")) {
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
			if ((stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("+")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("-")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("mul")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("/")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("sqrt")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("^")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals("(")) || (stringFormula.substring(1,stringFormula.indexOf(" ",1)).equals(")"))) {
				formula.add(new Operation(stringFormula.substring(1,stringFormula.indexOf(" ",1))));
			} else {
				formula.add(new Variable(stringFormula.substring(1,stringFormula.indexOf(" ",1))));
			}
			toSymbol(stringFormula.substring(stringFormula.indexOf(" ",1)));
		}

	}

	// converts image to string for database to store
	public String imageToString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos);
			byte[] bytes = baos.toByteArray();
			baos.close();
			return Base64.getEncoder().encodeToString(bytes);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		System.out.println("returned null");
		return null;
	}

}
