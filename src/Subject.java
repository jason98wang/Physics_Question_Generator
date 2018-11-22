public class Subject {
	
	private String name;
	private int grade;
	private String level;
	private SimpleLinkedList<Unit> units;
	
	public void addUnit(Unit u) {
		units.add(u);
	}
	
	public void removeUnit(Unit u) {
		units.remove(u);
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
	
}
