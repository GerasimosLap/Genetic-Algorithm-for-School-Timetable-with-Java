public class Gene { //gonidio=programma tmhmatos
	int teacher;
	int lessonId;
	int hour;
	int day;
	int year;
	int grade;
	Gene(){
		this.teacher=0;
		this.lessonId=-1;
		this.year=-1;
		this.grade=-1;
		this.day=-1;
		this.hour=-1;
	}
	Gene(Gene g){
		this.teacher = g.teacher;
		this.lessonId=g.lessonId;
		this.year=g.year;
		this.grade=g.grade;
		this.day=g.day;
		this.hour=g.hour;
	}
	Gene(int teacher,int lessonId, int year,int grade, int day, int hour){
		this.teacher = teacher;
		this.lessonId=lessonId;
		this.year=year;
		this.grade=grade;
		this.day=day;
		this.hour=hour;
	}
	@Override
	public int hashCode()
	{
		return teacher+lessonId+year+grade+day+hour;
	}
	@Override
     public boolean equals(Object obj)
     {
        if(this.lessonId==((Gene)obj).lessonId && this.day==((Gene)obj).day && this.hour==((Gene)obj).hour && this.teacher==((Gene)obj).teacher) {
            return true;
        }
        return false;
     }
	public void print() {
        System.out.println("ID Lesson"+lessonId+" Day"+day+" Hour"+hour+" IDTeacher"+teacher);
	}
}
