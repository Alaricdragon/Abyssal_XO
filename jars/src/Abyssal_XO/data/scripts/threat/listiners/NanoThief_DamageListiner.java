package Abyssal_XO.data.scripts.threat.listiners;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.combat.listeners.DamageListener;

public class NanoThief_DamageListiner implements DamageListener {
    @Override
    public void reportDamageApplied(Object source, CombatEntityAPI target, ApplyDamageResultAPI result) {
        if (!(target instanceof ShipAPI)) return;

        if (((ShipAPI) target).isFighter()) return;
        if (!((ShipAPI) target).isHulk()) return;

    }
}
