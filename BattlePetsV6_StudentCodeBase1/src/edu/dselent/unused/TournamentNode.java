package edu.dselent.unused;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO hash code and equals
public class TournamentNode<E>
{
    private TournamentBracket<E> containingBracket;
    private TournamentNode<E> parentNode;
    private E battle;
    private List<TournamentNode<E>> childList;

    public TournamentNode(TournamentNode<E> parentNode, TournamentBracket<E> containingBracket)
    {
        this.containingBracket = containingBracket;
        this.parentNode = parentNode;
        // where to initialize battle object?
        childList = new ArrayList<>();
    }

    public TournamentNode(TournamentBracket<E> containingBracket)
    {
        this.containingBracket = containingBracket;
        parentNode = null;
        // where to initialize battle object?
        childList = new ArrayList<>();
    }

    public TournamentNode()
    {
        this.containingBracket = null;
        parentNode = null;
        // where to initialize battle object?
        childList = new ArrayList<>();
    }

    public void setContainingBracket(TournamentBracket<E> containingBracket)
    {
        this.containingBracket = containingBracket;
    }

    public TournamentBracket<E> getContainingBracket()
    {
        return containingBracket;
    }

    public void removeContainingBracket()
    {
        this.containingBracket = null;
    }

    public boolean addChild(TournamentNode<E> child)
    {
        if(child.getParentNode() != null)
        {
            throw new IllegalArgumentException("Cannot add child.  Current node already has a parent");
        }

        return childList.add(child);
    }

    public boolean removeChild(TournamentNode<E> child)
    {
        boolean removed = childList.remove(child);

        if(removed)
        {
            child.setParentNode(null);
        }

        return removed;
    }

    public List<TournamentNode<E>> getChildList()
    {
        return childList;
    }

    public TournamentNode<E> getChild(int index)
    {
        return childList.get(index);
    }

    public TournamentNode<E> setChild(int index, TournamentNode<E> child)
    {
        TournamentNode<E> returnNode;

        if(child.getContainingBracket() != null)
        {
            throw new IllegalArgumentException("Cannot set child.  Current node is already contained in a Tournment");
        }
        else
        {
            TournamentNode<E> oldChild = childList.get(index);
            oldChild.setContainingBracket(null);

            returnNode = childList.set(index, child);
            child.setContainingBracket(containingBracket);
        }

        return returnNode;
    }

    public TournamentNode<E> getParentNode()
    {
        return parentNode;
    }

    private void setParentNode(TournamentNode<E> parentNode)
    {
        this.parentNode = parentNode;
    }

    public E getBattle()
    {
        return battle;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        TournamentNode<?> that = (TournamentNode<?>) o;
        return Objects.equals(parentNode, that.parentNode) && Objects.equals(battle, that.battle) && childList.equals(that.childList);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(parentNode, battle, childList);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("TournamentNode{");
        sb.append("battle=").append(battle);
        sb.append(", childList=").append(childList);
        sb.append('}');
        return sb.toString();
    }
}
