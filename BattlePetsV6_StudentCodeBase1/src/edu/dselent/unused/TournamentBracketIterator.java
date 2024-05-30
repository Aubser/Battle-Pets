package edu.dselent.unused;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Level order tree traversal
 * Generate a list of nodes for an ordering using BFS
 * Invert the order of the list
 *
 * @param <E> The type of data contained in a tournament node (a battle)
 */
public class TournamentBracketIterator<E> implements Iterator<TournamentNode<E>>
{
    private TournamentBracket<E> tournamentBracket;
    private int currentListIndex;

    private List<TournamentNode<E>> nodeList;

    TournamentBracketIterator(TournamentBracket<E> tournamentBracket)
    {
        this.tournamentBracket = tournamentBracket;
        currentListIndex = 0;
        nodeList = new ArrayList<>();

        TournamentNode<E> currentNode = tournamentBracket.getRootNode();

        if(currentNode != null)
        {
            nodeList.add(currentNode);

            List<TournamentNode<E>> childList = currentNode.getChildList();

            while(!childList.isEmpty())
            {
                List<TournamentNode<E>> grandChildList = new ArrayList<>();

                nodeList.addAll(childList);

                for(TournamentNode<E> child : childList)
                {
                    grandChildList.addAll(child.getChildList());
                }

                childList = grandChildList;
            }
        }

        // Reverse for less confusing use in next method

        List<TournamentNode<E>> reversedList = new ArrayList<>();

        for(int i=nodeList.size()-1; i>= 0; i--)
        {
            reversedList.add(nodeList.get(i));
        }

        nodeList = reversedList;
    }

    @Override
    public boolean hasNext()
    {
        return currentListIndex < nodeList.size();
    }

    @Override
    public TournamentNode<E> next()
    {
        return nodeList.get(currentListIndex++);
    }
}
