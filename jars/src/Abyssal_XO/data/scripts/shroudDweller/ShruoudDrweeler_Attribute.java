package Abyssal_XO.data.scripts.shroudDweller;

public class ShruoudDrweeler_Attribute {
    /*addition hints:
    * 1) ship.isSelectableInWarroom(). maybe this is if I can give this ship comamnds?*/

    /*I am redoing this.
    * first change: primary 'gimmick' of this attribute is fleet wide abilitys, and 'entropy'.
    * Entropy is a resorse gained from doing damage. it is fleet wide, and can be used on fleet wide abilitys.
    * note: most abilitys do not cost 'entropy', but instead happen faster when gaining entropy, or have stronger effects. only some powerful ability consume entropy.
    * 'Entropy' decreases at a rate of 0.1% per second. so 'Entropy' equlizes at certen values. larger battles will create more 'entropy' at a cap, so nothing ever goes out of control in say, battles against threat.
    * POSSABLE CHANGE:
    *   instead of a 'gloabal cooldown' why not have some ability effect the intier map at once? like effect all ships with the same ability at the same time.
    *   for example: 'stike', but it hits my inter fleet. 'Entropy' could increase its stats, so I do mush more damage for the duration.
    *   issue: incombatability with 'Breath'. maybe have some things be passive bonuses, that get stronger as 'entropy' is gathered?
    * possable abilitys:
    *   'Strike'
    *       every ? seconds, increases damage by ? of one random ship and all its fighters.
    *       cooldown multiplexed by 1/2/4/8 depending on hullsize.
    *   manoeuvrability:
    *       ???
    *   'pulse'
    *       every ? seconds, release a damaging pulse from
    *       and do what??? kill all fighters? to mush of a gimic.
    *   'freeze' target target (or maybe just reduce timeflow by like 50%?)
    *       every ? seconds 'freaze' a hostile ship in place for ? seconds.
    *       cooldown multiplied by 1/2/4/8 depending on hullsize.
    *       can only actavate at 10000/30000/90000/120000 'Entropy' depending on hullsize of target.
    *       will only target ships withen 500/1000/1500/2000 range of a friendly ship.
    *
    * 4) 'Hunger':
    *     increase damage taken by 2X on random hostile ship.
    *     cooldown multiplied by 1/2/4/8 depending on hullsize.
    *     can only actavate at 10000/30000/90000/120000 'Entropy' depending on hullsize of target.
    *     will only target ships withen 500/1000/1500/2000 range of a friendly ship.
    *
    * 5) 'Breathe'
    *   for 20 seconds, slowly increase the recharge rate and effect of all 'Controlled Incursion' ability's by up to 100%.
    *   for 20 seconds afterwords, slowly reduce the recharge rate and effect of all 'Controlled Incursion' ability's to 0%.
    *   repeats endlessly.
    *
    * 6) 'Dive'
    *   every 'Entropy'/10000 seconds, arm a single ship with charge of P-Teleporter.
    *   only can trigger at 5000/25000/125000/625000 'entropy' depended on hull size.
    *   cooldown is 1/3/9/27 times longer, depending on hullsize.
    *
    * 7) 'Lesser Manifestation'
    *   every 'Entropy'/5000 seconds, manifest a single 'Thread' on a random ship.
    *   'Threads' are fighter class ships armed with a single 'small rift weapon'. they have low resilience.
    *   'Threads' rapidly destabilize one manifest, lasting at most 15 seconds before departing.
    *
    *
    * 8) 'Seeking Tendrils'
    *   every 'Entropy'/5000 seconds, send a rift arc from a random ship, targeting a random hostile ship in 500/1000/1500/2000 range, depending on hullsize.
    *   deals 250 damage per shot.
    *   can fire at most 6/12/18/24 times per second, depending on ship size. if more arcs are selected for a ship, instead make the arcs stronger, up to 5 times stronger.
    *
    *
    * (at least 4?)
    * 9) 'Reformation'
    *   when a ship would die, restore it to full HP, and increase timeflow (like the abyssal ability from rat). costs DP * 5000 'Entropy' per ship?
    *   60 second cooldown per ship.
    *   cooldown is increased by 60 seconds every time it is used on the same ship.
    * OR?
    * 10) 'Manifestation'
    *   spend ALL gathered 'Entropy' spawn some cool ass ship.
    *   said cool ass ship gaines 25% of the 'Entropy' spent for itself, and its ability's.
    *   said cool ass ship keeps up to 25% of all newly gathered 'Entropy' in reserve for itself.
    *   gains the following ability's:
    *   long range P-Teleporter
    *   Applied energy
    *   Lunge
    *   Entropy Amplification
    *   if killed, all 'Entropy' stored in reserve for itself is lost.
    *   cannot have more then one 'Manifestation' active at a time.
    *   cannot create a new 'Manifestation' until this one dies. must wait 60 seconds after this ones dies to activate again.
    *
    *
    *
    *
    *
    *
    *
    *
    *
    * */


