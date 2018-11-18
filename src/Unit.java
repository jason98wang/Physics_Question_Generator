
public class Unit {
	
	// init vars
	private SimpleLinkedList<Question> questions;
	private SimpleLinkedList<Question> incorrectQuestions;
	private int questionsTaken;
	
	public void answeredQuestion(Question q, boolean correct) {
		questionsTaken++;
		if (!correct) {
			incorrectQuestions.add(q);
		}
	}
	
	public void addQuestion(Question q) {
		questions.add(q);
	}
	
	public void removeQuestion(Question q) {
		questions.remove(q);
	}
	
	// getters
	public Question getQuestion() {
		return questions.get((int)(randNum(0, questions.size())));
	}
	
	public Question getIncorrectQuestion() {
		return incorrectQuestions.get((int)(randNum(0, questions.size())));
	}
	
	public int getQuestionsTaken() {
		return questionsTaken;
	}
	
	// private util method to generate a random int
	private double randNum(int min, int range) {
		return Math.random() * range + min;
	}
}