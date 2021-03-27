
public class Lesson {
	int id;
	String name;
	public String grade;
	public int hours;
	
	Lesson(){
		this.id = 0;
		this.name = "";
		this.grade = "";
		this.hours = 0;
	}
	Lesson(int id, String name, String grade, int hours){
		this.id = id;
		this.name = name;
		this.grade = grade;
		this.hours = hours;
	}
	public void setId(int id){
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setGrade(String grade) {
		this.grade = grade ;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getHours() {
		return hours;
	}
	public void print() {
		System.out.println("ID: "+id+"\nName: "+name+"\nGrade "+grade+"\nHour "+hours);
		System.out.println();
	}
}
