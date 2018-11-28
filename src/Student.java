
public class Student {

	// vars
	private String name;
	private String studentNumber;
	private String password;
	private int incorrectQuestions;
	private int totalQuestions;

	// construtor
	Student(String name, String studentNumber, String password) {
		this.name = name;
		this.studentNumber = studentNumber;
		this.password = password;
		incorrectQuestions = 0;
		totalQuestions = 0;
	}
	
	Student(String name, String studentNumber, String password, int incorrectQuestions, int totalQuestions) {
		this.name = name;
		this.studentNumber = studentNumber;
		this.password = password;
		this.incorrectQuestions = incorrectQuestions;
		this.totalQuestions = totalQuestions;
	}

	// setters
	public void setName(String name) {
		this.name = name;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setIncorrectQuestions(int incorrectQuestions) {
		this.incorrectQuestions = incorrectQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	// getters
	public String getName() {
		return name;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public String getPassword() {
		return password;
	}
	
	public int getIncorrectQuestions() {
		return incorrectQuestions;
	}
	
	public int getTotalQuestions() {
		return totalQuestions;
	}
}
