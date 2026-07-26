package Abyssal_XO.data.scripts.omega;

public class Omega_Attribute {
    /*how will this work?
so the idea: buff a single ship in your fleet. The inter XO will be buffing just one ship.
if the ship dies, buff a different ship (so the death effect is not wasted.)
possible issue: might be OP
If I require a special remorse, call it 'cognition'. No clue how it would work. Based around how many 'other' ships there are in the fleet? breaks the point of the XO...
theams:
    I think I will theam the skills around some luddic things. mainly the 'false god of light'.
    I dont know mush about this topic, but it feels fitting to call omega a false god of light.

Power of XO for different power ships:
    What I should do is make the cooldowns of this ship become more extreme the lower DP a ship is.
    balance the skills for a DP of 40. At 20 dp, let skills charge twice as fast. 10, 2x as fast.
    so at 5/10/20/40/60/80 = 2.5/2/1.5/1/0.5/0.25. something like that.
                 so, past 40 dp, every 20 dp slows the skills by 50%, multiplicative.                   (so base * 0.5^[how many 20s more then 40 this is]) [60 = 0.25^((60-40)/20) = 0.5^1 = 0.5] [80 = 0.5^((80-40)/20) = 0.5^2 = 0.25] [100 = 0.5 ((100-40)/20) = 0.5^3 = 0.125]
                    -no cap for this. its logarithmic.
                 however, for every 50% drop in dp from 40 the skill speed increases by 50%, additive   (so base + ((40 / dp)*0.25) [20 = 2*0.25 = 1+0.5 = 1.5], [10 = 4*0.25 = 1+1 = 2], [5 = 8*0.25 = 2+1 = 3])
                    -for this, cap it at 2.5
so skills:
    the skill design should make the ship stronger, but it should not be some unkillable super death ship. Omega has that already, so it would be redundant. (also borring as hell)
    so, things like ability charges, on kill effects, on take critical damage effects, on overload effects.
    base:
        10 seconds after combat starts. Select one ship that was deployed. The deployed ship becomes 'chosen'.
        If a ship that is chosen dies or retreats, 10 seconds later a different ship is chosen. The newly chosen ship only recharges 25% of the skill cooldowns for any skills transferred.
        If a ship with 'chosen' or 'ascendant' hullmod is deployed and the 'chosen' ship does not have ether of said hullmods, 'chosen' status is transfer to the deployed ship after 10 seconds.
        if a ship with 'chosen' hullmod is deployed and the 'chosen' ship does not have the 'chosen' hullmod, 'chosen' status is transferd to the deployed ship after 10 seconds.
        The largest, most powerful ship deployed is most likely to be 'chosen'.
        ships with the 'refused' hullmod cannot be chosen.

        The chosen ship gains 20% hull, flux desperation, and flux capacity, and movement speed.

        gain the 'chosen', 'ascendant' and 'refused' hullmods.


    1: Passage To Ascendance
        gain the phase teleport ability. (or maybe even longer range, but less charges?)
        3 charges.
    2: Divine Will
        When overloaded, end the overload, instantly dissipate 30% hard flux, and rapidly repair weapons and engines.
        has a 45-second cooldown.
    3: Judgement:
        gain the 'judgement' ability.
        high damage ability, high cooldown?
    4: Redemption:
        When killing a ship regain hull, armor, cr, hard flux dissipation, and PPT based on the dp of the destroyed ship.
        relatively low effects.
        for every effect not needed, gain time dilation instead.
    5: Open The Gates:
        do something like the RATS abyssal boss ships ability, were it creates a space and summons a few frigates? again, let me chose the ships.
        or... I would make this some type of missile bombardment. like that one other RAT exo-tech ship?
    6.a: Champions:
        All allied ships and fighters within ? of this ship gain range, damage, speed, mobility, and flux dissipation.
        effect diminishes with range.
    6.b: 'false ideal':
        every ? seconds, summon a 'false idle near this ship.
        The false idle lasts 5 seconds, and is a copy of the ship. It does not benefit from the 'chosen' status, but can attack and take damage. Is extremely reckless.
    6.c: Presence:
        all hostile ships within X range suffer malfunctions, and a increase in PPT loss by 25%.
        all friendly ships within X range repair weapons and engines 50% faster, and a decrease in PPT loss by 25%
    7.a: Love:
        [NOTE: I really like this ability. maybe to fix issues, make it work on fighters? (so in such a case it negates a single powerfull attack, but thats all?)]
        [I want this to be idal for a super ship start, but I might need to live without that, because this skill is so cool.]
        If this ship were to die, instantly kill a friendly ship within X range.
        restore hull, armor, and dissipate flux based on the hull, armor, and flux of the destroyed ship.
        Attempts to target the highest hull and armor ship nearby, but will also attempt to target the ship with the least 'overkill' hp / armor.
        If the hull and armor of one ship is not enough to gain enough hull to resist a deadly attack, this skill will simply target more ships, up to 100% of the hull of the chosen ship
        Can target fighters.
        150 second recharge. recharge time is multiplied by the percent of hull and armor restored.
        cannot target a chosen ship
    7.b: Dance Of Time:
        gain the '' ability.
        when activated, instantly return the state and position it was in 10 seconds ago?
        If this ship takes fatal damage, negate it. The ship then returns to the position and status (hull, armor, ppt, flux, so forth) it had 10 seconds ago.
        has a 60 second cooldown
    8: Internal Strength:
        gain the Internal Strength ability, increasing time dilation by 2x for 15 seconds.
        has a 45-second cooldown.
    at least 4 skills.
    9: Twin Gods:
        allow 2 ships to be effected by this skill, but reduce all skills effects and recharge speed to 75%.
    10: A shattered throne
        when killed spawn ships and fighters worth up to 25% the DP of the destroyed ship from the debris.
        Possibility: make it so this can scale up to 50% dp, if below 120 total dp? (This feels weak for a capstone, but 25% more ships in battle is crazy. especially when base omega has about 100% dp for this skill.)
        every 5 op of fighters counts as 1 dp.
        fighters will retreat if no other ships are present, unless they normally operate independently of carriers.
        you can choose the ships (arg interfaces, but at least its mostly done?)
        has a 45-second cooldown.
    */
}
