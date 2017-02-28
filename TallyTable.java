/**************************************
 * Class TallyTable:
 * Stores voters' numbers and VoterId they voted for.
 * Also stores the VoterId and number of votes for that id
 ***************************************/
class TallyTable {

    HashMap<String, String> Voters;  		//<Voterphone#, VoterID>
    HashMap<String, Integer> Votes;		 	//<VoterID, #ofVotes>
    //List<String> candidates;

    /*
    * Constructor for class
     */
    public TallyTable(List<String> cand) {

        Voters = new HashMap<String, String>();
        Votes = new HashMap<String, Integer>();

        for(String c : cand){ //initialize votes for each id to 0
            Votes.put(cand, 0);
        }
        //candidates = cand;

    }

    /*
    * Cast a valid vote
    * @return 1 for valid vote
    * @return 2 if voter already cast vote
    * @return 3 if invalid candId
     */
    public int castVote(String voterNum, String candID) {

        if (Votes.containsKey(candID)) { //valid candidate id
            if (Voters.containsKey(voterNum)) { //voter is already in system
                return 2;
            }
            else { //add voter and cast vote
                Voters.put(voterNum, candID);
                Votes.put(candID, Votes.get(candID) + 1);

                return 1;
            }
        } else return 3;

    }

    /*
    * Returns the number of winners as a string of N CandidateID,NumberVote separated by semicolons
    *
    * @return result The sorted HashMap of candIds and their votes.
     */
    public HashMap<String, Integer> getWinners() {

        List<HashMap.Entry<String, Integer>> list = new LinkedList<HashMap.Entry<String, Integer>>( Votes.entrySet() );
        Collections.sort( list, new Comparator<HashMap.Entry<String, Integer>>()
        {
            public int compare( HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2 )
                {return (o1.getValue()).compareTo( o2.getValue() );}
        } );

        HashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (HashMap.Entry<String, Integer> entry : list) {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}
