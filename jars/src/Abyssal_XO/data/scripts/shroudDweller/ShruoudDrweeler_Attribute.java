package Abyssal_XO.data.scripts.shroudDweller;

public class ShruoudDrweeler_Attribute {
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
