package edu.dselent.player.spring2022.team05.AI;

import edu.dselent.event.*;
import edu.dselent.player.Playable;
import edu.dselent.player.Player;
import edu.dselent.player.PlayerTypes;
import edu.dselent.player.PetTypes;
import edu.dselent.settings.PlayerSettings;
import edu.dselent.skill.Skills;

import java.util.*;

/**
 * Team 05 playable is a SPEED pet logic which provides implementations for choosing skills.
 */
public class Team05AI implements Playable {
   int UID;
   private String namePet;
   private Player player;
   private PetTypes petType;
   private Double startingHp;
   private Double currentHp;
   private Integer standardPatternCounter;
   private Integer shootTheMoonPatternCounter;
   private Double attackerConditionalDamage;
   private Double randomDamageDifferential = 0.0;
   private int attackerIndex = -1;
   private int defenderIndex = -1;
   private Map<Skills, Integer> rechargeTimes = new HashMap<>();
   private List<InferredPet> opponentList;
   private List<PlayerEventInfo> eventInfoList = new ArrayList<>();
   private Random internalRandomizer = new Random();
   private int shootTheMoonPredict = 0;

   /**
    * Instantiate a new Team05AI.
    * @param uid - Unique identifier for the pet.
    * @param settings - A PlayerSettings to assign the skillset and name/type. [Type must be speed.]
    */
   public Team05AI(int uid, PlayerSettings settings)
   {
      standardPatternCounter = 0;
      shootTheMoonPatternCounter = 0;
      attackerConditionalDamage = 0.0;

      opponentList = new ArrayList<>();
      namePet = settings.getPetName();
      petType = settings.getPetType();
      startingHp = settings.getStartingHp();
      currentHp = startingHp;
      UID = uid;
      //Initialize skill recharge times.
      for(Skills s : settings.getSkillSet())
      {
         rechargeTimes.putIfAbsent(s, 0);
      }
      player = new Player(settings.getPlayerName(), settings.getPlayerType());

   }

   /**
    * Selects a skill based on the type of the assumed defender.
    * @return A Skills that the pet will use.
    */
   @Override
   public Skills chooseSkill() {
      Skills targetSkill = null;

      if(defenderIndex != -1 && targetSkill == null)
      {
         InferredPet defender = opponentList.get(defenderIndex);
         if(defender.petType == PetTypes.INTELLIGENCE) {
            targetSkill = chooseAgainstIntelligence();
         }
         // choose skill for power opponent.
         else if(defender.petType == PetTypes.POWER){
            targetSkill = chooseAgainstPower();
         } else if(defender.petType == PetTypes.SPEED){
            targetSkill = chooseAgainstSpeed();
         }
      }
      return targetSkill;
   }

   /**
    * Returns the assumed skill the opponent will use based on their type.
    * @return The Skills the prediction methods assume will be used.
    */
   @Override
   public Skills getSkillPrediction() {
      Skills targetSkill = null;

      if(defenderIndex != -1)
      {
         InferredPet defender = opponentList.get(defenderIndex);
         if(defender.petType == PetTypes.INTELLIGENCE)
         {
            targetSkill = predictSkillAgainstIntelligence();
         }
         else if (defender.petType == PetTypes.POWER){
            targetSkill = predictSkillAgainstPower();
         } else if (defender.petType == PetTypes.SPEED) {
            targetSkill = predictSkillAgainstSpeed();
         }
      }

      return targetSkill;
   }

