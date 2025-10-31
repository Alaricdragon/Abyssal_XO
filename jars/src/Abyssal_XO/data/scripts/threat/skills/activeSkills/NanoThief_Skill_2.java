package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_2;
import com.fs.starfarer.api.combat.WeaponAPI;

import java.util.ArrayList;

public class NanoThief_Skill_2 extends NanoThief_SkillBase {
    ArrayList<ArrayList<WeaponAPI>> wepons = new ArrayList<>();
    public NanoThief_Skill_2(NanoThief_ShipSkills skills) {
        super(skills);
        skills.ship.getVariant().getWeaponGroups().get(0).getSlots();
        wepons.add(new ArrayList<>());
        wepons.add(new ArrayList<>());
        wepons.add(new ArrayList<>());
        for (String a : skills.ship.getVariant().getFittedWeaponSlots()){
            skills.ship.getVariant().getWeaponSpec(a).getAmmoPerSecond();
            skills.ship.getVariant().getWeaponSpec(a).getMaxAmmo();

        }
        for (WeaponAPI a : skills.ship.getAllWeapons()){
            a.getAmmoTracker().setAmmo(0);
            if (a.getMaxAmmo() == 0) continue;
            if (a.getAmmoTracker().getAmmoPerSecond() != 0) continue;
            int b = 0;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL)) b = 0;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) b = 1;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.LARGE)) b = 2;
            wepons.get(b).add(a);
        }
        if (wepons.get(0).isEmpty() && wepons.get(1).isEmpty() && wepons.get(2).isEmpty()){
            skills.suppressListener(this);
        }
    }

    private float cooldown = 0f;
    private static final float secondsPerReceack = 3f;
    @Override
    public void advance(float amount) {
        cooldown-=amount;
        if (cooldown > 0) return;
        for (ArrayList<WeaponAPI> a : wepons){
            for (WeaponAPI b : a){
                if (b.getAmmoTracker().getAmmo() != 0) return;
                double costPerOpp = 0;
                float cooldownT = 0;
                if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL)){
                    costPerOpp = NanoThief_2.costSmall;
                    cooldownT = NanoThief_2.timeSmall;
                }
                if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)){
                    costPerOpp = NanoThief_2.costSmall;
                    cooldownT = NanoThief_2.timeSmall;
                }
                if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.LARGE)){
                    costPerOpp = NanoThief_2.costSmall;
                    cooldownT = NanoThief_2.timeSmall;
                }
                double cost = b.getSpec().getOrdnancePointCost(skills.ship.getCaptain().getStats(),skills.ship.getMutableStats()) * costPerOpp;
                if (skills.getTotalReclaim() >= cost){
                    skills.useReclaim(cost);
                    cooldown+=cooldownT;
                    return;
                }
            }
        }
        cooldown+=secondsPerReceack;
    }
}
