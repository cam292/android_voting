package edu.pitt.votingsystem;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * Created by craigmazzotta on 4/13/17.
 */

public class viewThree extends Activity {
    private static Context context;
    private static TextView winner;
    private static Button button;
    private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_three);
        context = getApplicationContext();

        chart = (PieChart) findViewById(R.id.graph);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.setCenterText("Voting Results");


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
        TallyTable table = SmsReceiver.tally; //get the tally table
        HashMap<String,Integer> sorted = table.getWinner(); //sort it
        Set set2 = sorted.entrySet(); //convert to set
        Iterator iterator2 = set2.iterator(); //convert to iterator
        int i = 0;

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        while(iterator2.hasNext()) {

            Map.Entry me2 = (Map.Entry)iterator2.next();
            if(i==0)
                winner.setText(me2.getKey().toString());

            //entries.add(new BarEntry(i,Float.parseFloat(me2.getValue().toString()),me2.getKey().toString()));
            entries.add(new PieEntry(Float.parseFloat(me2.getValue().toString()),me2.getKey().toString()));
            i++;
        }

        for(int c: ColorTemplate.COLORFUL_COLORS){
            colors.add(c);
        }
        PieDataSet set = new PieDataSet(entries, "Candidate IDs");
        set.setColors(colors);
        PieData data = new PieData(set);

        chart.setData(data);
        chart.invalidate();


    }

}

