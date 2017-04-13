package edu.pitt.votingsystem;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import android.widget.Toast;

/**
 * Created by craigmazzotta on 4/13/17.
 */

public class viewThree extends Activity {
    private static Context context;
    private static TextView winner;
    private static Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_three);
        context = getApplicationContext();

        winner = (TextView)findViewById(R.id.winnerText);
        calcWinner();

        button = (Button)findViewById(R.id.finishButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }


        });
    }
    private void calcWinner(){
        TallyTable table = SmsReceiver.tally;
        HashMap<String,Integer> sorted = table.getWinner();
        Set set2 = sorted.entrySet();
        Iterator iterator2 = set2.iterator();
        int i = 0;
        while(iterator2.hasNext()) {

            Map.Entry me2 = (Map.Entry)iterator2.next();
            if(i==0)
                winner.setText(me2.getKey().toString());
            //status += me2.getKey()+"->"+me2.getValue()+"\n";
            i++;
        }

    }

}
