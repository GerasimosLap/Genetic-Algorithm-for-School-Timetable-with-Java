import java.io.*;
import java.util.*;

public class Writer {

	public static void createFile  (String data,int numberOfEachGrade,Gene[][][] printSchedule) {

		File f = null;
		BufferedWriter writer = null;

		try	{
			f = new File(data);
		}
		catch (NullPointerException e) {
			System.out.println ("Can't create file");
		}

		try	{
			writer = new BufferedWriter(new FileWriter(f));
		}
		catch (IOException e){
			System.out.println("Can't write to file");
		}
		try{
			char[] grades = new char[]{'A','B','C'};			
			for(int i = 0;i<3*numberOfEachGrade;i++){
				if(i<3) {
					writer.write("                   "+grades[0]+""+((i%numberOfEachGrade)+1)+"\n");
				}else if(i<6) {
					writer.write("                   "+grades[1]+""+((i%numberOfEachGrade)+1)+"\n");
				}else {
					writer.write("                   "+grades[2]+""+((i%numberOfEachGrade)+1)+"\n");
				}
				writer.write("   M   |   T   |   T   |   W   |   F   |\n");
				for(int j=0;j<7;j++){
					for(int k=0;k<5;k++){
						String newS=" "+printSchedule[i][j][k].lessonId+"/"+printSchedule[i][j][k].teacher+"";
						while(newS.length()<7) {
							newS+=" ";
						}
						newS+="|";
						writer.write(newS);
					}
					writer.write("\n");
				}writer.write("////////////////////////////////////////\n");
			}
		}catch (IOException e) {
			System.err.println("Write Error!");
		}
	
		
		
		try {
			writer.close();

		}catch (IOException e) {
			System.err.println("Error closing file.");
		}
	}
}