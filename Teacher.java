import java.util.ArrayList;

public class Teacher {
	int id;
	String name;
	ArrayList<Integer> lessons;
	int maxDay;
	int maxWeek;
	
	Teacher(){
		this.id = 0;
		this.name = "";
		this.lessons = new ArrayList<>();
		this.maxDay = 0;
		this.maxWeek = 0;
	}
	Teacher(int id, String name, String grade, int hours){
		this.id = id;
		this.name = name;
		this.lessons=new ArrayList<>();
		this.maxDay = 0;
		this.maxWeek = 0;
	}
	public void setId(int id){
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLessons(ArrayList<Integer> lessons) {
		this.lessons = lessons;
	}
	public void setMaxDay(int maxDay) {
		this.maxDay = maxDay;
	}
	public void setMaxWeek(int maxWeek) {
		this.maxWeek = maxWeek;
	}
	public void print() {
		System.out.print(id+" "+name+" "+"\nlessons : ");
		for(int i=0;i<lessons.size();i++){
		    System.out.println(lessons.get(i));
		} 
		System.out.print("maxDay:"+maxDay+"\nmaxWeek "+maxWeek);
		System.out.println();
		System.out.println();
		System.out.println();
	}
}