   /**
    * Estimates whether using Reversal of Fortune should do more than our average damage output.
    * @return Returns true when estimated damage exceeds average damage output.
    */
   public boolean useReversalOfFortune(){
      double guaranteedDamage = 2*randomDamageDifferential;
      double expectedDamage = guaranteedDamage + 2.5;
      boolean useROF;
      InferredPet defender = opponentList.get(defenderIndex);

      if(guaranteedDamage <= 0)
         useROF = false;
      else if (defender.currentHp < guaranteedDamage)
         useROF = true;
      else if ((defender.getPercentHp() >= 75 && !(rechargeTimes.get(Skills.ROCK_THROW) == 0)) ||
              (defender.getPercentHp() >= 25 && defender.getPercentHp() < 75
                      && rechargeTimes.get(Skills.SCISSORS_POKE) == 0) || (defender.getPercentHp() >= 0 && defender.getPercentHp() < 25
              && rechargeTimes.get(Skills.PAPER_CUT) == 0)){
         if (expectedDamage > 15.0)
            useROF = true;
         else
            useROF = false;
      }
      else{
         if (expectedDamage > 7.5)
            useROF = true;
         else
            useROF = false;
      }
      return useROF;
   }

   /**
    * Adrian's Predict (Changed)
    * @return
    */
   public Skills predictSkillAgainstPower() {
      InferredPet victim = opponentList.get(defenderIndex);
      Skills predictedSkill = null;
      int shootTheMoonCount = 0;

      if(victim.rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0){
         predictedSkill = Skills.SHOOT_THE_MOON;
      }
      else if (victim.getPercentHp() >= 75) {
         // we could use this for shoot the moon prediction... Let me know if you want me to explain it.--Mamadou
         if (victim.rechargeTimes.get(Skills.PAPER_CUT) == 0) {
            predictedSkill = Skills.PAPER_CUT;
         }
         else
            predictedSkill = Skills.ROCK_THROW;
      }
      else if (victim.getPercentHp() > 25 && victim.getPercentHp() < 75) {
         if (victim.rechargeTimes.get(Skills.SCISSORS_POKE) == 0) {
            predictedSkill = Skills.SCISSORS_POKE;
         }
         else
            predictedSkill = Skills.ROCK_THROW;
      }
      else if (victim.getPercentHp() < 25) {
         if (victim.rechargeTimes.get(Skills.SCISSORS_POKE) > 0) {
            predictedSkill = Skills.SCISSORS_POKE;
         }
         else
            predictedSkill = Skills.PAPER_CUT;
      }
      return predictedSkill;
   }

   /**
    * Adrian
    * @return
    */
   public Skills chooseAgainstPower() {
      InferredPet opponent = opponentList.get(defenderIndex);
      Skills condSkill = null;

      if(opponent.getPercentHp() > 75) {
         if(rechargeTimes.get(Skills.ROCK_THROW) == 0) {
            condSkill = Skills.ROCK_THROW;
         }
         else if (predictSkillAgainstPower() == Skills.ROCK_THROW && rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0) {
            condSkill = Skills.SHOOT_THE_MOON;
         }
         else if (rechargeTimes.get(predictSkillAgainstPower()) == 0) {
            condSkill = predictSkillAgainstPower();
         }
         else
         {
            condSkill = Skills.PAPER_CUT;
         }
      }
      else if (opponent.getPercentHp() > 25) {
         if(rechargeTimes.get(Skills.SCISSORS_POKE) == 0) {
            condSkill = Skills.SCISSORS_POKE;
         }
         else if (predictSkillAgainstPower() == Skills.SCISSORS_POKE && rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0) {
            condSkill = Skills.SHOOT_THE_MOON;
         }
         else if (rechargeTimes.get(predictSkillAgainstPower()) == 0) {
            condSkill = predictSkillAgainstPower();
         }
         else {
            condSkill = Skills.ROCK_THROW;
         }
      }
      else {
         if(rechargeTimes.get(Skills.PAPER_CUT) == 0) {
            condSkill = Skills.PAPER_CUT;
         }
         else if (predictSkillAgainstPower() == Skills.PAPER_CUT && rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0) {
            condSkill = Skills.SHOOT_THE_MOON;
         }
         else if (rechargeTimes.get(predictSkillAgainstPower()) == 0) {
            condSkill = predictSkillAgainstPower();
         }
         else {
            condSkill = Skills.ROCK_THROW;
         }
      }

      if (rechargeTimes.get(Skills.REVERSAL_OF_FORTUNE) == 0)
      {
         if (useReversalOfFortune())
         {
            condSkill = Skills.REVERSAL_OF_FORTUNE;
         }
      }

      if(condSkill == null) {
         if(rechargeTimes.get(Skills.SCISSORS_POKE) == 0) {
            condSkill = Skills.SCISSORS_POKE;
         } else {
            condSkill = Skills.PAPER_CUT;
         }
      }

      return condSkill;
   }

