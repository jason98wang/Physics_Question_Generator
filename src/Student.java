
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
	
	public double getPercentage() {
		if (totalQuestions != 0) {
			double output = (double)(incorrectQuestions) / totalQuestions * 100;
			
			// round to 2 decimal places
			output *= 100;
			output = Math.round(output);
			output /= 100;
						
			return output;
		}
		return -1;
	}
	
	
}
