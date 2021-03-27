import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class main {
	static ArrayList<Lesson> lessons;
	static ArrayList<Teacher> teachers;
	public static void main(String args[])
	{
		parseData("lessons.txt","teachers.txt"); //parse data to lists
		long start = System.currentTimeMillis();
		Genetic g = new Genetic(lessons,teachers,3);//create genetic
		Chromosome x = g.geneticAlgorithm(500, 0.7,100, 200);
		
		long end = System.currentTimeMillis();
		long res=end-start;
		System.out.println("TIME: "+res);
		Writer.createFile("schedule.txt",3,x.getPrintSchedule());
	}
	public static void parseData(String filenameLessons,String filenameTeachers) {
		lessons = Reader.convertToListOfLessons(filenameLessons); //create arrayList with lessons
		teachers = Reader.convertToListOfTeachers(filenameTeachers); //create arrayList with teachers
	}
}


