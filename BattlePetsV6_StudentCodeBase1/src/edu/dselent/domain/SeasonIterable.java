package edu.dselent.domain;

import java.util.Iterator;

/**
 *
 * @param <E> Kept generic, but could probably specify Battle
 */
public interface SeasonIterable<E> extends Iterable<E>
{
    Iterator<E> iterator2();
}
