package Abyssal_XO.data.scripts.threat.dialogPlugin;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

import java.util.List;

public class Nano_Thief_CustomUIPanelPlugin implements CustomUIPanelPlugin {
    @Override
    public void positionChanged(PositionAPI position) {

    }

    @Override
    public void renderBelow(float alphaMult) {

    }

    @Override
    public void render(float alphaMult) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void processInput(List<InputEventAPI> events) {

    }

    @Override
    public void buttonPressed(Object buttonId) {

    }






    /*public static void create(CustomPanelAPI moduleTab) {
        float width = 250f;
        float listHeight = 478f;
        float verticalOffset = PanelConstants.HEADER_HEIGHT + 27f;
        TooltipMakerAPI moduleListHeader = createHeader(moduleTab, width, listHeight);
        moduleTab.addUIElement(moduleListHeader).inTL(PanelConstants.PANEL_CONTENT_OFFSET + 2f, -verticalOffset);
        ModuleList.renderModuleList();
    }

    public static void renderModuleList() {
        CustomPanelAPI tabInstance = ModulesTab.getTabInstance();
        if (tabInstance == null) return;
        if (listInstance != null) {
            tabInstance.removeComponent(listInstance);
        }
        CustomPanelAPI moduleList = createModuleList(tabInstance);
        float verticalOffset = PanelConstants.HEADER_HEIGHT + 27f;
        tabInstance.addComponent(moduleList).inTL(PanelConstants.PANEL_CONTENT_OFFSET + 2f, -(verticalOffset - 22f));
        ModuleList.listInstance = moduleList;
    }*/
}
