import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.*;
import java.util.Map;
import java.util.*;
public class Chromosome implements Comparable<Chromosome> {
	public double Ena;
	public double Duo;
	public double Basic1;
	public double Basic2;
	public double Muon;
	public double Plus;
	
	private Gene [] genes;
	private double fitness; //score
	private Gene [][][] printSchedule;

	private int numberOfEachGrade;
	private ArrayList<Lesson> lessonsA;
	private ArrayList<Lesson> lessonsB;
	private ArrayList<Lesson> lessonsC;
	private HashMap<Integer, ArrayList<Integer>> lessonTeachers;
	private HashMap<Integer,Integer> lessonRequirements; 
	private HashMap<Integer,Integer> syntelestis;
	private int totalNumberOfLessonHours;
	private int[] lessonRequirementsPerLesson;
	private ArrayList<Lesson> listLessons;
	private ArrayList<Teacher> listTeachers;
	private HashMap<Integer,ArrayList<Integer>> teachersByLessons = new HashMap<Integer,ArrayList<Integer>>();
	
	//constructor for child
	public Chromosome(Gene[] genes,Gene [][][] printSchedule,ArrayList<Lesson> lessonsA,ArrayList<Lesson> lessonsB,ArrayList<Lesson> lessonsC,HashMap<Integer, ArrayList<Integer>> lessonTeachers,int numberOfEachGrade,HashMap<Integer,Integer> lessonRequirements,HashMap<Integer,Integer> syntelestis, ArrayList<Lesson> listLessons,ArrayList<Teacher>listTeachers,int totalNumberOfLessonHours,int[] lessonRequirementsPerLesson)
	{
		this.genes=genes;
		this.printSchedule=printSchedule;
		
		this.lessonsA=lessonsA;
		this.lessonsB=lessonsB;
		this.lessonsC=lessonsC;
		this.lessonTeachers=lessonTeachers;
		this.numberOfEachGrade=numberOfEachGrade;
		this.lessonRequirements=lessonRequirements;
		this.syntelestis=syntelestis;
		this.listLessons=listLessons;
		this.listTeachers=listTeachers;
		this.totalNumberOfLessonHours=totalNumberOfLessonHours;
		this.lessonRequirementsPerLesson=lessonRequirementsPerLesson;
		
		this.Ena=0;
		this.Duo=0;
		this.Basic1=0;
		this.Basic2=0;
		ArrayList<Integer> gg=new ArrayList<Integer>();
		for(int i=0;i<genes.length;i++){
			int currentLesson= genes[i].lessonId;
			int teach=genes[i].teacher;
			if(teachersByLessons.get(currentLesson)!=null){
				gg= teachersByLessons.get(currentLesson);
				boolean flag=false;
				for(int ff:gg){
					if(ff==teach){flag=true; break;}
				}
				if(!flag){
					gg.add(teach);
				}
			}else{
				gg.add(teach);
			}
			teachersByLessons.put(currentLesson,gg);
		}
		//calculateFitness();
	}
	
