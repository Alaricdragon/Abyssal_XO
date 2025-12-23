package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import net.java.games.input.Component;

import java.awt.Button;
import java.util.List;

public class optionsHolder implements CustomUIPanelPlugin {
    /*notes:
        1: getX and getCenterX are diffrent.
            getX is relevant position.
            getCenterX is the true position
        2: when useing a 'PositionAPI' releitive position set functions keep in mind that if you want to unset them, you cant just unset one item if it has any child items. You need to unset them all. one by fucking one.
        3: to add buttons the most striaghtforward way starting out is to call createTooltip on CustomPanelAPI and call addButton on it from there
            -how the hell do I do that????




    */
    /*I am getting nowhere.
    * so: here is what I know
    * 1) this fjnkbasdijlsbv nmfcjwmknbv mas,vbn fc as,fbnvc
    * 2) I -can- render items. I can also get the 'position' of a givin item next to another item.
    * 3) maybe... all I need to do is just... get the relevent position */
    public void note(){
        /*position.belowMid(new ButtonAPI() {},5);
        * is that usefull??A?!?!?!?!?*/

    }
    public optionsHolder(){

    }
    public void init(){

    }
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
}
