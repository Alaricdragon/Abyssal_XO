package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnFireEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.apache.log4j.Logger;

public class NanoThief_OnAttackListener implements OnFireEffectPlugin {
    //THIS IS NOT IN USE
    private static Logger log = Global.getLogger(NanoThief_OnAttackListener.class);

    @Override
    public void onFire(DamagingProjectileAPI projectile, WeaponAPI weapon, CombatEngineAPI engine) {
        log.info("RUNNING THE LISTINER BOYYYY");
    }
}
