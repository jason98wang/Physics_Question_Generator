
public class Question {
	
	// init vars
	private String problemStatement;
	private SimpleLinkedList<Symbol> formula;
	
	// constructor
	Question(String problemStatement, SimpleLinkedList<Symbol> formula){
		this.problemStatement = problemStatement;
		this.formula = formula;
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
		return 0;
	}
}
