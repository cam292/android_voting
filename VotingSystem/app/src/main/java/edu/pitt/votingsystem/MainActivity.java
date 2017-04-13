package edu.pitt.votingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private static TextView status;
    private static Button button;
    private static boolean voting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        voting = false;
        status = (TextView)findViewById(R.id.statusLabel);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(voting){
                    Intent intent = new Intent(context, viewTwo.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context,"Table has not been initialized!",Toast.LENGTH_LONG).show();
                }
            }


        });
    }


    public static void changeStatus(){
        voting = true;
        status.setText("Table initialized! Click button to continue");
        return;
    }
    /*
    * Gets list of candidates/votes and displays to user
     */
    public static void printWinner(){
//        status = "Calculating winner...";
//        statusText.setText(status);
//
//        status = "Final votes are (poster->votes): \n";
//        HashMap<String,Integer> sorted = tally.getWinner();
//        Set set2 = sorted.entrySet();
//        Iterator iterator2 = set2.iterator();
//        while(iterator2.hasNext()) {
//            Map.Entry me2 = (Map.Entry)iterator2.next();
//            status += me2.getKey()+"->"+me2.getValue()+"\n";
//        }
//
//        statusText.setText(status);
//        tally = null;
    }


}
