package edu.dselent.domain;

import edu.dselent.player.Playable;

import java.util.List;

@FunctionalInterface
public interface Winnable
{
    List<Playable> calculateWinners();
}