	//random constructor
	public Chromosome(ArrayList<Lesson> lessonsA,ArrayList<Lesson> lessonsB,ArrayList<Lesson> lessonsC,HashMap<Integer, ArrayList<Integer>> lessonTeachers,int numberOfEachGrade,HashMap<Integer,Integer> lessonRequirements,HashMap<Integer,Integer> syntelestis, ArrayList<Lesson> listLessons,ArrayList<Teacher>listTeachers,int totalNumberOfLessonHours,int[] lessonRequirementsPerLesson)
	{
		this.numberOfEachGrade=numberOfEachGrade;
		this.lessonRequirements=lessonRequirements;
		this.lessonsA=lessonsA;
		this.lessonsB=lessonsB;
		this.lessonsC=lessonsC;
		this.lessonTeachers=lessonTeachers;
		this.syntelestis=syntelestis;
		this.listLessons=listLessons;
		this.listTeachers=listTeachers;
		this.totalNumberOfLessonHours=totalNumberOfLessonHours;
		this.lessonRequirementsPerLesson=lessonRequirementsPerLesson;
		this.genes = new Gene[totalNumberOfLessonHours*numberOfEachGrade];
		printSchedule = new Gene[3*numberOfEachGrade][7][5];
		for(int i=0;i<3*numberOfEachGrade;i++){
			for(int j=0;j<7;j++){
				for(int k=0;k<5;k++){
					printSchedule[i][j][k]=new Gene();
				}
			}
		}
		
		for(int o=0;o<this.genes.length;o++)this.genes[o]=new Gene();//initialize array 

		int counter=0;
		for(int year=0;year<3;year++){
			for(int classNumber=0;classNumber<numberOfEachGrade;classNumber++){
				int index=0;
				int requirements=0;
			
				ArrayList<Lesson> currentLessons=new ArrayList<Lesson>();
				switch(year){
					case 0:
						currentLessons=lessonsA;
						requirements=lessonRequirementsPerLesson[0];
						index=lessonRequirementsPerLesson[0]*classNumber;
						break;
					case 1:
						currentLessons=lessonsB;
						requirements=lessonRequirementsPerLesson[1];
						index=lessonRequirementsPerLesson[0]*numberOfEachGrade+lessonRequirementsPerLesson[1]*classNumber;
						break;
					case 2:
						currentLessons=lessonsC;
						requirements=lessonRequirementsPerLesson[2];
						index=(lessonRequirementsPerLesson[0]+lessonRequirementsPerLesson[1])*numberOfEachGrade+lessonRequirementsPerLesson[2]*classNumber;
						break;
					default:
						System.out.println("Error");
				}
				Random r=new Random();

				int[] startHour = new int[5];
				for(int i=0; i < 5 ;i++) {
					startHour[i] = 0;
				}
			
				ArrayList<Integer> gg=new ArrayList<Integer>();
				ArrayList<Integer> hoursToBeTaught=new ArrayList<Integer>();
	
				HashMap<Integer,Integer> rememberIndex= new HashMap<Integer,Integer>();
				int newIndex=0;
				for(Lesson l:currentLessons) {
					newIndex+=l.getHours();
					rememberIndex.put(l.id,newIndex-l.getHours());
					int sumHours=l.getHours();
					while(sumHours>0) {
							hoursToBeTaught.add(l.id);
							sumHours--;
					}
				}
				while(!hoursToBeTaught.isEmpty()){
					//System.out.println("end pop"+ hoursToBeTaught.size());
					int randomHours = r.nextInt(5);
					if(startHour[randomHours]==7)continue;
					int randomLesson= r.nextInt(hoursToBeTaught.size());

					counter++;
					int randomLessonId=hoursToBeTaught.get(randomLesson);
					//System.out.println("selected index "+ randomLesson+" "+randomLessonId);
					int teach = lessonTeachers.get(randomLessonId).get(r.nextInt(lessonTeachers.get(randomLessonId).size()));
					
					if(teachersByLessons.get(randomLessonId)!=null){
						gg= teachersByLessons.get(randomLessonId);
						boolean flag=false;
						for(int i:gg){
							if(i==teach){flag=true; break;}
						}
						if(!flag){gg.add(teach);}
					}else{
						gg.add(teach);
					}
					teachersByLessons.put(randomLessonId,gg);
					int putIndex=rememberIndex.get(randomLessonId);
					
					rememberIndex.put(randomLessonId,putIndex+1);
					genes[index+putIndex]=new Gene(teach,randomLessonId,year,classNumber,randomHours,startHour[randomHours]);
					
					printSchedule[year*3+classNumber][startHour[randomHours]][randomHours]=genes[index+putIndex];
					startHour[randomHours] = startHour[randomHours] + 1;
					
					hoursToBeTaught.remove(randomLesson);
				}
			}
		}
		//calculateFitness();
	}
	public Gene[] getGenes()
	{
		return this.genes;
	}
	public Gene[][][] getPrintSchedule()
	{
		return this.printSchedule;
	}
	public double getFitness()
	{
		return this.fitness;
	}
	
