package edu.pitt.votingsystem;

import java.util.*;

/**
 * Created by craigmazzotta on 3/1/17.
 */

public class TallyTable {
    HashMap<String, String> Voters;  		//<Voterphone#, VoterID>
    HashMap<String, Integer> Votes;		 	//<VoterID, #ofVotes>
    //List<String> candidates;

    /*
    * Constructor for class
     */
    public TallyTable(String[] cand) {

        Voters = new HashMap<String, String>();
        Votes = new HashMap<String, Integer>();

        for(int i=0; i<cand.length; i++){ //initialize votes for each id to 0
            Votes.put(cand[i], 0);
        }

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
                int numVotes = Votes.get(candID);
                Votes.put(candID, numVotes + 1);

                return 1;
            }
        } else return 3;

    }

    /*
    * Returns the number of winners as a string of N CandidateID,NumberVote separated by semicolons
    *
    * @return result The sorted HashMap of candIds and their votes.
     */
    public HashMap<String,Integer> getWinner() {
        HashMap<String, Integer> map = sortByValues(Votes);
        return map;
    }

    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}


