/**
 * [Question.java]
 * This class represents a question object, which also holds a formula or preset answers
 * Authors:
 * Date:
 */

//java imports
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

//Data structure imports
import data_structures.SimpleLinkedList;
import data_structures.SimpleQueue;
import data_structures.Stack;

public class Question {

	//Init vars
	private String problemStatement; //General problem statement (situation outline)
	private SimpleLinkedList<Symbol> formula; //List of symbol objects representing the formula (if numerical)
	private Double answer = 0.0; //Answer to the question (if numerical) - Rounded to 2 decimal places

    //Lists for preset questions (to store sub-questions, answers, and choices for MC)
	private SimpleLinkedList<String> specificQuestions;
	private SimpleLinkedList<String> specificAnswers;
	private SimpleLinkedList<String> possibleAnswers;

	//Image related to the question (possibly displaying choices or the situation)
	private BufferedImage image;

	//Flags to determine whether the question is preset (choices held inside) or needs the answer calculated
	private boolean preset;
	private boolean numerical;

	//Stacks (for formula calculation)
	private Stack<Operation> operators;
	private SimpleQueue<Symbol> output;

    /**
     * Question
     * This method creates a question object with a given problem statement and formula
     * @param problemStatement, a String representing the Question's problem statement
     * @param formula, A linked list of Symbols representing the formula used to calculate the answer to the problem
     */
	Question(String problemStatement, SimpleLinkedList<Symbol> formula){
		this.problemStatement = problemStatement;
		this.formula = formula;
		operators = new Stack<>(); //Initialize stack used to calc answer
		output = new SimpleQueue<>(); //Initialize queue used to calc answer
		numerical = true; //Flag as numerical question
	}

    /**
     * Question
     * This method creates a question object with a given problem statement, formula, and image
     * @param problemStatement, a String representing the Question's problem statement
     * @param formula, A linked list of Symbols representing the formula used to calculate the answer to the problem
     * @param image, A BufferedImage representing the question's accompanying image
     */
	Question(String problemStatement, SimpleLinkedList<Symbol> formula, BufferedImage image){
		this.image = image;
		this.problemStatement = problemStatement;
		this.formula = formula;
		operators = new Stack<>(); //Initialize stack used to calc answer
		output = new SimpleQueue<>(); //Initialize queue used to calc answer
		numerical = true;//Flag as numerical question
	}

    /**
     * Question
     * This method creates a Question object with a given problem statement, list of specific questions,
     *    list of answers to those questions, and a list of possible answers (for MC choices)
     * @param problemStatement, the question's problem statement
     * @param specificQuestions, the sub-questions stored in the general question
     * @param specificAnswers, the answers to the sub-questions
     * @param possibleAnswers, the overall possibilities that serve as possible valid answers to sub-questions
     */
	Question(String problemStatement, SimpleLinkedList<String> specificQuestions, SimpleLinkedList<String> specificAnswers, SimpleLinkedList<String> possibleAnswers) {
		this.specificQuestions = specificQuestions;
		this.specificAnswers = specificAnswers;
		this.possibleAnswers = possibleAnswers;
		preset = true; //Flag as preset question
	}

    /**
     * Question
     * This method creates a Question object with a given problem statement, list of specific questions,
     *    list of answers to those questions, a list of possible answers (for MC choices), and an image
     * @param problemStatement, the question's problem statement
     * @param specificQuestions, the sub-questions stored in the general question
     * @param specificAnswers, the answers to the sub-questions
     * @param possibleAnswers, the overall possibilities that serve as possible valid answers to sub-questions
     * @param image, the BufferedImage accompanying the question
     */
	Question(String problemStatement, SimpleLinkedList<String> specificQuestions, SimpleLinkedList<String> specificAnswers, SimpleLinkedList<String> possibleAnswers, BufferedImage image) {
		this.problemStatement = problemStatement;
		this.specificQuestions = specificQuestions;
		this.specificAnswers = specificAnswers;
		this.possibleAnswers = possibleAnswers;
		this.image = image;
		preset = true; //Flag as preset question
	}

    /**
     * setProblemStatement
     * This method sets the problem statement of the question
     * @param problemStatement, the String representing the problem statement to be set
     */
	public void setProblemStatement(String problemStatement) {
		this.problemStatement = problemStatement;
	}

    /**
     * setFormula
     * This method sets the formula of the question
     * @param formula, the SimpleLinkedList of symbols representing the formula to be set
     */
	public void setFormula(SimpleLinkedList<Symbol> formula) {
		this.formula = formula;
	}

    /**
     * getProblemStatement
     * This method returns the problem statement
     * @return problemStatement, a string representing the question's problem statement
     */
	public String getProblemStatement() {
		return problemStatement;
	}