	public void mutate(double probability)
    { 
		Random r=new Random();
        for(int i=0;i<this.genes.length;i++){
            if(r.nextDouble()<probability){
                Gene thisGene=genes[i];
                ArrayList<Integer> tempTeachersList=lessonTeachers.get(thisGene.lessonId);
                boolean flag=true;
                while(flag){
                    if(tempTeachersList.size()==0)break;
                    int index=r.nextInt(tempTeachersList.size());
                    int thisTeacher=tempTeachersList.get(index);
                    boolean selected=true;
                    for(int j=0;j<3*numberOfEachGrade;j++){
                        if(thisGene.equals(printSchedule[j][thisGene.hour][thisGene.day]))continue;
                        if(printSchedule[j][thisGene.hour][thisGene.day].teacher==thisTeacher){
                            selected=false;
                            break;
                        }
                    }
                    if(selected){
                    	printSchedule[thisGene.grade][thisGene.hour][thisGene.day].teacher=thisTeacher;
                        genes[i].teacher=thisTeacher;
                        flag=false;
                    }else{
                        tempTeachersList.remove(index);
                    }
                }
            }
        }
        calculateFitness();
    }
	public void calculateFitness()
	{
		double sum3=0;//3os periorismos
		double sum5=0;//5os periorismos
		int sum1=0;//1os periorismos
		double sum4=0;
		//create useful data for calculate fitness
		HashMap<Integer,HashMap<Integer,Integer>> teacherRate= new  HashMap<Integer,HashMap<Integer,Integer>>();//id kathigiti/id mathimatos,wres mathimatos ana mathima ana kathigiti
		for (Teacher t:listTeachers){
			HashMap<Integer,Integer> helpMap = new HashMap<Integer,Integer>();
			for(int j:t.lessons){
				helpMap.put(j,0);
			}
			teacherRate.put(t.id,helpMap);
		}//end of create useful data for calculate fitness
		
		//calculate for sum 1 and sum 3
		for(int j=0;j<3*numberOfEachGrade;j++){
			int weekcounter=0;
			int[] temp=new int[5];
			for(int p=0;p<5;p++){temp[p]=0;}

			for(int i=0;i<5;i++){
				
				int daycounter=0;
				boolean firstHour=true;
				int sum=0;//wres p den ginetai mathima
				int tmpsum=0;
				for(int k=0;k<7;k++){
					Gene currentGene=printSchedule[j][k][i];
					if(currentGene.teacher==0){//gia thn kathe wra/gene
						if(!firstHour) {
							tmpsum++;
						}
						continue;
					}else{
						sum+=tmpsum;
						tmpsum=0;
						if(firstHour) {
							firstHour=false;
						}
						HashMap<Integer,Integer> ok=new HashMap<Integer,Integer>();
						if(teacherRate.containsKey(currentGene.teacher)==false){
							ok.put(currentGene.lessonId,1);
							teacherRate.put(currentGene.teacher,ok);
						}else{
							ok=teacherRate.get(currentGene.teacher);
							if(ok.containsKey(currentGene.lessonId)==false){
								ok.put(currentGene.lessonId,0);
							}
							ok.put(currentGene.lessonId,(ok.get(currentGene.lessonId)+1));//prosthetei +1 stis wre mathimatos sto mathima pou eksetazoyme
						}
						teacherRate.put(currentGene.teacher,ok);//ananewnei tis wres pou doulevei o kathigitis
						daycounter++;
					}
					
				}
				temp[i]=daycounter;
				weekcounter+=daycounter;
				sum1 += sum*10;
				this.Ena = sum1;
			}
			if(weekcounter!=0){
                sum3+=calculateFor3And4(weekcounter,printSchedule[j],2);
                //System.out.println("kkkkkkkkkkkkkkkkkkk");
            }//end calculate for sum1 and sum3
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//calculate for sum 5
			ArrayList<Lesson> tempLessonList = new ArrayList<Lesson>();
			if(j<3){
				tempLessonList=lessonsA;
			}else if(j>5){
				tempLessonList=lessonsB;
			}else{
				tempLessonList=lessonsC;
			}
				
			for(Lesson l:tempLessonList){
				ArrayList<Integer> temp1=teachersByLessons.get(l.id);
			 	for(int t:temp1){//gia kathe mathima ths A poioi to kanoun
			 			HashMap<Integer,Integer> temporary= teacherRate.get(t);//id mathimatos, wres po didakse
			 			if(temporary.get(l.id)!=null)sum5+=(double)(Math.abs((syntelestis.get(l.id)-temporary.get(l.id)))/(syntelestis.get(l.id)*1.0))*0.05;
			 	}
			}//calculate for sum5
		}
		//calculate for sum4
		for(Lesson currentLesson: listLessons){
			int start=0;
			int end=0;
			if(currentLesson.hours==1)continue;
			switch(currentLesson.grade){
				case "A":
					start=0;
					end=2;
				case "B":
					start=3;
					end=5;
				case "C":
					start=6;
					end=8;
			}
			for(int i=start;i<=end;i++){
				int[] hoursTaught=new int[5];
				for(int day=0;day<5;day++){
					for(int hour=0;hour<7;hour++){
						if(printSchedule[i][hour][day].lessonId==currentLesson.id)hoursTaught[day]++;
					}
				}
				Gene[][] newLesson=new Gene[7][5];
				for(int day=0;day<5;day++){
					for(int hour=0;hour<7;hour++){
						if(hour<hoursTaught[day]){
							newLesson[hour][day]=new Gene(1,1,1,1,1,1);
						}else{
							newLesson[hour][day]=new Gene();
						}
					}
				}
				sum4+=calculateFor3And4(currentLesson.hours,newLesson,2);
			}
		}//end calculate for sum 4
		//athroisma mathimatwn na evdomada
		//
		
		int sum2AndBasic1=calculateSum2AndBasic1();
		//this.fitness=sum3+sum4+sum5-sum1-sum2AndBasic1;
		Muon=sum1+sum2AndBasic1+basic2();
		//Muon=-sum1-sum2AndBasic1;
		Plus=(sum3+sum4+sum5)/160.0*120.0;
		this.fitness=Plus-Muon;
		
		
		//System.out.printf("[CALC] Score after %.2f , for chromosome %s\n" , getFitness(),this.toString());
	}
	public double calculateFor3And4(int i, Gene[][] genees,int perfectScore){
		//System.out.println("///////////////////////////\n"+i);
		int[][] program = new int[7][5];
		int[][] worstProgram = new int[7][5];
		for(int hours=0;hours<7;hours++){
			for(int days=0;days<5;days++){
				program[hours][days]=0;
				worstProgram[hours][days]=0;
				}
		}
		int k=i;
		int f=i;
		
		for(int days=0;days<5;days++){
			boolean flag=true;
			for(int hours=0;hours<7;hours++){
				worstProgram[hours][days]=1;
				k--;
				if(k==0){flag=false;break;}
			}
			if(!flag){break;}
		}
		for(int hours=0;hours<7;hours++){
			boolean flag=true;
			for(int days=0;days<5;days++){
				program[hours][days]=1;
				f--;
				if(f==0){flag=false;break;}
			}
			if(!flag){break;}
		}

		int index=0;
		for(int p=0;p<7;p++){
			if(program[p][0]==0){index=p-1;break;}
		}
		int score=0;
		int officialScore=0;

		for(int hours=0;hours<7;hours++){
			int counterBest=0;
			int counterWorst=0;
			int counterNow=0;
			for(int days=0;days<5;days++){
				if(worstProgram[hours][days]==1)counterWorst++;
				if(program[hours][days]==1)counterBest++;
				if(genees[hours][days].teacher!=0)counterNow++;
			}

			score+=Math.abs(counterWorst-counterBest)*(Math.abs(index-hours)+1);
			officialScore+=Math.abs(counterNow-counterBest)*(Math.abs(index-hours)+1);
		}
		//System.out.println("officialScore  "+officialScore+ " perfectScore "+perfectScore+ " score "+score);
		double test=(double)((score-officialScore)/(score*1.0))*perfectScore;
		//System.out.println("sfalma "+test);
		return test;
	}
	
