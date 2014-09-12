package com.kevin.example.HttpJsonParseQuiz;

import java.util.ArrayList;
import java.util.HashMap;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
 
    private ProgressDialog pDialog;
   public Button wrongButton;
    // URL to get contacts JSON
    private static String url = "http://radiant-cove-6475.herokuapp.com/json/";
 
    // JSON Node names
    private static final String TAG_QUESTIONS = "questions";
    private static final String TAG_SLNO = "id";
    private static final String TAG_QUESTION = "question";
    private static final String TAG_ANS = "answer";
    private static final String TAG_OPTIONS = "options";
    private static final String TAG_OPTIONS_OP1 = "option1";
    private static final String TAG_OPTIONS_OP2 = "option2";
    private static final String TAG_OPTIONS_OP3 = "option3";
    private static final String TAG_OPTIONS_OP4 = "option4";
    private static final String TAG_OPTIONS_OP5 = "option5";
    private static final String TAG_OPTION_OP6 = "option6";

    // contacts JSONArray
    JSONArray questions = null;
 
    // Hashmap for ListView
    ArrayList<QuestionObject> questionList ;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionList = new ArrayList<QuestionObject>();
        // Calling async task to get json
        new GetQuestions().execute();
    }
    
    
    
 
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetQuestions extends AsyncTask<ArrayList<QuestionObject>, Void, ArrayList<QuestionObject>> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Fetching data, Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected ArrayList<QuestionObject> doInBackground(ArrayList<QuestionObject>... arg) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    questions = jsonObj.getJSONArray(TAG_QUESTIONS);
 
                    // looping through All Contacts
                    for (int i = 0; i < questions.length(); i++) {
                        JSONObject c = questions.getJSONObject(i);
                         
                        String id = c.getString(TAG_SLNO);
                        String question = c.getString(TAG_QUESTION);
                        String answer = c.getString(TAG_ANS);
                        //String op4 = c.getString(TAG_OPTIONS_OP4);
                        //String op5 = c.getString(TAG_OPTIONS_OP5);
 
                        // Phone node is JSON Object
                        JSONObject options = c.getJSONObject(TAG_OPTIONS);
                        String op1 = options.getString(TAG_OPTIONS_OP1);
                        String op2 = options.getString(TAG_OPTIONS_OP2);
                        String op3 = options.getString(TAG_OPTIONS_OP3);
 
                        // tmp hashmap for single contact
                        QuestionObject qn = new QuestionObject(id, question, answer, op1, op2, op3);
                        questionList.add(qn);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                return null;
            }
 
            return questionList;
        }
 
        @Override
        protected void onPostExecute(final ArrayList<QuestionObject> result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())		// Dismiss the progress dialog
                pDialog.dismiss();
            if (result!=null)
            	{
                	setContentView(R.layout.activity_main);
                	Button start = (Button)findViewById(R.id.b1);
                	Button timeAttack = (Button)findViewById(R.id.b2);
                	start.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                        	int i=0;
                        	nextqeustion(result,i);
                        }
                    });
                	timeAttack.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                        }
                    });
                	
            	}
            
          	else{
          		Toast.makeText(getApplicationContext(), "Could not Fetch Data/Check Network connection",Toast.LENGTH_LONG).show();
          	}
          		
        }
        public void nextqeustion(final ArrayList<QuestionObject> qns, final int index){
            	setContentView(R.layout.quiz_layout);
        		final Button op1 = (Button)findViewById(R.id.op1);
            	final Button op2 = (Button)findViewById(R.id.op2);
            	final Button op3 = (Button)findViewById(R.id.op3);
            	final Button op4 = (Button)findViewById(R.id.op4);
            	final TextView question = (TextView)findViewById(R.id.qns);
            	question.setText(qns.get(index).getQuestion());
            	op1.setVisibility(1);
            	op1.setText(qns.get(index).getAnswer());
            	op2.setVisibility(1);
            	op2.setText(qns.get(index).getOption1());
            	op3.setVisibility(1);
            	op3.setText(qns.get(index).getOption2());
            	op4.setVisibility(1);
            	op4.setText(qns.get(index).getOption3());
            	op1.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View v) {
                         // Perform action on click
                    	 if (index<qns.size()-1){
                    		 int i= index;
                    		 //System.out.println("size "+qns.size()+", i: "+i);
                    		 i=i+1;
                    		 nextqeustion(qns,i);
                    		 }
                    	 else
                    		question.setText("end of quiz!!!");
                    	 	op1.setVisibility(View.GONE);
                    	 	op2.setVisibility(View.GONE);
                    	 	op3.setVisibility(View.GONE);
                    	 	op4.setVisibility(View.GONE);
                     }
                 });
            	op2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	continueProgram(qns); 
                    }
                });
            	op3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	continueProgram(qns);
                    }
                });
            	op4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	continueProgram(qns);
                    }
                });
        }
        public void continueProgram(final ArrayList<QuestionObject> questions){
     	   setContentView(R.layout.wrong_answer);
     	   final int i=0;
     	   wrongButton = (Button)findViewById(R.id.wrngbtn);
     	   wrongButton.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            	  nextqeustion(questions,i);
              }
          });
     		
        }
 
    }
 
}