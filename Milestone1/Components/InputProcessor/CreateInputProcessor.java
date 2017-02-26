import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Calendar;


public class CreateInputProcessor
{
    // socket for connection to SISServer
    static Socket universal;
    private static int port = 53217;
    // message writer
    static MsgEncoder encoder;
    // message reader
    static MsgDecoder decoder;

    // scope of this component
    private static final String SCOPE = "SIS.Scope1";
	// name of this component
    private static final String NAME = "InputProcessor";
    // messages types that can be handled by this component
    private static final List<String> TYPES = new ArrayList<String>(
        Arrays.asList(new String[] { "Alert","Test", "Emergency", "Confirm", "Setting" }));



    /*
     * Main program
     */
    public static void main(String[] args)
    {
        while (true)
        {
            try
            {
                // try to establish a connection to SISServer
                universal = connect();

                // bind the message reader to inputstream of the socket
                decoder = new MsgDecoder(universal.getInputStream());
                // bind the message writer to outputstream of the socket
                encoder = new MsgEncoder(universal.getOutputStream());

                /*
                 * construct a Connect message to establish the connection
                 */
                KeyValueList conn = new KeyValueList();
                conn.putPair("Scope", SCOPE);
                conn.putPair("MessageType", "Connect");
                conn.putPair("Role", "Basic");
                conn.putPair("Name", NAME);
                encoder.sendMsg(conn);

                // KeyValueList for inward messages, see KeyValueList for
                // details
                KeyValueList kvList;


                while (true)
                {
                    // attempt to read and decode a message, see MsgDecoder for
                    // details
                    kvList = decoder.getMsg();

                    // process that message
                    ProcessMsg(kvList);
                }

            }
            catch (Exception e)
            {
                // if anything goes wrong, try to re-establish the connection
                e.printStackTrace();
                try
                {
                    // wait for 1 second to retry
                    Thread.sleep(1000);
                }
                catch (InterruptedException e2)
                {
                }
                System.out.println("Try to reconnect");
                try
                {
                    universal = connect();
                }
                catch (IOException e1)
                {
                }
            }
        }
    }

    /*
     * used for connect(reconnect) to SISServer
     */
    static Socket connect() throws IOException
    {
        Socket socket = new Socket("127.0.0.1", port);
        return socket;
    }

    /*
     * Method for sending SMS which contains the information which GUI shows
     * on screen.
     */
    static void sendSMSMessage(String from, String recipients[],
                               String subject, String message)
    {

    }



    private static void ProcessMsg(KeyValueList kvList) throws Exception
    {

        String scope = kvList.getValue("Scope");
        if (!SCOPE.startsWith(scope))
        {
            return;
        }

        String messageType = kvList.getValue("MessageType");
        if (!TYPES.contains(messageType))
        {
            return;
        }

        String sender = kvList.getValue("Sender");

        String receiver = kvList.getValue("Receiver");

        String purpose = kvList.getValue("Purpose");

        switch (messageType)
        {

        case "Alert":
            System.out.println("\n*** Alert from "+sender+" ***");
            switch (sender)
            {

            case "Remote":
                System.out.println("Received alert from PrjRemote");
                KeyValueList test = new KeyValueList();
                test.putPair("Scope", SCOPE);
                test.putPair("MessageType", "Alert");
                test.putPair("Sender", "InputProcessor");
                test.putPair("Receiver", "Remote");
                encoder.sendMsg(test);
                break;

            }
            break;

        case "Emergency":
            String sup = kvList.getValue("MainComponent");
            String auxs = kvList.getValue("HelperComponents");
            String note = kvList.getValue("Note");

            System.out.println("\n*** Emergency Alert from " + sup + " backed by " + auxs+" ***");
            break;

        case "Confirm":
            System.out.println("Connect to SISServer successful.");
            break;
        case "Setting":
            if (receiver.equals(NAME))
            {
                System.out.println("Message from " + sender);
                System.out.println("Message type: " + messageType);
                System.out.println("Message Purpose: " + purpose);
                switch (purpose)
                {
                case "SwitchUser":
                    String user = kvList.getValue("UserID");
                    //reading.uid = user;
                    System.out.println("User Switched: "+user);
                    break;
                case "UpdateRecipients":
          //           String recs = kvList.getValue("Recipients");
          //           reading.recipients = recs.replaceAll("\\s+", "").split(",",0);
					// System.out.println("Recipients Updated: "+Arrays.toString(reading.recipients)+" "+reading.recipients.length);
                    break;

                case "Kill":
                    System.exit(0);
                    break;
                }
            }
            break;
        }
    }


}