   /**
    * Selects a skill when the opponent is an Intelligence pet.
    * @return The skill assumed best given the known state information.
    */
   public Skills chooseAgainstIntelligence(){
      int pointsToRockThrow = 0;
      int pointsToScissorPoke = 0;
      int pointsToPaperCut = 0;
      InferredPet defender = opponentList.get(defenderIndex);
      InferredPet attacker = opponentList.get(attackerIndex);
      Skills chosenSkill = null;
      //choose reversal of fortune if...
      //shoot the moon (guaranteed 12 damage if used) -- do with counter!

      //check if we can get the condition damage
      if (defender.getPercentHp() >= 75 && rechargeTimes.get(Skills.ROCK_THROW) == 0){
         if (defender.rechargeTimes.get(Skills.ROCK_THROW) != 0){
            pointsToRockThrow++;
         }
      }
      else if (defender.getPercentHp() >= 25 && defender.getPercentHp() < 75
              && rechargeTimes.get(Skills.SCISSORS_POKE) == 0) {
         if (defender.rechargeTimes.get(Skills.SCISSORS_POKE) != 0){
            pointsToScissorPoke++;
         }
      }
      else if (defender.getPercentHp() >= 0 && defender.getPercentHp() < 25
              && rechargeTimes.get(Skills.PAPER_CUT) == 0){
         if (defender.rechargeTimes.get(Skills.PAPER_CUT) != 0){
            pointsToPaperCut++;
         }
      }

      //if we can't take condition, take the least amount of damage if skill is not recharging
      if (pointsToPaperCut == pointsToRockThrow && pointsToPaperCut == pointsToScissorPoke){
         if (attacker.rechargeTimes.get(Skills.ROCK_THROW) != 0
                 && rechargeTimes.get(Skills.PAPER_CUT) == 0){
            chosenSkill = Skills.PAPER_CUT;
         }
         else if (attacker.rechargeTimes.get(Skills.SCISSORS_POKE) != 0
                 && rechargeTimes.get(Skills.ROCK_THROW) == 0){
            chosenSkill = Skills.ROCK_THROW;
         }
         else if (attacker.rechargeTimes.get(Skills.PAPER_CUT) != 0
                 && rechargeTimes.get(Skills.SCISSORS_POKE) == 0){
            chosenSkill = Skills.SCISSORS_POKE;
         }
      }
      //if we can do conditional, do the appropriate skill for conditional damage
      else
      {
         if (pointsToRockThrow > pointsToPaperCut && pointsToRockThrow > pointsToScissorPoke){
            chosenSkill = Skills.ROCK_THROW;
         }
         else if (pointsToScissorPoke > pointsToPaperCut && pointsToScissorPoke > pointsToRockThrow){
            chosenSkill = Skills.SCISSORS_POKE;
         }
         else if (pointsToPaperCut > pointsToScissorPoke && pointsToPaperCut > pointsToRockThrow){
            chosenSkill = Skills.PAPER_CUT;
         }
      }

      if (chosenSkill == null){
         if (rechargeTimes.get(Skills.ROCK_THROW) == 0){
            chosenSkill = Skills.ROCK_THROW;
         }
         else{
            chosenSkill = Skills.PAPER_CUT;
         }
      }

      return chosenSkill;
   }

