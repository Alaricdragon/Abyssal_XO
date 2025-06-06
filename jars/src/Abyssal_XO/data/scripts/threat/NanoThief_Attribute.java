package Abyssal_XO.data.scripts.threat;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import second_in_command.SCData;
import second_in_command.specs.SCAptitudeSection;
import second_in_command.specs.SCBaseAptitudePlugin;

public class NanoThief_Attribute extends SCBaseAptitudePlugin {
    /*so relevent data:
    * ThreatHullmod.advanceInCombat is responsable to adding a script. to only adds the script to dead ships. when a ship is dead, and is not yet taged as reclame, it runs adds the following script:
    * ThreatShipReclamationScript with a 3 delay on the killed ship. this then proceeds to wait a time, then spawn particals and swarms for reclame.
    *   this script does not deside on how the newly created swarm will act.
    *   'ThreatShipReclamationScript.launchSwarm() {'swarm.params.flockingClass', 'swarm.params.memberExchangeClass'} does determin how the swarm is able to do things like share fragments though.
    * the sawrm isself i created in 'FragmentSwarmHullmod.createSwarmFor'. it should just work?????
     *       -dont look to deep into this. a lot of things look like they require perset data to work, but most things are just checking if the ship is real. the following are important though:
     *           SwarmLauncherEffect.SWARM_RADIUS() for IDK. maybe how far out the swarms things go?
     *           SwarmLauncherEffect.FRAGMENT_NUM() for IDK. maybe how many fragments the swarm has in it?
     *
     *  ...
     * ok, so this is -working- here i what I still need to do:
     * 1) add in the new 'what reclaim targets' script.
     *  -need the hullmod 'Central Fabracater' for this.
     * 2) add in the new stat changes based on skills.
     * 3) add in the interface, so the player can see what on earth is even going on.
     *  -note: might need to change reclaim storge location to make this easyer.
     * 4) make it so Wave Deployment works (with interface)
     * 5) make it so Desprate Messures works
     * 6) make it so 'defenders' can spawn.
     *  -need to learn how to make my own ship
     *  -need to edit the swarm AI for this ship, making it hang out around its ship when in defense mode, and go act like an attack swarm when attacking.
     * 7) need to make a way to get this SiC officer
     *  -add in a battle script, count the number of fabricates killed. after 5 give this upgrade.
     * 8) version checker support, because its easier
     *
     * 1) find a way to add a custom fighter that will act as defenders. defenders will go defend, untill they no longer need to.
     * notes ok skills:
     * second in command atrubtue
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
base: spawns a reclaim package worth 100/200/400/800 reclaim from destroyed ships. reclaim packages will then go to the nearest ship in the fleet. any packages that make it to there target ship will be converted into reclaim.
for every 100 reclaim gained, spawn a attack swarm. each spawned swarm has a base quality of 1.
for every point of quality a swarm has gain the following stats:
10% hull
10% max speed and manoeuvrability
10% attack speed on all weapons with no increase in flux cost.
10% increased charge gain speed
//-10% flux cost for all wepons


1) Wave Deployment: swarms will no longer be deployed immanently, but instead be deployed every 45 seconds. this timer is shared between all ships. swarms cost 10% less.
2) Mass Manufacturing: swarms lose 1 quality, but cost 33% less.
3) Longevity: swarms loss 1 quality, and gain 50% more HP
4) Condensing: swarms gain 1 quality, and have 25% less hp.
5) Quality Checks: swarms gain 1 quality, and cost 25% more.

6) Centralized Logistics: when the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the 'Central Fabricator'. Reclaim Packages will always attempt to move to the Central Fabricator, provided it exists.
   IF the Central Fabricator no longer exists, the swarms will attempt to move to the closest largest ship in your fleet.
   The Central Fabricator produces swarms for 33% less cost.
   swarm quality is increases by 0/0/1/2 depending on the hullsize of the ship that created it.
   gain the 'Central Fabricator' hullmod, allowing you to chose your Central Fabricator
or
7) Material Analyses: swarms cost 25% more. swarms gain 1(15%),2(20%),3(30%),4(35%) points of quality.

8) Desperate Measures: for every 100 reclame gained, restore 0.5 CR, 10 seconds of PP time, and 1% of hull.
or
9) Efficient Production: for every swarm created, a defense swarm of the same quality is created. defense swarms will go to the nearest living friendly ship and protect it. they will target fighters and missles first, and are armed with lazzars?
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
        addSection(section1);

        SCAptitudeSection section2 = new SCAptitudeSection(false, 3, "technology2");
        section2.addSkill("SiC_NanoThief_skill_6");
        section2.addSkill("SiC_NanoThief_skill_7");
        addSection(section2);

        SCAptitudeSection section3 = new SCAptitudeSection(false, 4, "technology4");
        section3.addSkill("SiC_NanoThief_skill_8");
        section3.addSkill("SiC_NanoThief_skill_9");
        addSection(section3);
    }

    @Override
    public Float getNPCFleetSpawnWeight(SCData scData, CampaignFleetAPI campaignFleetAPI) {
        String faction = campaignFleetAPI.getFaction().getId();
        if (Settings.NanoThief_Users.contains(faction)) return 100f;
        return 0f;
    }
}
