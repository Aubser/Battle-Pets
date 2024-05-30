package edu.dselent.unused;

import java.util.Iterator;

public class TournamentBracket<E> implements Iterable<TournamentNode<E>>
{
    private TournamentNode<E> rootNode;

    public TournamentBracket()
    {
        rootNode = null;
    }

    public void setRoot(TournamentNode<E> rootNode)
    {
        if(rootNode.getContainingBracket() != null)
        {
            throw new IllegalArgumentException("Cannot set root node.  Current node is already contained in another Tournment node");
        }

        this.rootNode = rootNode;
        rootNode.setContainingBracket(this);
    }

    public TournamentNode<E> getRootNode()
    {
        return rootNode;
    }

    @Override
    public Iterator<TournamentNode<E>> iterator()
    {
        return new TournamentBracketIterator<E>(this);
    }
}