   /**
    * Estimates which skill an intelligence pet will use.
    * @return The assumed skill the opponent will use given the current state.
    */
   public Skills predictSkillAgainstIntelligence(){
      Skills predictedSkill = null;
      InferredPet defender = opponentList.get(defenderIndex);
      if (defender.rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0){
         predictedSkill = Skills.SHOOT_THE_MOON;
      }
      else if (rechargeTimes.get(Skills.ROCK_THROW) != 0){
         if (defender.rechargeTimes.get(Skills.PAPER_CUT) == 0){
            predictedSkill = Skills.PAPER_CUT;
         }
         else{
            predictedSkill = Skills.ROCK_THROW;
         }
      }
      else if (rechargeTimes.get(Skills.SCISSORS_POKE) != 0){
         if (defender.rechargeTimes.get(Skills.ROCK_THROW) == 0){
            predictedSkill = Skills.ROCK_THROW;
         }
         else{
            predictedSkill = Skills.SCISSORS_POKE;
         }
      }
      else if (rechargeTimes.get(Skills.PAPER_CUT) != 0){
         if (defender.rechargeTimes.get(Skills.SCISSORS_POKE) == 0){
            predictedSkill = Skills.SCISSORS_POKE;
         }
         else{
            predictedSkill = Skills.PAPER_CUT;
         }
      }
      else{
         predictedSkill = Skills.ROCK_THROW;
      }
      return predictedSkill;
   }

   /**
    * Selects a skill based on which skill a Speed pet is likely to use.
    * @return The skill to be used.
    */
   public Skills chooseAgainstSpeed(){
      InferredPet victim = opponentList.get(defenderIndex);
      Skills chosenSkill = null;
      boolean skipRoF = false;

      if(victim.getPercentHp() > 75) {
         //We want to Rock Throw.
         if(rechargeTimes.get(Skills.ROCK_THROW) == 0) {
            chosenSkill = Skills.ROCK_THROW;
            skipRoF = true;
         } else if (predictSkillAgainstSpeed() == Skills.ROCK_THROW && rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0) {
            //We can't, and they can get a guaranteed damage bonus?
            chosenSkill = Skills.SHOOT_THE_MOON;
         } else if (rechargeTimes.get(predictSkillAgainstSpeed()) == 0){
            //We can't, and we can't rely on StM. Force random damage.
            chosenSkill = predictSkillAgainstSpeed();
         } else {
            //Can't force random? Choose skill least likely to be needed next round.
            chosenSkill = Skills.PAPER_CUT;
         }
      } else if (victim.getPercentHp() > 25) {
         //We want to Scissor Poke.
         if(rechargeTimes.get(Skills.SCISSORS_POKE) == 0) {
            chosenSkill = Skills.SCISSORS_POKE;
            skipRoF = true;
         } else if (predictSkillAgainstSpeed() == Skills.SCISSORS_POKE && rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0) {
            //We can't, and they can get a guaranteed damage bonus?
            chosenSkill = Skills.SHOOT_THE_MOON;
         } else if (rechargeTimes.get(predictSkillAgainstSpeed()) == 0){
            //We can't, and we can't rely on StM. Force random damage.
            chosenSkill = predictSkillAgainstSpeed();
         } else {
            //Can't force random? Choose skill least likely to be needed next round.
            chosenSkill = Skills.ROCK_THROW;
         }
      } else {
         //We want to Paper Cut.
         if(rechargeTimes.get(Skills.PAPER_CUT) == 0) {
            chosenSkill = Skills.PAPER_CUT;
            skipRoF = true;
         } else if (predictSkillAgainstSpeed() == Skills.PAPER_CUT && rechargeTimes.get(Skills.SHOOT_THE_MOON) == 0) {
            //We can't, and they can get a guaranteed damage bonus?
            chosenSkill = Skills.SHOOT_THE_MOON;
         } else if (rechargeTimes.get(predictSkillAgainstSpeed()) == 0){
            //We can't, and we can't rely on StM. Force random damage.
            chosenSkill = predictSkillAgainstSpeed();
         } else {
            //Can't force random? Choose skill least likely to be needed next round.
            chosenSkill = Skills.ROCK_THROW;
         }
      }

      if(useReversalOfFortune() && rechargeTimes.get(Skills.REVERSAL_OF_FORTUNE) == 0 && !skipRoF)
      {
         chosenSkill = Skills.REVERSAL_OF_FORTUNE;
      }

      if(chosenSkill == null) {
         //Did we hit the edge case where we had no reasonable options right after changing HP brackets?
         if(rechargeTimes.get(Skills.SCISSORS_POKE) == 0) {
            chosenSkill = Skills.SCISSORS_POKE;
         } else {
            chosenSkill = Skills.PAPER_CUT;
         }
      }

      return chosenSkill;
   }

