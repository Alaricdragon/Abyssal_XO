package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

public class CentralFabricator extends BaseHullMod {
    @Override
    public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return (int)((NanoThief_8.sModBonus-1)*100)+"%";
    }
}
