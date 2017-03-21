package edu.pitt.votingsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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


    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(edu.pitt.votingsystem.MainActivity.voteSession){
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
                    String[] msg = message.split(",");
                    SmsManager smsManager = SmsManager.getDefault();
                    edu.pitt.votingsystem.MainActivity.statusText.setText(msg[0]);


                    switch(msg[0]){
                        case "702": //request report. aka end voting
                            if(msg[1].equals("1234")){
                                edu.pitt.votingsystem.MainActivity.printWinner();
                            } else {
                                smsManager.sendTextMessage(sender,null, "Incorrect password",null,null);
                            }
                            break;
                        case "703": //init tally table. message will be {'msgId', 'passcode', 'list of candidates'}

                            if(msg[1].equals("1234")){
                                edu.pitt.votingsystem.MainActivity.statusText.setText("Tally table initialized");
                                edu.pitt.votingsystem.MainActivity.tally = new TallyTable(Arrays.copyOfRange(msg,2,msg.length));
                            } else {
                                smsManager.sendTextMessage(sender,null, "Incorrect password",null,null);
                            }
                            break;
                        default:
                            edu.pitt.votingsystem.MainActivity.statusText.setText("Received vote");
                            switch (edu.pitt.votingsystem.MainActivity.tally.castVote(sender, message)) {//try to cast vote
                                case 1: //successful vote
                                    Toast.makeText(context, "successful vote", Toast.LENGTH_SHORT).show();
                                    smsManager.sendTextMessage(sender, null, "Vote Cast!", null, null);
                                    break;
                                case 2: //duplicate vote
                                    Toast.makeText(context, "duplicate voter", Toast.LENGTH_SHORT).show();
                                    smsManager.sendTextMessage(sender, null, "You have already voted!", null, null);
                                    break;
                                case 3: //invalid candidate id
                                    Toast.makeText(context, "invalid candidate", Toast.LENGTH_SHORT).show();
                                    smsManager.sendTextMessage(sender, null, "Invalid candidate entered. Please try again", null, null);
                                    break;
                            }
                            break;
                    }


                }
            }
        }
    }
}