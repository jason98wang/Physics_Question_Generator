public abstract class Subject {
	
	private String name;
	private int grade; 
	private String level;
	
	
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
	
}
