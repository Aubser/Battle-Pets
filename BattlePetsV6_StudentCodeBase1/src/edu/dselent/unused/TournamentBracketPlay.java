package edu.dselent.unused;

import java.util.ArrayList;
import java.util.List;

public class TournamentBracketPlay
{
    private List<Integer> playerList;
    private int playersPerBattle;
    private int numberOfRounds;
    private TournamentBracket<List<Integer>> myTestBracket;

    public TournamentBracketPlay(int playersPerBattle, List<Integer> playerList)
    {
        this.playersPerBattle = playersPerBattle;
        this.playerList = playerList;
        numberOfRounds = (int)Math.ceil(Math.log10(playerList.size()) / Math.log10(playersPerBattle));

        myTestBracket = new TournamentBracket<>();
    }

    public TournamentBracket<List<Integer>> getMyTestBracket()
    {
        return myTestBracket;
    }

    public static void main(String[] args)
    {
        int numberOfPlayers = 13;
        int playersPerBattle = 2;

        List<Integer> playerList = new ArrayList<>();

        for(int i=0; i<numberOfPlayers; i++)
        {
            playerList.add(i+1);
        }

        TournamentBracketPlay testBracket = new TournamentBracketPlay(playersPerBattle, playerList);

        // generate tournament bracket based on list size

        TournamentNode<List<Integer>> rootNode = new TournamentNode<>();
        TournamentBracket<List<Integer>> myTestBracket = testBracket.getMyTestBracket();
        myTestBracket.setRoot(rootNode);

        testBracket.populateTree(numberOfPlayers, rootNode, 0);

    }

    // playersInRound = number of total players fought up and including the current round
    // start at last round and recursively n-ary split
    // do not split if number of children <= number of players per battle
    // can have a split with a node with fewer children then number of players per battle
    // if count < number of players per battle, run battle with less players
    // only create a battle for greater than 1 player

    // XXXXXXX

    // n-ary split, generate list of split numbers
    // number of remaining battles starts at number of players per battle
    // number remaining players in tournament round / number of remaining battles per tournament round
    // for each split number add child node to this current node
    // call populate on each one of the child nodes

    // if number is a '1' add it to the data (list) for current node
    // other wise add it to the child list
    public void populateTree(int playersInRound, TournamentNode<List<Integer>> parentNode, int currentPlayerIndex)
    {
        int currentPlayersPerBattle = playersPerBattle;
        while(playersInRound > playersPerBattle)
        {
            int playersForCurrentBattle = (int)Math.ceil(playersInRound / (currentPlayersPerBattle * 1.0));

            if(playersForCurrentBattle > 1)
            {
                TournamentNode<List<Integer>> childNode = new TournamentNode<>();
                parentNode.addChild(childNode);

                populateTree(playersForCurrentBattle, childNode, currentPlayerIndex);
            }
            else
            {
                // Add player to this battle
                parentNode.getBattle().add(playerList.get(currentPlayerIndex++));
            }

            playersInRound = playersInRound - playersForCurrentBattle;
        }
    }
}