	public int calculateSum2AndBasic1() {
		int sum=0; //=sum2
		int basic=0;//posa diplotupa k poses fores t kathe diplotupo
		for(int i=0;i<5;i++){ //ana mera
			//for sum 2
			HashMap<Integer, Integer>  kathhours= new HashMap<Integer, Integer>(); //id kathigiti,poses wres didakse os tr sunexomena
			//end for sum 2
			//both
			ArrayList<Integer> kathigites = new ArrayList<Integer>(); //tha prosthetei kathe fora to torino,tous kathigites ths torinis wras
			//end both
			for(int k=0;k<7;k++){ //ana wra
				kathigites.clear();
				 for(int tmima=0; tmima<3*numberOfEachGrade; tmima++){
					 if(printSchedule[tmima][k][i].teacher!=0) {
						 kathigites.add(printSchedule[tmima][k][i].teacher);
					 }
		         }
				 //for sum2
				 HashMap<Integer, Integer>  kathhoursNeo= new HashMap<Integer, Integer>();
				 for (Integer key: kathhours.keySet()) {
						if(kathigites.contains(key)) {//vrikame pithanothta epanalipsis
							int value=kathhours.get(key);
							if(value>2) {//shmainei oti vrika 3h ora sunexomenh
								sum++;//aukise kata ena ton metrhth sfalmatwn
								//kathhoursNeo.put(key, 2);
							}
							kathhoursNeo.put(key, value+1);
						}
				 }
				//end for sum2
				 
				 //both
				 HashMap<Integer, Integer>  kathigitesTemp= new HashMap<Integer, Integer>(); //id kathigiti=poses fores ton exw dei
				 for(Integer kath:kathigites) {
					 if(kathhoursNeo.get(kath)==null) {
						 kathhoursNeo.put(kath, 1);//vrethike stn torini wra k den eixe vrethei prin
					 }
					//basic 1 calculate
					 if(kath!=0) {//axriasto
						 if(kathigitesTemp.get(kath)==null) { //prwth fora p vlepw kathigiti
							 
							 kathigitesTemp.put(kath, 0);
						 }else {
							 kathigitesTemp.put(kath,kathigitesTemp.get(kath)+1);
						 }
					}
					//end basic 1 calculate
				 }
				//end both
				 kathhours=kathhoursNeo;
				 //basic 1 calculate
				 //int sumTemp=0;
				 for (Integer key: kathigitesTemp.keySet()) {
					 if(key!=0) {//axriasto
						 basic=basic+kathigitesTemp.get(key);//posa diplotupa eixa
					 }
				 }
				 
				 //end basic 1 calculate
			}
		}
		basic=basic*20;
		this.Basic1=basic;
		
		sum=sum*5;
		this.Duo=sum;
		return sum+basic;
	}
	public int basic2() {
        HashMap<Integer,int[]> workingHours= new  HashMap<Integer,int[]>();
        //id kathigiti->integer[] 5 imerwn
       
        for(int day=0;day<5;day++){
            for(int hour=0;hour<7;hour++){
                for(int tmhma=0;tmhma<3*numberOfEachGrade;tmhma++){
                	if(printSchedule[tmhma][hour][day].teacher!=0) {
	                    if(workingHours.containsKey(printSchedule[tmhma][hour][day].teacher)) {
	                    	int[] tmpdayz=workingHours.get(printSchedule[tmhma][hour][day].teacher);
	                    	tmpdayz[day]++;
	                        workingHours.put(printSchedule[tmhma][hour][day].teacher,tmpdayz);
	                    }else {
	                       // dayz[day]=1;
	                        int[] dayz=new int[5];
	                        dayz[day]=1;
	                        workingHours.put(printSchedule[tmhma][hour][day].teacher,dayz);              
	                    }
                	}
                }
            }
        }
        HashMap<Integer,Teacher> tmpTeachers= new  HashMap<Integer,Teacher>();
        for(Teacher x:listTeachers) {
        	tmpTeachers.put(x.id, x);
        }
        int sum=0;
        for(Integer t:workingHours.keySet()) {
        	int[] tmpdayz=workingHours.get(t);
        	int weeksum=0;
        	for(int i=0;i<5;i++) {
        		if(tmpTeachers.get(t).maxDay<tmpdayz[i]) {
        			sum++;
        		}
        		weeksum+=tmpdayz[i];
        	}
        	if(tmpTeachers.get(t).maxWeek<weeksum) {
    			sum++;
    		}
        }
        this.Basic2=sum*20;
        return sum*20;
     }
	 //end calculate fitness
	 @Override
	 public boolean equals(Object obj)
	 {
		 for(int i=0; i<3*numberOfEachGrade; i++) {
			if(!this.genes[i] .equals( ((Chromosome)obj).genes[i]))
	 		{
	 				return false;
	 		}
	 	}
	 	return true;
	 }
	@Override
	public int hashCode()
	{
		int hashcode = 0;
		for(int i=0; i<this.genes.length; i++)
		{
			hashcode += this.genes[i].hashCode();
		}
		return hashcode;
	}
	
    //The compareTo function has been overriden so sorting can be done according to fitness scores
	@Override
	public int compareTo(Chromosome x)
	{
		if(this.fitness - x.fitness>0){
			return 1;
		}else if(this.fitness - x.fitness<0) {
			return -1;
		}else {
			return 0;
		}
	}
	//prints
	public void print() {
		for(int i=0;i<3*numberOfEachGrade;i++){
			for(int j=0;j<7;j++){
				for(int k=0;k<5;k++){
					printSchedule[i][j][k].print();
				}
			}
			System.out.println("////////////////////////");
		}
	}
	public void print2() {
		for(int i=0;i<genes.length;i++){
			if(genes[i]==null){System.out.println("problem at "+i);}
			System.out.println(i);genes[i].print();

		}
	}
	public void print3(){
		for(int i = 0;i<3*numberOfEachGrade;i++){
			for(int j=0;j<7;j++){
				for(int k=0;k<5;k++){
					System.out.print(printSchedule[i][j][k].lessonId+"/"+printSchedule[i][j][k].teacher+"  |  ");

				}System.out.println();
			}
			System.out.println("////////////////////////");
		}
	}
}