   /**
    * Predicts which skill a Speed pet will used based on their optimal damage.
    * @return The skill the opponent pet wants to use.
    */
   private Skills predictSkillAgainstSpeed() {
      InferredPet victim = opponentList.get(defenderIndex);
      Skills predictedSkill = null;

      if(calculateHpPercent() > 75)
      {
         if(victim.rechargeTimes.get(Skills.ROCK_THROW) == 0) {
            predictedSkill = Skills.ROCK_THROW;
         } else {
            predictedSkill = Skills.SCISSORS_POKE;
         }
      } else if(calculateHpPercent() > 25) {
         if(victim.rechargeTimes.get(Skills.SCISSORS_POKE) == 0) {
            predictedSkill = Skills.SCISSORS_POKE;
         } else {
            predictedSkill = Skills.ROCK_THROW;
         }
      } else {
         if(victim.rechargeTimes.get(Skills.PAPER_CUT) == 0) {
            predictedSkill = Skills.PAPER_CUT;
         } else {
            predictedSkill = Skills.ROCK_THROW;
         }
      }

      return predictedSkill;
   }

   /**
    * Tracks the amount of times we've been explicitly countered with Shoot the Moon.
    */
   public void checkPattern(){
      InferredPet attacker = opponentList.get(attackerIndex);
      if (attackerConditionalDamage != 0){
         standardPatternCounter++;
         if (attackerConditionalDamage == 20.0){
            shootTheMoonPatternCounter++;
         }
      }
   }

   /**
    * Called when a new battle event occurs, and handles the event.
    * @param event The event sent to the pet.
    */
   @Override
   public void update(Object event) {
      if(event instanceof  BaseEvent)
      {
         EventTypes eventType = ((BaseEvent) event).getEventType();
         if(eventType == EventTypes.ATTACK || eventType == EventTypes.ATTACK_SHOOT_THE_MOON)
         {
            AttackEventHandler((AttackEvent)event);
         }
         else if(eventType == EventTypes.ROUND_START)
         {
            //Do we do anything with this?
         }
         else if(eventType == EventTypes.FIGHT_START)
         {
            //Clear opponent data.
            opponentList = new ArrayList<>();
            attackerIndex = -1;
            defenderIndex = -1;
            randomDamageDifferential = 0.0;
            standardPatternCounter = 0;
            FightStartEventHandler((FightStartEvent) event);
         }

      }

   }

   /**
    * Gathers information from an Attack event.
    * @param event The Attack event that was issued.
    */
   private void AttackEventHandler(AttackEvent event)
   {
      int attackerUid = event.getAttackingPlayableUid();
      int defenderUid = event.getVictimPlayableUid();

      if(attackerUid == UID) {
         //Are we attacking?
         randomDamageDifferential -= event.getDamage().getRandomDamage();
         if(opponentList.get(defenderIndex).Uid != defenderUid) {
            for(int i = 0; i < opponentList.size(); i++) {
               if (opponentList.get(i).Uid == defenderUid)
               {
                  defenderIndex = i;
               }
            }
         }
         opponentList.get(defenderIndex).applyDamage(event.getDamage().calculateTotalDamage());
      } else if(defenderUid == UID) {
         //Are we being attacked?
         randomDamageDifferential += event.getDamage().getRandomDamage();
         attackerConditionalDamage = event.getDamage().getConditionalDamage();
         if (attackerConditionalDamage > 0){    //maybe add a condition for != 20
            // so that it doesn't take shoot the moon into account
            checkPattern();
         }
         Skills skill = event.getAttackingSkillChoice();
         if(opponentList.get(attackerIndex).Uid != attackerUid) {
            for(int i = 0; i < opponentList.size(); i++){
               if(opponentList.get(i).Uid == attackerUid)
               {
                  attackerIndex = i;
               }
            }
         }
         opponentList.get(attackerIndex).decrementSkills();
         opponentList.get(attackerIndex).rechargeTimes.replace(skill, InferredPet.rechargeType(skill));
      } else {
         //A pet that isn't our target or attacker is being attacked. Try to track their HP in case we fight them later.
         int targetIndex = -1;
         for(int i = 0; i < opponentList.size(); i++){
            if(opponentList.get(i).Uid == attackerUid) {
               targetIndex = i;
            }
         }
         if(targetIndex != -1) {
            opponentList.get(targetIndex).applyDamage(event.getDamage().calculateTotalDamage());
         }
      }

   }

