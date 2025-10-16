package Abyssal_XO.data.scripts.threat;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import second_in_command.SCData;
import second_in_command.specs.SCAptitudeSection;
import second_in_command.specs.SCBaseAptitudePlugin;

public class NanoThief_Attribute extends SCBaseAptitudePlugin {
    /*so relevent data:
    to do list:
        2) add in the new icons for the systems (copyed another skill for now)
        3) make it so the fighters acsualy lanch from the ship. (aka an animation for that)
        4) add in the quest to allow someone to quire this skill (kill a centen number of fabricates, then just have it.)
        5) improve the display for the nano-thief stats. something like, a bar that fills as I build fighters, and a bar that fills as I max deployment (and fills more (with a different color) as I max storge)
        6) add graphics to the 'overcharge' sub system
    -balance:
        the fighters are way to fucking strong. this is not a issue that can be fixed with deploment time
        solution: reduce fighter damage by 10%, and hull by 20%, and shields by 20%.

    -bugs:
        1) there is a bug that makes it so if I put in a talen, then cancel, the thing does not give me back my wing / reset the fighter to the old one. this needs looking into.

note: I need to change everything.
issue: the 'fighter spam' with this attribute can be completely broken. I will take a suggestion and turn the fighter thing into a capstone.
issue: centralized production is completely fucking overpowered. the issue comes from the faster and faster production that accores.

new skill pholosaphy:
all new skills will incore effects that will slowly use reclaim. (as to avoid the infinit fighter issue I have right now). the skills will be desinged around improving ship stats, or restoring ships.
so the skills:
1) recover hull:
    when out of combat for 5 seconds, recover 1% hull a second.
    for every 100 hull restored, costs 1 reclaim

2) recover CR / PPC?:
    for 10 reclaim a second, negate the loss of peak performance time / combat readiness
    for ships with a peck performance time of 200/150/100/50/25/10 and lower, the cost is multiplied by 2/3/4/5/6/7
    if the ship has infinite PPT, restore CR instead, at a rate of 1 CR every 10 seconds, at the cost of 100 reclaim

3) recover missles:
    every 15/30/45 seconds, fully refill a single empty missile weapons ammo
    costs 5/10/20 * op cost reclaim

4) flux recovery?
    when over 20% flux, gain 20% flux dispensation, and 5% hard flux dispensation
    costs ?? reclaim a second well active
4)


5) forcefully recovery:?
    increase salvage by 100%
    reduce the chances of recovering ships by 50%
5) fighter spawning?
    when a reclaim package is created, create 1/2/3/4 Simulacrum Fighter Wings instantly
5) create escords:
    create up to 1/2/3/4 Simulacrum Fighter Wings that act as though under the 'Defensive Targeting Array' hullmod
    -notes on what a Simulacrum Fighter Wing is here.

(requires at least one from lower tiers of skill)
6) Expensiveness Production:
    for every 2000 reclaim held by a ship, increase the power that all ability's by 5%.
    this process has demonising returns

AND / OR

7) Centralized Logistics: when the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the 'Central Fabricator'. Reclaim Packages will always attempt to move to the Central Fabricator, provided it exists.
   every 20 seconds, produce 1200 'Refined Reclaim' from 1000 'Reclaim'
   every second, provided the Central Fabricator holds at least 2000 Refined Reclaim, a Refined Reclaim package is created that holds 50% of the Refined Reclaim the Central Fabricator holds and moves to the fleet member with the smallest amount of Refined Reclaim held
   (if the craft with the least reclaim is the centralized fabricator nothing happens.)
   Refined Reclaim moves 2X as fast as normal reclaim
   for every 1000 Reclaim the Central Fabricator has:
    produce Refined Reclaim 10% faster.

   After the Central Fabricator is assigned, it cannot be changed for the inter combat, even if it is destroyed or retreats.
   gain the 'Central Fabricator' hullmod, allowing you to chose your Central Fabricator

(requires at least 4 lower level skills)
8) Simulacrum Fighter Wings
    for every 1000 reclaim in a ship, gain 1 control.
    for every control, gain the ability to control one more Simulacrum Fighter Wings.
    Each Simulacrum Fighter Wing costs OP cost * ?? reclaim to produce, and can takes refit time * wing size * ?? seconds to produce.
    Simulacrum Fighters dont benefit from fighter modifiers, and rapidly decay, only being able to stay in combat for 60 seconds before being destroyed.
    Simulacrum Fighters have infinite engagement range.
    Use the __ ability to select your Simulacrum Fighter Wings from available fighter LCPs
OR

9) create starships? (this is way way way to mush... maybe have this be the capstone?)
    every DP * 30 seconds, create a starship (reclaim cost == 500 * DP)



skills:
0) base:
    base:
    When any ship is destroyed, harvest a Reclaim Package worth 1000/2000/3000/4000 reclaim, depending on hullsize. reclaim packages will then go to the nearest ship in the fleet. any packages that reaches there target will be converted into reclaim.
    for every 1000 reclaim in a ship, gain 1 control.
    for every control, gain the ability to control one more Simulacrum Fighter Wings.
    Each Simulacrum Fighter Wing costs OP cost * ?? reclaim to produce, and can takes refit time * wing size * ?? seconds to produce.
    Simulacrum Fighters dont benefit from fighter modifiers, and rapidly decay, only being able to stay in combat for 60 seconds before being destroyed.
    Simulacrum Fighters have infinite engagement range.
    Simulacrum Fighters have -20% hull, -20% shield efficiency, and -10% damage

    Reclaim packages cannot be collected by a ship is in phase.
    Simulacrum Fighters cannot be deployed by a ship is in phase.

1) Overcharged:
    Gain the 'Overcharged' sub system, wish increases precived time flow by 200% for 30 seconds with a very long cooldown.
    lose 50% time to live, 50% hp, and shields take 100% more damage.
    or
    Increased damage by 20%. increase speed and acceleration by 100%. Increase weapon fire and ammunition recharge rate by 500%
    lose 80% hp, 80% time to live, shields take 500% more damage
2) Mass Manufacturing:
    cost 20% less
    take 30% less time to build
    use 10% less control
    5% less HP, armor, and shields.
    5% less damage
3) Longevity:
    gain 10% maximum hull
    shields take 10% less damage
    gain 100% Time To Live
4) Advanced Installation:
    gain 50su speed.
    when outside of combat gain:
        +40% max speed.
5) Quality Checks:
    cost 30% more
    take 10% more time to build
    gains 10% Time To Live
    gains 10% max hp
    gain 10% flux dissipation
    shields take 10% less damage
    gains 10% higher fire rate for all weapons
    gain 10% recharge rate for all weapons
    gain 5% more damage
6) Centralized Logistics: when the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the 'Central Fabricator'. Reclaim Packages will always attempt to move to the Central Fabricator, provided it exists.
   for every 1000 Reclaim the Central Fabricator has:
    produce Simulacrum Fighter Wings 10% faster.
   Simulacrum Fighter Wings produced by the Central Fabricator:
    cost 33% less.
    cost 50% less control
    gain 20% time to live
    gain 5% max hp
    shields take 5% less damage.
   Simulacrum Fighter Wings produced by any ship that is NOT the Central Fabricator:
    cost 50% more
    take 25% more time to build
    lose 20% max hp
    shields take 20% more damage.
    lose 20% time to live
    lose 5% damage
   After the Central Fabricator is assigned, it cannot be changed for the inter combat, even if it is destroyed or retreats.
   gain the 'Central Fabricator' hullmod, allowing you to chose your Central Fabricator
-7) Prepared Deployment
    when a ship has deployed all Simulacrum Fighter Wings it can control, the ship will continue to build Simulacrum Fighter Wings and hold them in storge.
    The ship can store up to the number of Simulacrum Fighter Wings this ship can control * 0.2 +1.
    the ship can deploy Simulacrum Fighter Wings extremely quickly when control becomes available.

    what do I need to do to get this working?
    1) make a new system in ship stats that can add additional ships to 'prepared to be deployed'. by defalt, the value of this will be one.
    2) whenever the ship is at full capacity for swarms, check and make sure it is not at full capacity for 'stored swrams'.

    3) make it so ship stats and skill base has a way to modify the number of available 'stored ships'



-OLD) Material Analyses:
    cost 25% more
    take 100% more time to build

    40% chance of:
    25% chance of:
    15% chance of:
    15% chance of:
    5% chance of:

8) Desperate Measures:
    for every 1000 reclaim gained
        restore 1% CR
        gain 10 seconds of PP time
        gain and 1% of hull, or 500 hull, whatever is higher regenerated over 10 seconds.
        gain 1% fighter replacement rate
or
9) Efficient Production:
    production time reduced by 30%.
    cost reduced by 30%.
    control cost reduced by 50%.



systems:
swarms: fighter craft spawned with this skill. cost 100 reclame at base
reclame package: (like threat) fighters that return to a target firendly vessel. if they reach it, the ship gains X reclaim
	by defalt, a ship is worth 100/200/400/800 reclame.
reclame: the amount of 'sawrm build power' a ship has.
quality: how powerfull a given swarm is.
quality should effect the following:
	(possable qualitys): 0,1,2,3,4,5,6 (1 by defalt)




HP	flux	defesnes	wepons		hullmods
0:	0.7X	0.8X	none		tatical lazer	na
1:	1X	1X	shield?
2:	1.25X		shield?
3:	1.5X		shield?
4:	2X		shield?
5:	3X		shield?
6:	5X		shield + dampiner

changed quality:
for each ponit of quality, gain the following:
+10% hull
+10% attackspeed
-10% flux cost
+10% max speed
+10% max acseration.

skills:
base: spawns a Reclaim Package worth 1000/2000/4000/8000 reclaim from destroyed ships. reclaim packages will then go to the nearest ship in the fleet. any packages that reaches there target will be converted into reclaim.
gain the ability to control reclaim / 1000 Attack Swarms.
if less Attack Swarms are deployed then you can control, create a single Attack Swarm.
attack swarms cost 100 reclaim to create. each swarm spawned has a base quality of 1.
for every point of quality a swarm has gain the following stats:
10% hull
10% max speed and manoeuvrability
10% attack speed on all weapons with no increase in flux cost.
10% increased weapon charge gain speed


1) Overcharged: loss 1 quality, and gain 30% increased damage.
2) Mass Manufacturing: lose 1 quality, and cost 33% less.
3) Longevity: loss 1 quality, and gain 50% maximum hull
4) Condensing: gain 1 quality, and lose 10% maximum hull.
5) Quality Checks: gain 1 quality, and cost 10% more.

6) Centralized Logistics: when the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the 'Central Fabricator'. Reclaim Packages will always attempt to move to the Central Fabricator, provided it exists.
   The Central Fabricator produces swarms for 33% less cost.
   The Central Fabricator produces swarms at +3 quality.
   The Central Fabricator can control 50% more swarms.
   IF the Central Fabricator no longer exists, Reclaim Package will prefer go to the nearest capital ship, provided one exists.
   Capital ships that are not the Central Fabricator produce swarms with +1 quality
   gain the 'Central Fabricator' hullmod, allowing you to chose your Central Fabricator
or
7) Material Analyses: swarms cost 25% more. swarms gain 1(15%),2(20%),3(30%),4(35%) points of quality.
If this ship is deployed when the 'Central Fabricator' is selected, forces this ship to be the Central Fabricator.
8) Desperate Measures: for every 100 reclaim gained, restore 0.5 CR, 10 seconds of PP time, and 1% of hull.
or
9) Efficient Production: swarm cost is reduced by 50%. Ships can control 100% more swarms.
    * */
    @Override
    public String getOriginSkillId() {
        return "SiC_NanoThief_NanoThiefBase";
    }

