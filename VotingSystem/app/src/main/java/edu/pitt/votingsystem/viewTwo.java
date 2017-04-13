
/**
 * Created by craigmazzotta on 4/13/17.
 */

package edu.pitt.votingsystem;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class viewTwo extends Activity {
    private static Context context;
    private static TextView status,candList,voteText;
    private static Button button;
    private static boolean voting;
    private String[] candidates;
    private static int votes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_two);
        context = getApplicationContext();

        candList = (TextView) findViewById(R.id.candList);
        candidates = SmsReceiver.candidates;
        setCandidates();

        voting = true;

        votes = 0;
        voteText = (TextView)findViewById(R.id.votes);
        voteText.setText(Integer.toString(votes));

        status = (TextView)findViewById(R.id.status);

        button = (Button)findViewById(R.id.resultsButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!voting){
                    Intent intent = new Intent(context, viewThree.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context,"Voting isn't over!",Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    public static void changeStatus(){
        status.setText("Voting closed. Click button to see results!");
        voting = false;
        return;
    }
    private void setCandidates(){
        String cands = "";
        for(int i=0; i<candidates.length;i++){
            if(i==0)
                cands+=candidates[i];
            else
                cands = cands+", "+candidates[i];
        }
        candList.setText(cands);
        return;
    }
    public static void updateVotes(){
        votes++;
        voteText.setText(Integer.toString(votes));
        return;
    }


}
