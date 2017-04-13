package edu.pitt.votingsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SmsManager;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fanof on 2/28/2017.
 */

public class SmsReceiver extends BroadcastReceiver {
    public static TallyTable tally;
    public static String[] candidates;

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    // get sms objects
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }
                    // large message might be broken into many
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(messages[i].getMessageBody());
                    }
                    String sender = messages[0].getOriginatingAddress();
                    String message = sb.toString();
                    handleText(sender, message);

                }
            }
    }
    public void handleText(String sender, String message){
        String[] msg = message.split(",");
        SmsManager smsManager = SmsManager.getDefault();

        switch(msg[0]){
            case "702": //request report. aka end voting
                if(tally != null){
                    if(msg[1].equals("1234")){
                        edu.pitt.votingsystem.viewTwo.changeStatus();
                        //edu.pitt.votingsystem.viewThree.calcWinner(tally);
                    } else {
                        smsManager.sendTextMessage(sender,null, "Incorrect password",null,null);
                    }
                }else{
                    smsManager.sendTextMessage(sender,null, "Voting hasn't started!",null,null);
                }

                break;
            case "703": //init tally table. message will be {'msgId', 'passcode', 'list of candidates'}

                if(msg[1].equals("1234")){
                    candidates = Arrays.copyOfRange(msg,2,msg.length);
                    tally = new TallyTable(candidates);

                    edu.pitt.votingsystem.MainActivity.changeStatus();

                } else {
                    smsManager.sendTextMessage(sender,null, "Incorrect password",null,null);
                }
                break;
            default:
                if(tally != null){
                    switch (tally.castVote(sender, message)) {//try to cast vote
                        case 1: //successful vote
                            smsManager.sendTextMessage(sender, null, "Vote Cast!", null, null);
                            edu.pitt.votingsystem.viewTwo.updateVotes();
                            break;
                        case 2: //duplicate vote
                            smsManager.sendTextMessage(sender, null, "You have already voted!", null, null);
                            break;
                        case 3: //invalid candidate id
                            smsManager.sendTextMessage(sender, null, "Invalid candidate entered. Please try again", null, null);
                            break;
                    }
                }else{
                    smsManager.sendTextMessage(sender,null, "Voting hasn't started!",null,null);
                }

                break;
        }
    }

}