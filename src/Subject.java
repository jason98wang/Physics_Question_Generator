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

	//	public void addUnit(Unit u) {
	//		units.add(u);
	//	}
	//	
	//	public void removeUnit(Unit u) {
	//		units.remove(u);
	//	}
	//	
	//	public void addStudent(Student s) {
	//		students.add(s);
	//	}
	//	
	//	public void removeStudent(Student s) {
	//		students.remove(s);
	//	}

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

}
