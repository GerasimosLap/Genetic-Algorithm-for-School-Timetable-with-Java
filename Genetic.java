import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Genetic {
	private ArrayList<Chromosome> population;
	private ArrayList<Integer> fitnessBounds;
	public ArrayList<Lesson> listLessons;
	public ArrayList<Teacher> listTeachers;
	
	private int numberOfEachGrade;

	public ArrayList<Lesson> lessonsA;
	public ArrayList<Lesson> lessonsB;
	public ArrayList<Lesson> lessonsC;
	public HashMap<Integer, ArrayList<Integer>> lessonTeachers;
	
	public HashMap<Integer,Integer> lessonRequirements; //arithmos apaitoumenwn oron ana mathima
	public HashMap<Integer,Integer> syntelestis;
	int totalNumberOfLessonHours;
	int[] lessonRequirementsPerLesson;
	
	public Genetic(ArrayList<Lesson> lessons, ArrayList<Teacher> teachers,int numberOfEachGrade) {
		this.population = null;
		this.fitnessBounds = null;
		this.listLessons=lessons;
		this.listTeachers = teachers;
		this.numberOfEachGrade=numberOfEachGrade;
		createData(lessons,teachers);
	}
	
	//create necessary data for calculations at chromosome
	private void createData(ArrayList<Lesson> lessons2, ArrayList<Teacher> teachers) {
		//create a Array with the lessons of each class
		this.lessonsA=new ArrayList<Lesson>();
		this.lessonsB=new ArrayList<Lesson>();
		this.lessonsC=new ArrayList<Lesson>();
		
		//(key:kodiko mathimatos,value:wres)
		this.lessonRequirements = new HashMap<Integer,Integer>();

		for(int i=0;i<listLessons.size();i++) {
			lessonRequirements.put(listLessons.get(i).id,listLessons.get(i).hours);
			if(listLessons.get(i).grade.equals("A")) {
				lessonsA.add(listLessons.get(i));
			}
			if(listLessons.get(i).grade.equals("B")) {
				lessonsB.add(listLessons.get(i));
			}
			if(listLessons.get(i).grade.equals("C")) {
				lessonsC.add(listLessons.get(i));
			}
		}
		
		//create neseccary teacher's table
		//(key:idlesson,value:pinaka me kathigites p didaskoun to mathima)
		this.lessonTeachers = new HashMap<Integer, ArrayList<Integer>>();
		
		for(Teacher t:teachers) {//gia kathe kathigiti
			ArrayList<Integer> helpl=t.lessons;//pare ta mathimata pou kanei
			for(Integer les:helpl) {//gia kathe mathima poy kanei
				//an uparxei sto hashmap to mathima afto prosthese sthn lista me ton kathigiti to mathima
				//an den uparxei initialize th lista k prosthese kathigiti
				if(lessonTeachers.get(les)==null) {
					lessonTeachers.put(les,new ArrayList<Integer>());
					lessonTeachers.get(les).add(t.id);
				}else {
					lessonTeachers.get(les).add(t.id);
				}
			}
		}
		
		//calculate variable:syntelestis
		//(key:kathigitis,value:sunolo mathimatwn p didaskei)
		HashMap<Integer,Integer> teacherSumLessons = new HashMap<Integer,Integer>();
		
		for(Teacher t:teachers) {
			for(Integer k: t.lessons){
				if(teacherSumLessons.get(k)!=null){
					int num= teacherSumLessons.get(k);
					teacherSumLessons.put(k,++num);
					
				}else{
					teacherSumLessons.put(k,1);
				}
			}
		}
		
		this.syntelestis = new HashMap<Integer,Integer>();
		for(Lesson l:listLessons){
			syntelestis.put(l.id,l.hours*3/teacherSumLessons.get(l.id));//id mathima, wres pou prepei na didaxthei se kathe tmima/arithmos kathigitwn pou t kanoyn
		}
		//end calculate variable:syntelestis
		
		//calculate lessonRequirementsPerLesson
		this.totalNumberOfLessonHours=0;
		this.lessonRequirementsPerLesson = new int[3];
		int counterLesson=0;
		for(Lesson l:lessonsA){ 
			totalNumberOfLessonHours+=lessonRequirements.get(l.id);
			counterLesson+=lessonRequirements.get(l.id);
		}
		lessonRequirementsPerLesson[0]=counterLesson;
		counterLesson=0;
		for(Lesson l:lessonsB){ 
			totalNumberOfLessonHours+=lessonRequirements.get(l.id);
			counterLesson+=lessonRequirements.get(l.id);
		}
		lessonRequirementsPerLesson[1]=counterLesson;
		counterLesson=0;
		for(Lesson l:lessonsC){ 
			totalNumberOfLessonHours+=lessonRequirements.get(l.id);
			counterLesson+=lessonRequirements.get(l.id);}
		lessonRequirementsPerLesson[2]=counterLesson;
		//end calculate lessonRequirementsPerLesson
	}
	
	
	public Chromosome geneticAlgorithm(int populationSize, double mutationProbability, int minimumFitness, int maximumSteps)
	{
		initializePopulation(populationSize);
		Random r = new Random();
		for(int step=0; step < maximumSteps; step++) 
		{
			ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
			for(int i=0; i < populationSize; i++)
			{
				int xIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size())); //epilogh paidiou pou tha gonimopoihsei
				Chromosome x = this.population.get(xIndex);
				int yIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
				while(yIndex == xIndex)
				{
					yIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
				}
				Chromosome y = this.population.get(yIndex);
				
				Chromosome child[] = this.reproduce(x, y); 
				
				child[0].mutate(mutationProbability);
				child[1].mutate(mutationProbability);
				newPopulation.add(child[0]);
				newPopulation.add(child[1]);
			}
			this.population = new ArrayList<Chromosome>(newPopulation);
			Collections.sort(this.population, Collections.reverseOrder());
			
			if(this.population.get(0).getFitness() >= minimumFitness)
			{
				System.out.println("Finished after " + step + " steps...");
				return this.population.get(0);
			}
			this.updateFitnessBounds();
		}
		System.out.println("Finished after " + maximumSteps + " steps...");
		return this.population.get(0);
	}
	
	
	private void initializePopulation(int populationSize) {
		this.population = new ArrayList<Chromosome>();
		
		for(int i=0;i<populationSize;i++) {
			
			this.population.add(new Chromosome(lessonsA,lessonsB,lessonsC,lessonTeachers,numberOfEachGrade,lessonRequirements,syntelestis,listLessons,listTeachers,totalNumberOfLessonHours,lessonRequirementsPerLesson));
			
		}

		this.updateFitnessBounds();
	}
	
	public void updateFitnessBounds()
	{
		this.fitnessBounds = new ArrayList<Integer>();
		//System.out.println("in fitness bounds with pop size "+this.population.size());
		//find min fitness,add up it to each fitness so every fitness become positive number
		Collections.sort(this.population, Collections.reverseOrder());
		double min=this.population.get(this.population.size()-1).getFitness()+1;
		
		if(min<0) {
			min*=-1;
			//System.out.println("in fitness bounds");
		}
		for (int i=0; i<this.population.size(); i++)
		{
			for(int j=0; j<(this.population.get(i).getFitness()+min); j++)
			{
				//System.out.println(this.population.get(i).getFitness());
                //Each chromosome index exists in the ArrayList as many times as its fitness score
                //By creating this ArrayList so, and choosing a random index from it,
                //the greater the fitness score of a chromosome the greater chance it will be chosen.
				fitnessBounds.add(i);

			}
		}
	}
	//"Reproduces" two chromosomes and generated their "child"
	public Chromosome[] reproduce(Chromosome x, Chromosome y)
	{
			
			Random r = new Random();
			int intersectionPoint = r.nextInt(x.getGenes().length);
			
			//initialize the necessary arrays for 2 children
			Gene[] geneArray1= new Gene[totalNumberOfLessonHours*numberOfEachGrade];
			for(int i=0;i<numberOfEachGrade*totalNumberOfLessonHours;i++) {geneArray1[i]=new Gene();}
			
			Gene[][][] printSchedule1 = new Gene[3*numberOfEachGrade][7][5];
			for(int i=0;i<3*numberOfEachGrade;i++){
				for(int j=0;j<7;j++){
					for(int k=0;k<5;k++){
						printSchedule1[i][j][k]=new Gene();//ola kena sthn arxh
					}
				}
			}
			Gene[] geneArray2= new Gene[totalNumberOfLessonHours*numberOfEachGrade];
			for(int i=0;i<numberOfEachGrade*totalNumberOfLessonHours;i++) {geneArray2[i]=new Gene();}
			
			Gene[][][] printSchedule2 = new Gene[3*numberOfEachGrade][7][5];
			for(int i=0;i<3*numberOfEachGrade;i++){
				for(int j=0;j<7;j++){
					for(int k=0;k<5;k++){
						printSchedule2[i][j][k]=new Gene();//ola kena sthn arxh
					}
				}
			}
			//end: initialize the necessary arrays for 2 children
			
			
			if(intersectionPoint>totalNumberOfLessonHours*numberOfEachGrade-intersectionPoint) {
				//tote thelw na kratiso to mprostino meros tou pinaka p 
				//pernw copy paste to mprostino meros tou x
				for(int i=0; i<intersectionPoint; i++)
				{
					Gene currentLesson=x.getGenes()[i];
					geneArray1[i] = currentLesson;
					printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
				}
				//thelw na parw apthn mikri meria tous y osa perissotera mporw
				for(int i=intersectionPoint; i<geneArray1.length; i++) {
					Gene currentLesson=y.getGenes()[i];
					//elegxos an uparxei epikalipsi tou y
					if(printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day].teacher==0) {//den exw epikalipsi,pare thn wra tou y
						geneArray1[i] = currentLesson;
						printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
					}else {//allios pare thn wra tou x
						Gene currentLesson2=x.getGenes()[i];
						geneArray1[i] = currentLesson2;
						printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson2;
					}
				}
			}else {
				//allios thelw to pisw 
				//pernw copy paste to pisw meros tou y
				for(int i=intersectionPoint; i<geneArray1.length; i++)
				{
					Gene currentLesson=y.getGenes()[i];
					geneArray1[i] = currentLesson;
					printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
				}
				
				//thelw na parw apthn mprostini meria tou x 
				for(int i=0; i<intersectionPoint; i++) {
					Gene currentLesson=x.getGenes()[i];
					//elegxos an uparxei epikalipsi tou x
					if(printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day].teacher==0) {//den exw epikalipsi,pare thn wra tou y
						geneArray1[i] = currentLesson;
						printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
					}else {//allios pare thn wra tou y
						Gene currentLesson2=y.getGenes()[i];
						geneArray1[i] = currentLesson2;
						printSchedule1[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson2;
					}
				}
			}
			
			if(intersectionPoint>totalNumberOfLessonHours*numberOfEachGrade-intersectionPoint) {
				//tote thelw na kratiso to mprostino meros tou pinaka
				
				//pernw copy paste to mprostino meros tou y
				for(int i=0; i<intersectionPoint; i++)
				{
					Gene currentLesson=y.getGenes()[i];
					geneArray2[i] = currentLesson;
					printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
				}
				//thelw na parw apthn mikri meria tous x osa perissotera mporw
				for(int i=intersectionPoint; i<geneArray2.length; i++) {
					Gene currentLesson=x.getGenes()[i];
					//elegxos an uparxei epikalipsi tou x
					if(printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day].teacher==0) {//den exw epikalipsi,pare thn wra tou y
						geneArray2[i] = currentLesson;
						printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
					}else {//allios pare thn wra tou y
						Gene currentLesson2=y.getGenes()[i];
						geneArray2[i] = currentLesson2;
						printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson2;
					}
				}
			}else {
				//allios thelw to pisw  dld pali to megalo 
				//pernw copy paste to pisw meros tou x
				for(int i=intersectionPoint; i<geneArray2.length; i++)
				{
					Gene currentLesson=x.getGenes()[i];
					geneArray2[i] = currentLesson;
					printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
				}
				
				//thelw na parw apthn mprostini meria tou y thn mikroteri dld osa perissotera mporo
				for(int i=0; i<intersectionPoint; i++) {
					Gene currentLesson=y.getGenes()[i];
					//elegxos an uparxei epikalipsi tou y
					if(printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day].teacher==0) {//den exw epikalipsi,pare thn wra tou y
						geneArray2[i] = currentLesson;
						printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson;
					}else {//allios pare thn wra tou x
						Gene currentLesson2=x.getGenes()[i];
						geneArray2[i] = currentLesson2;
						printSchedule2[currentLesson.year*numberOfEachGrade+currentLesson.grade][currentLesson.hour][currentLesson.day]=currentLesson2;
					}
				}
			}
			Chromosome chromo1= new Chromosome(geneArray1,printSchedule1,lessonsA,lessonsB,lessonsC,lessonTeachers,numberOfEachGrade,lessonRequirements,syntelestis,listLessons,listTeachers,totalNumberOfLessonHours,lessonRequirementsPerLesson);
			Chromosome chromo2= new Chromosome(geneArray2,printSchedule2,lessonsA,lessonsB,lessonsC,lessonTeachers,numberOfEachGrade,lessonRequirements,syntelestis,listLessons,listTeachers,totalNumberOfLessonHours,lessonRequirementsPerLesson);

			Chromosome[] chromo = new Chromosome[2];
			chromo[0] = chromo1;
			chromo[1] = chromo2;

			return chromo;
		}
	
}