    /*notes on how this works:
    * all ships in your fleet
    * at the start of combat, manifests one 'Shrouded Incursion' on your side of the map. It cannot move, and if it is destroyed it will lose all 'Abyssal Charges' and respawn in 60 seconds.
    * 100 seconds after the 'Shrouded Incursion' is manifest it will transform into one 'Shrouded Entity', and loss all stored 'Abyssal Charge'. The most 'Abyssal Charge' it had, the stronger the resalting 'Shrouded Entity' will be, costing up to 100 deployment points.
    * when the 'Shrouded Entity' is banished, the 'Shrouded Incursion' will re-manifest
    * the 'Shrouded Entity' will not obey your commands, but it will attack your enamys with reckless abandon.
    * if you no longer have any ships deployed, the 'Shrouded Entity' and 'Shrouded Incursion' will start to withdraw from the reality, quickly losing hull.
    * for every X ponits to damage your weapons deal, generate 1 'abyssal charge'. only generate 50% 'abyssal charge' for damage on shields. 'abyssal charge' will not generate if the 'Shrouded Incursion' is currently banished.*/
    /*skills: we need 9
    * 1) preemptive entry: every 20 seconds, manifest 1 'Shrouded Thread' for every X 'abyssal charges' from your 'Shrouded Incursion'. the 'Shrouded Thread' are armed with a single (that 10 cost PD wepon), and wil die after 120 seconds. does not consume 'abyssal charge's to create.
    * 2) I dont know.... something. like a mobility skill? I dont know... arg....
    * 3) Abyssal Synergy: abyssal ships, shrouded entity's and ships with abyssal hullmods, create Abyssal Charges twice as mush abyssal charges from damage dealt.
    * 4) Nightmare: when the 'Shrouded Entity's hits a hostile ship, it will cause 'destabilization' for 5 seconds. ships that are 'destabilized' and are attempting to flee the 'Shrouded Entity' move at 50% speed
    * 5) Awaking: When the 'Shrouded Entity' is manifested, it will have 3X time-flow for 30 second.
    *
    * 6) The Pact: the 'Shrouded Entity' gains 5 elite skills. The 'Shrouded Entity' will now obey most of your commands.
    * 7) Twin Incursion: manifests two 'Shrouded Incursion' instead of one at the start of combat. each 'Shrouded Incursion' will only collect 'Abyssal Charge' at 50% speed.
    *
    * 8) Harvest: when the 'Shrouded Entity' is manifest, 'Abyssal Charges' will not be discarded, instead they will be used to restore the 'Shrouded Entity' hull at a rate of 1 hull per x 'Abyssal Charges'. If 'preemptive entry' is active, the 'Shrouded Entity' will manifest 'Shrouded Thread'.
    * or
    * 9) Stable Incursion: When the 'Shrouded Entity' manifests, the 'Shrouded Incursion' will remain. It will only transform into a 'Shrouded Entity' 100 seconds after the current 'Shrouded Entity' is banished.
    * */
}
