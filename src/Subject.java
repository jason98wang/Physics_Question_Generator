import data_structures.SimpleLinkedList;

public class Subject {

	private String name;
	private int grade;
	private String level;
	private SimpleLinkedList<Unit> units;
	private SimpleLinkedList<Student> students;

	// constructor
	Subject(String name, int grade, String level) {
		this.name = name;
		this.grade = grade;
		this.level = level;
		units = new SimpleLinkedList<Unit>();
		students = new SimpleLinkedList<Student>();
	}

	// setters
	public void setName(String name) {
		this.name = name;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	// getters
	public String getName() {
		return name;
	}

	public int getGrade() {
		return grade;
	}

	public String getLevel() {
		return level;
	}

	public SimpleLinkedList<Unit> getUnits(){
		return units;
	}

	public SimpleLinkedList<Student> getStudents(){
		return students;
	}
	
	public Student getStudent(String studentNumber, String password) {
		for (int i = 0; i < students.size(); i++) {
			Student st = students.get(i);
			if (st.getStudentNumber().equals(studentNumber) && st.getPassword().equals(password)) {
				return st;
			}
		}
		return null;
	}

}
