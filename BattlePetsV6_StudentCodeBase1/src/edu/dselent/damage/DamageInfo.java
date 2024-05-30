package edu.dselent.damage;

import java.util.Objects;

public class DamageInfo
{
    private int attackingPlayableUid;
    private int victimPlayableUid;
    private Damage damage;

    public DamageInfo(int attackingPlayableUid, int victimPlayableUid, Damage damage)
    {
        this.attackingPlayableUid = attackingPlayableUid;
        this.victimPlayableUid = victimPlayableUid;
        this.damage = damage;
    }

    public int getAttackingPlayableUid()
    {
        return attackingPlayableUid;
    }

    public void setAttackingPlayableUid(int attackingPlayableUid)
    {
        this.attackingPlayableUid = attackingPlayableUid;
    }

    public int getVictimPlayableUid()
    {
        return victimPlayableUid;
    }

    public void setVictimPlayableUid(int victimPlayableUid)
    {
        this.victimPlayableUid = victimPlayableUid;
    }

    public Damage getDamage()
    {
        return damage;
    }

    public void setDamage(Damage damage)
    {
        this.damage = damage;
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
        DamageInfo that = (DamageInfo) o;
        return attackingPlayableUid == that.attackingPlayableUid && victimPlayableUid == that.victimPlayableUid && Objects.equals(damage, that.damage);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(attackingPlayableUid, victimPlayableUid, damage);
    }

}
