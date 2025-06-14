package Abyssal_XO.data.scripts.threat;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import second_in_command.SCData;
import second_in_command.specs.SCAptitudeSection;
import second_in_command.specs.SCBaseAptitudePlugin;

public class NanoThief_Attribute extends SCBaseAptitudePlugin {
    /*so relevent data:
    to do list:
        1) rebalance the 9 skills to not use the quality system.
            -1.a) rebalance skills
            -1.b) implement skills.
        2) add in the new icons for the systems
        3) make it so the fighters acsualy lanch from the ship.
        4) add in the quest to allow someone to quire this skill (kill a centen number of fabricates, then just have it.)

    -balance:
        the fighters are way to fucking strong. this is not a issue that can be fixed with deploment time
        solution: reduce fighter damage by 10%, and hull by 20%, and shields by 20%.

    -bugs:
        (fixed): something is modifiing the max speed of the fighters by like 75% increase. the reasons is unknown.
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
7) Material Analyses:
    cost 25% more
    take 100% more time to build

    //note: I dont know how to feel about this... giving ships stats seems strange... maybe instead:
    40% chance of:
    25% chance of:
    15% chance of:
    15% chance of:
    5% chance of:

    40% chance of:
        gain 10% time to live
        gain 5% max hp
        shields take 5% less damage
    25% chance of:
        gain 20% time to live
        gain 10% max hp
        shields take 10% less damage
    15% chance of:
        gain 25% time to live
        gain 20% max hp
        shields take 20% less damage
    15% chance of:
        lose 25% time to live
        lose 10% hp
        gain 10% increased damage
        gain 20% increased weapon range
    5% chance of:
        gain 50% max hp
        shields take 50% less damage
        gain 30% rate of fire for all weapons
        gain 30% ammo regeneration
        gain 30% increased flux dissipation
        gain 10% increased damage

    cost 25% more
    take 100% more time to produce

8) Desperate Measures:
    for every 1000 reclaim gained
        restore 0.5 CR
        gain 10 seconds of PP time
        gain and 1% of hull, regenerated over 10 seconds.
        gain 1% fighter replacement time
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
        if (Settings.NanoThief_Users.contains(faction)) return true;
        return super.guaranteePick(fleet);
    }
}
