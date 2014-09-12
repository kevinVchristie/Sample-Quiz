package com.kevin.example.HttpJsonParseQuiz;

public class QuestionObject {
	String slno;
	public String question, answer, option1, option2, option3;
	public QuestionObject(String sno, String qn, String ans, String op1, String op2, String op3){
		this.slno=sno;
		this.question=qn;
		this.answer=ans;
		this.option1=op1;
		this.option2=op2;
		this.option3=op3;
	}
	
	public QuestionObject() {
		// TODO Auto-generated constructor stub
	}

	public String getSlno(){
		return this.slno;
	}
	
	public String getQuestion(){
		return this.question;
	}
	
	public String getAnswer(){
		return this.answer;
	}
	
	public String getOption1(){
		return this.option1;
	}
	
	public String getOption2(){
		return this.option2;
	}
	
	public String getOption3(){
		return this.option3;
	}
}