   /**
    * Gathers information from a Fight Start event.
    * @param event The event that was issued.
    */
   private void FightStartEventHandler(FightStartEvent event)
   {
      boolean pastPlayer = false;
      eventInfoList = event.getPlayerEventInfoList();
      for(PlayerEventInfo info : eventInfoList)
      {
         if(!pastPlayer) {
            //If we haven't found our self yet, we assume the player before us will be attacking us.
            if(info.getPlayableUid() != UID)
            {
               opponentList.add(new InferredPet(info.getPlayableUid(), info.getPetType(), info.getStartingHp()));
               attackerIndex = opponentList.size()-1;
            } else {
               pastPlayer = true;
            }

            //Once we find our self, we assume the next pet is the defender.
         } else {
            opponentList.add(new InferredPet(info.getPlayableUid(), info.getPetType(), info.getStartingHp()));
            if(defenderIndex == -1) {
               defenderIndex = opponentList.size()-1;
            }
         }
      }

      //If we've gotten an attacker or defender but not the other, we can assume there's only two pets.
      //Our pet, and the target. Both attacker and defender will be set to the same target.
      if(attackerIndex == -1)
      {
         attackerIndex = defenderIndex;
      }
      if(defenderIndex == -1)
      {
         defenderIndex = attackerIndex;
      }

   }

   /**
    *
    * Provided by parent class, unsure what this is for.
    * Commented out since we were asked not to override it.
    * @return
    *
    * @Override
    * public Set<Skills> getSkillSet() {
    *       return Playable.super.getSkillSet();
    *    }
    */


   /**
    * Returns the unique identifier of the pet.
    * @return The UID of the pet.
    */
   @Override
   public int getPlayableUid() {
      return UID;
   }

   /**
    * Sets the unique identifier of the pet.
    * @param playableUid ID to set our UID to.
    */
   @Override
   public void setPlayableUid(int playableUid) {
      UID = playableUid;
   }

   /**
    * Returns the name of the player.
    * @return The name of the player.
    */
   @Override
   public String getPlayerName() {
      return player.getName();
   }

   /**
    * Returns the name of our pet.
    * @return Pet's name.
    */
   @Override
   public String getPetName() {
      return namePet;
   }

   /**
    * Returns our player type (should always be TEAM_05)
    * @return The player type.
    */
   @Override
   public PlayerTypes getPlayerType() {
      return player.getPlayerType();
   }

   /**
    * Returns the type of the pet. (Should always be SPEED.)
    * @return The pet type.
    */
   @Override
   public PetTypes getPetType() {
      return petType;
   }

   /**
    * Returns our starting HP, usually 100.
    * @return Pet's starting HP.
    */
   @Override
   public double getStartingHp() {
      return startingHp;
   }

   /**
    * Sets our current hitpoints.
    * @param currentHp Value to set hit points to.
    */
   @Override
   public void setCurrentHp(double currentHp) {
      this.currentHp = currentHp;
   }

   /**
    * Returns our current hit points.
    * @return Current hit points.
    */
   @Override
   public double getCurrentHp() {
      return currentHp;
   }

