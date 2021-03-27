import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {
	public static ArrayList<Lesson> convertToListOfLessons(String input) {
		ArrayList<Lesson> list = new ArrayList<Lesson>();
        BufferedReader reader = openFile(input);
        String line=null;
        try {
            line = reader.readLine();
            while (line != null) {
            	Lesson l = new Lesson(); //new lesson
            	//find id
            	int index = line.indexOf('-');
            	l.setId(Integer.parseInt(line.substring(0,index)));
            	//find name of lesson	
            	int index2 = line.indexOf('-',++index);
            	l.setName(line.substring(index,index2));
            	//findGrade
            	index=line.indexOf('-',++index2);
            	l.setGrade(line.substring(index2,index));
            	//find hours
            	l.setHours(Integer.parseInt(line.substring(++index)));
            	list.add(l); //add to list
            	line = reader.readLine();
            }
        }catch (IOException e) {
            System.out.println	("Error reading line ...");
            return null;
      }
		return list;
	}
	public static ArrayList<Teacher> convertToListOfTeachers(String input) {
		ArrayList<Teacher> list = new ArrayList<>();
		BufferedReader reader = openFile(input);
        String line=null;
        try {
            line = reader.readLine();
            while (line != null) {
            	Teacher t = new Teacher();
            	//find id
            	int index = line.indexOf('-');
            	t.setId(Integer.parseInt(line.substring(0,index)));
            	//find name of teacher
            	int index2 = line.indexOf('-',++index);
            	t.setName(line.substring(index,index2));
            	index=index2+1;//phga sto [
            	//find lessons
            	ArrayList<Integer> lessons = new ArrayList<>();
            	int nextl = line.indexOf(',',++index); //find lesson
            	int indexhelp = index;
            	while(nextl>0) {//if find , = find ki allo mathima
            		String id = line.substring(index , nextl);
            		lessons.add(Integer.parseInt(id));
            		index=nextl+1;
            		indexhelp = index;
            		nextl = line.indexOf(',',index+1);
            	}
            	lessons.add(Integer.parseInt(line.substring(indexhelp,line.indexOf(']'))));
            	t.setLessons(lessons);
            	index2 = line.indexOf('-',index);
            	//find maxDay
            	index=line.indexOf('-',++index2);
            	t.setMaxDay(Integer.parseInt(line.substring(index2,index)));
            	//find hours
            	index2=line.indexOf('-',++index);
            	t.setMaxWeek(Integer.parseInt(line.substring(index)));
            	list.add(t);
            	line = reader.readLine();
            }
        }catch (IOException e) {
            System.out.println	("Error reading line ...");
            return null;
        }
		return list;
	}
	private static BufferedReader openFile(String input) {
		File f = null;
        BufferedReader reader = null;	
		try {
            f = new File(input);
        } catch (NullPointerException e) {
            System.err.println("File not found.");
        }
        try {
            reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file!");
        }
		return reader;
	}
}
