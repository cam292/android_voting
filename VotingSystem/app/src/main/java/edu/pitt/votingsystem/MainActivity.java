package edu.pitt.votingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private static Button startButton, endButton;
    private static EditText candListInput, adminPass;
    private static TextView statusText,candList;

    public static boolean voteSession;
    private SmsReceiver SmsReceiver = new SmsReceiver();
    public static TallyTable tally;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.Startbutton);
        endButton = (Button) findViewById(R.id.Endbutton);
        statusText = (TextView)findViewById(R.id.statusText);
        candList = (TextView)findViewById(R.id.candList);
        candListInput = (EditText) findViewById(R.id.candListInput);
        adminPass = (EditText) findViewById(R.id.adminPass);

        //logic to start voting
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempCandidates = candListInput.getText().toString();
                if(voteSession){
                    Toast.makeText(getApplicationContext(), "Voting System Already Started!", Toast.LENGTH_SHORT).show();
                }
                else if(tempCandidates.length() != 0){
                    List candidates = Arrays.asList(tempCandidates.split(","));
                    tally = new TallyTable(candidates);
                    voteSession = true;
                    status = "Polling for messages";
                    statusText.setText(status);
                    candList.setText(tempCandidates);
                    Toast.makeText(getApplicationContext(), "Voting System Started", Toast.LENGTH_SHORT).show();
                    candListInput.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter a list of candidates, separated by commas.", Toast.LENGTH_SHORT).show();
                }

            }


        });


        //logic to end voting
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = adminPass.getText().toString();
                Toast.makeText(getApplicationContext(), String.valueOf(password.equals("1234")), Toast.LENGTH_SHORT).show();
                if(password.equals("1234")){
                    if(tally == null){
                        Toast.makeText(getApplicationContext(), "Voting Never Started!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Voting System Stopped", Toast.LENGTH_SHORT).show();
                        voteSession = false; //end voting loop
                        adminPass.setText("");
                        printWinner();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /*
    * Gets list of candidates/votes and displays to user
     */
    private void printWinner(){
        status = "Calculating winner...";
        statusText.setText(status);

        status = "Final votes are (poster->votes): \n";
        HashMap<String,Integer> sorted = tally.getWinner();
        Set set2 = sorted.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            status += me2.getKey()+"->"+me2.getValue()+"\n";
        }

        statusText.setText(status);
        tally = null;
    }

}
