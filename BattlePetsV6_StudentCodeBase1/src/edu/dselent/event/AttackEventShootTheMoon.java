package edu.dselent.event;

import edu.dselent.damage.Damage;
import edu.dselent.skill.Skills;

import java.util.Objects;

public class AttackEventShootTheMoon extends AttackEvent
{
    private final Skills predictedSkillEnum;

    private AttackEventShootTheMoon(AttackEventShootTheMoonBuilder builder)
    {
        super(builder.attackingPlayableUid, builder.victimPlayableUid, builder.attackingSkillChoice, builder.damage, EventTypes.ATTACK_SHOOT_THE_MOON);
        this.predictedSkillEnum = builder.predictedSkillEnum;
    }

    public AttackEventShootTheMoon(AttackEventShootTheMoon otherEvent)
    {
        super(otherEvent.getAttackingPlayableUid(), otherEvent.getVictimPlayableUid(), otherEvent.getAttackingSkillChoice(), new Damage(otherEvent.getDamage()), EventTypes.ATTACK_SHOOT_THE_MOON);
        this.predictedSkillEnum = otherEvent.predictedSkillEnum;
    }

    public Skills getPredictedSkillEnum()
    {
        return predictedSkillEnum;
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
        if (!super.equals(o))
        {
            return false;
        }
        AttackEventShootTheMoon that = (AttackEventShootTheMoon) o;
        return predictedSkillEnum == that.predictedSkillEnum;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), predictedSkillEnum);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("AttackEventShootTheMoon{");
        sb.append("predictedSkillEnum=").append(predictedSkillEnum);
        sb.append(", attackingPlayableUid=").append(getAttackingPlayableUid());
        sb.append(", victimPlayableUid=").append(getVictimPlayableUid());
        sb.append(", attackingSkillChoice=").append(getAttackingSkillChoice());
        sb.append(", damage=").append(getDamage());
        sb.append(", eventType=").append(getEventType());
        sb.append('}');
        return sb.toString();
    }

    public static class AttackEventShootTheMoonBuilder
    {
        private int attackingPlayableUid;
        private int victimPlayableUid;
        private Skills attackingSkillChoice;
        private Damage damage;
        private Skills predictedSkillEnum = null;

        public AttackEventShootTheMoonBuilder withAttackingPlayableUid(int attackingPlayableUid)
        {
            this.attackingPlayableUid = attackingPlayableUid;
            return this;
        }

        public AttackEventShootTheMoonBuilder withVictimPlayableUid(int victimPlayableUid)
        {
            this.victimPlayableUid = victimPlayableUid;
            return this;
        }

        public AttackEventShootTheMoonBuilder withAttackingSkillChoice(Skills attackingSkillChoice)
        {
            this.attackingSkillChoice = attackingSkillChoice;
            return this;
        }

        public AttackEventShootTheMoonBuilder withDamage(Damage damage)
        {
            this.damage = damage;
            return this;
        }

        public AttackEventShootTheMoonBuilder withPredictedSkillEnum(Skills predictedSkillEnum)
        {
            this.predictedSkillEnum = predictedSkillEnum;
            return this;
        }

        public AttackEventShootTheMoon build()
        {
            return new AttackEventShootTheMoon(this);
        }
    }
}