    /**
     * getFormula
     * This method returns the question's formula
     * @return formula, a SimpleLinkedList of Symbol objects representing the formula used to calculate the answer
     */
	public SimpleLinkedList<Symbol> getFormula() {
		return formula;
	}

    /**
     * getImage
     * This method returns the question's image
     * @return image, a BufferedImage representing the image held in the question
     */
	public BufferedImage getImage() {
		return image;
	}

    /**
     * getSpecificQuestion
     * This method returns the sub-questions held in the general question
     * @return specificQuestions, a SimpleLinkedList of Strings representing the sub-questions
     */
	public SimpleLinkedList<String> getSpecificQuestions() {
		return specificQuestions;
	}

    /**
     * getSpecificAnswers
     * This method returns the answers to the sub-questions
     * @return specificAnswers, a SimpleLinkedList of Strings representing the answers
     */
	public SimpleLinkedList<String> getSpecificAnswers() {
		return specificAnswers;
	}

    /**
     * getPossibleAnswers
     * This method returns the possible answers to any given sub-question
     * @return possibleAnswers, a SimpleLinkedList of String representing all possible answers
     */
	public SimpleLinkedList<String> getPossibleAnswers() {
		return possibleAnswers;
	}

    /**
     * getStringQuestions
     * This method returns a given number of preset sub-questions, along with the answers and false choices (3 each)
     * @param num, An integer representing the number of questions, with answers and false choices, to be returned
     * @return questions, A 2D array of strings representing the sub-questions, answers, and false choices
     */
	//Returns in format (for each row): Question, Answer, FalseAnswer1, FalseAnswer2, FalseAnswer3
	public String[][] getStringQuestions(int num) {
		String[][] questions = new String[num][5];
		int rand; //Random integer to be generated
		boolean repeatedQuestion; //Flag to mark whether the question has already been randomly selected

		for (int i = 0; i < num; i++) {
			do {
				repeatedQuestion = false;
				rand = (int)(Math.round(Math.random()*(specificQuestions.size()-1)));
				for (int j = 0; j < i; j++) {
					if (questions[j][0].equals(specificQuestions.get(rand))) {
						repeatedQuestion = true;
					}
				}
			} while (repeatedQuestion);
			questions[i][0] = specificQuestions.get(rand);
			questions[i][1] = specificAnswers.get(rand);
			if (possibleAnswers.size() < 4) {
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
				}
			} else {
				boolean[] repeated = new boolean[possibleAnswers.size()];
				repeated[rand] = true;
				for (int j = 2; j < 5; j++) {
					while (repeated[rand]) {
						rand = (int)(Math.random()*(possibleAnswers.size()));
					}
					repeated[rand] = true;
					questions[i][j] = possibleAnswers.get(rand);
				}
			}
		}
		return questions;
	}

    /**
     * isPreset
     * This method returns whether the question is a preset question
     * @return preset, a boolean representing whether the question is preset
     */
	public boolean isPreset() {
		return preset;
	}

    /**
     * isNumerical
     * This method returns whether the question is numerical (has a calculated answer)
     * @return numerical, a boolean representing whether the question is numerical
     */
	public boolean isNumerical() {
		return numerical;
	}

    /**
     * numSpecificQuestions
     * This method returns the number of sub-questions held in the general question
     * @return specificQuestions.size, the size of the sub-Question SimpleLinkedList
     */
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
				falseAnswers[i] = Math.round(answer*2.0*100.0)/100.0;
			} else if (j == 1) {
				falseAnswers[i] = Math.round(answer/2.0*100.0)/100.0;
			} else if (j == 2) {
				falseAnswers[i] = Math.round(Math.pow(answer,2.0)*100.0)/100.0;
			} else if (j == 3) {
				falseAnswers[i] = Math.round(Math.sqrt(answer)*100.0)/100.0;
			} else if (j == 4) {
				falseAnswers[i] = Math.round(answer*3.0*100.0)/100.0;
			} else if (j == 5) {
				falseAnswers[i] = Math.round(answer/3.0*100.0)/100.0;
			} else if (j == 6) {
				falseAnswers[i] = Math.round(answer/Math.PI*100.0)/100.0;
			} else if (j == 7) {
				falseAnswers[i] = Math.round(answer*Math.PI*100.0)/100.0;
			} else if (j == 8) {
				falseAnswers[i] = Math.round(answer*Math.E*100.0)/100.0;
			} else if (j == 9) {
				falseAnswers[i] = Math.round(answer/Math.E*100.0)/100.0;
			}
		}

		return falseAnswers;
	}

	/*
	 * toRPN
	 * This method converts the symbol list storing the formula into a queue sorted by calculation order
	 *    in reverse polish notation
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

	/*
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

		answer = Math.round(calc.pop()*100.0) / 100.0;

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

    /**
     * imageToString
     * This method converts image to string for database to store
     * @return The String representing the question's image
     */
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
