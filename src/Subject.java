/**
 * [Subject.java]
 * Subject object representing a subject in a course
 * Authors: Jason Wang, Yili Liu, Eric Long, Josh Cai, Brian Li
 * Date: December 09, 2018
 */

import data_structures.SimpleLinkedList;

public class Subject {

	// init vars
	private String name;
	private int grade;
	private String level;
	private SimpleLinkedList<Unit> units;
	private SimpleLinkedList<Student> students;

	/**
	 * Subject
	 * Constructor that makes a subject with a given name, grade, and level
	 * @param name, the name of the subject (String)
	 * @param grade, the grade of the subject (int)
	 * @param level, the level of the object (String)
	 */
	Subject(String name, int grade, String level) {
		this.name = name;
		this.grade = grade;
		this.level = level;
		units = new SimpleLinkedList<Unit>(); //Create list of units in subject
		students = new SimpleLinkedList<Student>(); //Create list of students
	} //End of constructor

	/**
	 * setName
	 * Sets the subject name
	 * @param name, the name to be set (String)
	 */
	public void setName(String name) {
		this.name = name;
	} //End of setName

	/**
	 * setGrade
	 * Sets the grade of the subject
	 * @param grade, the grade to be set (int)
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	} //End of setGrade

	/**
	 * setLevel
	 * Sets the level of the subject
	 * @param level, the level to be set (String)
	 */
	public void setLevel(String level) {
		this.level = level;
	} //End of setLevel

	/**
	 * getName
	 * Returns the subject name
	 * @return name, the subject name (String)
	 */
	public String getName() {
		return name;
	} //End of getName

	/**
	 * getGrade
	 * Returns the subject's grade level
	 * @return grade, the grade level of the subject (int)
	 */
	public int getGrade() {
		return grade;
	} //End of getGrade

	/**
	 * getLevel
	 * Returns the subject level
	 * @return level, the subject level (String)
	 */
	public String getLevel() {
		return level;
	} //End of getLevel

	/**
	 * getUnits
	 * Returns the list of units in the subject
	 * @return units, the SimpleLinkedList of unit objects in the subject
	 */
	public SimpleLinkedList<Unit> getUnits(){
		return units;
	} //End of getUnits

	/**
	 * getStudents
	 * Returns the list of students
	 * @return students, the SimpleLinkedList of Student objects
	 */
	public SimpleLinkedList<Student> getStudents(){
		return students;
	} //End of getStudents
	
	/**
	 * getStudent
	 * Returns the specific student
	 * @param studentNumber, the student number of the student searched for (String)
	 * @param password, the password of the student searched for (String)
	 * @return st, the student object searched for
	 */
	public Student getStudent(String studentNumber, String password) {
		for (int i = 0; i < students.size(); i++) {
			Student st = students.get(i);
			if (st.getStudentNumber().equals(studentNumber) && st.getPassword().equals(password)) {
				return st; //Return student object if it matches search criteria
			}
		}
		return null; //If the student is not found within the subject
	} //End of getStudent

} //End of class
