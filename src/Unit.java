/**
 * [Unit.java]
 * Object representing a unit in a course
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * December 09, 2018
 */

//Data structure imports
import data_structures.SimpleLinkedList;

public class Unit {

	// init vars
	private String name;
	private int num;
	private SimpleLinkedList<Question> questions;
	private SimpleLinkedList<Question> incorrectQuestions;
	private int questionsTaken;

	/**
	 * Unit
	 * Constructor that makes a unit with a given name and number
	 * @param name, the name of the unit
	 * @param num, the unit number
	 */
	Unit(String name, int num){
		this.name = name;
		this.num = num;
		questions = new SimpleLinkedList<Question>();
		incorrectQuestions = new SimpleLinkedList<Question>();
	} //End of constructor

	/**
	 * answeredQuestion
	 * Updates question counts and incorrect question list
	 * @param q, the Question answered
	 * @param correct, whether the question was answered correctly (boolean)
	 */
	public void answeredQuestion(Question q, boolean correct) {
		questionsTaken++;
		if (!correct) {
			incorrectQuestions.add(q);
		}
	} //End of answeredQuestion

	/**
	 * addQuestion
	 * Adds a question to the unit
	 * @param q, the question object to be added
	 */
	public void addQuestion(Question q) {
		questions.add(q);
	} //End of addQuestion

	/**
	 * removeQuestion
	 * Removes a given question from the unit's list of questions
	 * @param q, the question object to be removed
	 */
	public void removeQuestion(Question q) {
		questions.remove(q);
	} //End of removeQuestion

	/**
	 * getName
	 * Returns the unit name
	 * @return name, the unit name (String)
	 */
	public String getName() {
		return name;
	} //End of getName

	/**
	 * getNum
	 * Returns the unit number
	 * @return num, the unit number (integer)
	 */
	public int getNum() {
		return num;
	} //End of getNum

	/**
	 * getQuestions
	 * Returns the questions in the unit
	 * @return questions, the SimpleLinkedList of questions in the unit
	 */
	public SimpleLinkedList<Question> getQuestions() {
		return questions;
	} //End of getQuestions

	/**
	 * getIncorrectQuestions
	 * Returns the incorrect questions answered in the unit
	 * @return incorrectQuestions, the SimpleLinkedList of incorrect questions answered in the unit
	 */
	public SimpleLinkedList<Question> getIncorrectQuestion() {
		return incorrectQuestions;
	} //End of getIncorrectQuestions

	/**
	 * getQuestionsTaken
	 * Returns the number of questions taken in the unit
	 * @return questionsTaken, the number of questions taken in the unit
	 */
	public int getQuestionsTaken() {
		return questionsTaken;
	} //End of getQuestionsTaken

} //End of class