    @Override
    public void createSections() {
        SCAptitudeSection section1 = new SCAptitudeSection(true, 0, "technology1");
        section1.addSkill("SiC_NanoThief_skill_1");
        section1.addSkill("SiC_NanoThief_skill_2");
        section1.addSkill("SiC_NanoThief_skill_3");
        section1.addSkill("SiC_NanoThief_skill_4");
        section1.addSkill("SiC_NanoThief_skill_5");
        section1.addSkill("SiC_NanoThief_skill_6");
        section1.addSkill("SiC_NanoThief_skill_7");

        //note: this is a temp solution to a very completed issue. I need to be carefull carefull to remove this after~~~
        //section1.addSkill("SiC_NanoThief_skill_8");
        //section1.addSkill("SiC_NanoThief_skill_9");

        addSection(section1);
        /*SCAptitudeSection section2 = new SCAptitudeSection(false, 3, "technology2");
        addSection(section2);*/

        SCAptitudeSection section3 = new SCAptitudeSection(false, 4, "technology4");
        section3.addSkill("SiC_NanoThief_skill_8");
        section3.addSkill("SiC_NanoThief_skill_9");
        addSection(section3);
    }

    @Override
    public Float getNPCFleetSpawnWeight(SCData scData, CampaignFleetAPI campaignFleetAPI) {
        /*String faction = campaignFleetAPI.getFaction().getId();
        if (Settings.NanoThief_Users.contains(faction)) return 100f;*/
        return 0f;
    }

    @Override
    public Boolean guaranteePick(CampaignFleetAPI fleet) {
        String faction = fleet.getFaction().getId();
        for (String a : Settings.NanoThief_Users){
            if (a.equals(faction)) return true;
        }
        //if (Settings.NanoThief_Users.contains(faction)) return true;
        return super.guaranteePick(fleet);
    }
}
