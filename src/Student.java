/**
 * [Student.java]
 * The student object class
 * Author: Jason Wang, Eric Long, Brian Li, Yili Liu, Josh Cai 
 * Nov. 20, 2018
 */
public class Student {

	// vars
	private String name;
	private String studentNumber;
	private String password;
	private int incorrectQuestions;
	private int totalQuestions;

	// construtor
	/**
	 * Student
	 * This constructor creates a student object and sets its attributes
	 * @param name, the name of the student
	 * @param studentNumber, the student number of the student
	 * @param password, the password of the student account
	 */
	Student(String name, String studentNumber, String password) {
		this.name = name;
		this.studentNumber = studentNumber;
		this.password = password;
		incorrectQuestions = 0;
		totalQuestions = 0;
	}
	
	/**
	 * Student
	 * This constructor creates a student object and sets its attributes, with additional attributes
	 * @param name, the name of the student
	 * @param studentNumber, the student number of the student
	 * @param password, the password of the student account
	 * @param incorrectQuestions, number of incorrectQuestions done
	 * @param totalQuestions, total number of questions done
	 */
	Student(String name, String studentNumber, String password, int incorrectQuestions, int totalQuestions) {
		this.name = name;
		this.studentNumber = studentNumber;
		this.password = password;
		this.incorrectQuestions = incorrectQuestions;
		this.totalQuestions = totalQuestions;
	}

	// setters
	/**
	 * setName
	 * This method sets name
	 * @param name, the name to set it to 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * setStudentNumber
	 * This methods sets the student number
	 * @param studentNumber, the student number to set it to
	 */
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	/**
	 * setPassword
	 * This methods sets the student password
	 * @param password, the password to set it to
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * setIncorrectQuestions
	 * This methods sets the student incorrect questions
	 * @param incorrectQuestions, the number of incorrect questions to set it to
	 */
	public void setIncorrectQuestions(int incorrectQuestions) {
		this.incorrectQuestions = incorrectQuestions;
	}

	/**
	 * setTotalQuestions
	 * This methods sets the number of total questions
	 * @param totalQuestions, the number of total questions to set it to 
	 */
	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	

	// getters
	/**
	 * getName
	 * This method gets the name
	 * @return name, the name of the student
	 */
	public String getName() {
		return name;
	}

	/**
	 * getStudentNumber
	 * This method gets the student number of the student
	 * @return studentNumber, the student number of the student
	 */
	public String getStudentNumber() {
		return studentNumber;
	}

	/**
	 * getPassword
	 * This method gets the student password
	 * @return password, the student's password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * getIncorrectQuestions
	 * This method gets the student's total incorrect questions
	 * @return incorrectQuestions, the student's total number of incorrect questions
	 */
	public int getIncorrectQuestions() {
		return incorrectQuestions;
	}
	
	/**
	 * getTotalQuestions
	 * This method gets the student's total questions
	 * @return incorrectQuestions, the student's total number of questions
	 */
	public int getTotalQuestions() {
		return totalQuestions;
	}
	
	/**
	 * getPercentage
	 * This method calculates the student's accuracy
	 * @return output, the student's accuracy rounded to 2 decimal places
	 */
	public double getPercentage() {
		if (totalQuestions != 0) {
			double output = (double)(totalQuestions - incorrectQuestions) / totalQuestions * 100;
			
			// round to 2 decimal places
			output *= 100;
			output = Math.round(output);
			output /= 100;
						
			return output;
		}
		return -1;
	}
	
	
}