   /**
    * Removes HP from the pet.
    * @param hp Amount to remove.
    */
   @Override
   public void updateHp(double hp) {
      currentHp -= hp;
   }

   /**
    * Restores pet's HP to starting HP.
    */
   @Override
   public void resetHp() {
      currentHp = startingHp;
   }

   /**
    * Returns true if the pet is awake, i.e. their HP is greater than 0.
    * @return boolean of pet's awake state.
    */
   @Override
   public boolean isAwake() {
      return currentHp > 0;
   }

   /**
    * Returns recharge time of a given skill.
    * @param skill Skill to check recharge of.
    * @return Amount of turns remaining.
    */
   @Override
   public int getSkillRechargeTime(Skills skill) { return rechargeTimes.get(skill); }

   /**
    * Returns the current percentage of our HP, current vs starting HP.
    * @return Percentage of hit points left.
    */
   @Override
   public double calculateHpPercent() {
      return ((currentHp/startingHp) * 100);
   }

   /**
    * Resets pet to start a new fight. Initializes recharge times and HP.
    */
   @Override
   public void reset() {
      resetHp();
      rechargeTimes = new HashMap<>();
      for(Skills s : Playable.super.getSkillSet())
      {
         rechargeTimes.putIfAbsent(s, 0);
      }
   }

   /**
    * Recovers recharge times by 1 each round.
    */
   @Override
   public void decrementRechargeTimes() {
      for(Map.Entry<Skills, Integer> entry : rechargeTimes.entrySet())
      {
         if(entry.getValue() > 0) {
            rechargeTimes.replace(entry.getKey(), entry.getValue() - 1);
         }
      }
   }

   /**
    * Sets the recharge time of a skill.
    * @param skill Skill to set recharging.
    * @param rechargeTime Amount of time.
    */
   @Override
   public void setRechargeTime(Skills skill, int rechargeTime) {
      rechargeTimes.replace(skill, rechargeTime);
   }

   /**
    * Tracks our assumed opponent statistics.
    */
   private static class InferredPet {
      private PetTypes petType;
      private Double startingHp;
      private Double currentHp;
      private Map<Skills, Integer> rechargeTimes = new HashMap<>();
      private int Uid;

      /**
       * Initializes a shorthand pet class to store data.
       * @param uid UID of the target pet.
       * @param type Type of the target pet.
       * @param hp Max hitpoints of the target pet.
       */
      private InferredPet(int uid, PetTypes type, Double hp){
         Uid = uid;
         petType = type;
         startingHp = hp;
         currentHp = hp;
         for(Skills s : Skills.values())
         {
            rechargeTimes.putIfAbsent(s, 0);
         }
      }

      /**
       * Reduces recharge on inferred pet skills.
       */
      private void decrementSkills() {
         for(Map.Entry<Skills, Integer> entry : rechargeTimes.entrySet()) {
            if(entry.getValue() > 0) {
               rechargeTimes.replace(entry.getKey(), entry.getValue() - 1);
            }
         }
      }

      /**
       * Assumed remaining percentage of HP.
       * @return Double representing percentage of HP.
       */
      private Double getPercentHp(){ return ((currentHp/startingHp) * 100); }

      /**
       * Reduces target pet's ESTIMATED health.
       * @param damage Amount to reduce health by.
       */
      private void applyDamage(Double damage) { currentHp -= damage; }

      /**
       * Resets our assumed cooldowns for opponent skills.
       */
      private void resetSkills() { for(Skills s : Skills.values()) { rechargeTimes.putIfAbsent(s, 0);} }

      /**
       * Gets the recharge time a skill should have.
       * 1 if it's a standard skill, or 6 if it's StM or RoF.
       * @param skill Skill to recharge.
       * @return Amount of time it recharges for.
       */
      static public int rechargeType(Skills skill)
      {
         int rechargeValue = 1;
         int RECHARGE_LONG = 6;

         if(skill.ordinal() > Skills.PAPER_CUT.ordinal())
         {
            rechargeValue = RECHARGE_LONG;
         }

         return rechargeValue;
      }
   }
}
