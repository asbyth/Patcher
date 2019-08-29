package dev.asbyth.patcher.gui;

import dev.asbyth.patcher.Patcher;
import dev.asbyth.patcher.gui.helpers.AbstractScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import static dev.asbyth.patcher.config.Settings.*;

public class PatcherGui extends AbstractScreen {

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, getCenter() - 155, getRowPos(1), 150, 20,
                getSuffix(ASYNC_SCREENSHOTS, "Async Screenshots")));
        buttonList.add(new GuiButton(1, getCenter() + 5, getRowPos(1), 150, 20,
                getSuffix(CLOSING_CHAT, "Closing Chat")));

        buttonList.add(new GuiButton(2, getCenter() - 155, getRowPos(2), 150, 20,
                getSuffix(COMMAND_HANDLER, "Command Handler")));
        buttonList.add(new GuiButton(3, getCenter() + 5, getRowPos(2), 150, 20,
                getSuffix(DEATH_SCREEN, "Death Screen")));

        buttonList.add(new GuiButton(4, getCenter() - 155, getRowPos(3), 150, 20,
                getSuffix(FULLBRIGHT, "Fullbright")));
        buttonList.add(new GuiButton(5, getCenter() + 5, getRowPos(3), 150, 20,
                getSuffix(FULLSCREEN, "Fullscreen")));

        buttonList.add(new GuiButton(6, getCenter() - 155, getRowPos(4), 150, 20,
                getSuffix(INTERNAL_ERROR, "Internal Error")));
        buttonList.add(new GuiButton(7, getCenter() + 5, getRowPos(4), 150, 20,
                getSuffix(INVENTORY_POSITION, "Inventory Position")));

        buttonList.add(new GuiButton(8, getCenter() - 155, getRowPos(5), 150, 20,
                getSuffix(MOUSE_DELAY, "Mouse Delay")));
        buttonList.add(new GuiButton(9, getCenter() + 5, getRowPos(5), 150, 20,
                getSuffix(PLAYER_ARM, "Player Arm")));

        buttonList.add(new GuiButton(10, getCenter() - 155, getRowPos(6), 150, 20,
                getSuffix(VOID_FLICKERING, "Void Flickering")));
        buttonList.add(new GuiButton(11, getCenter() + 5, getRowPos(6), 150, 20,
                getSuffix(WORLD_SWITCHING, "World Switching")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(mc.fontRendererObj, "Patcher", getCenter(), getRowPos(0), -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                ASYNC_SCREENSHOTS = !ASYNC_SCREENSHOTS;
                button.displayString = getSuffix(ASYNC_SCREENSHOTS, "Async Screenshots");
                break;

            case 1:
                CLOSING_CHAT = !CLOSING_CHAT;
                button.displayString = getSuffix(CLOSING_CHAT, "Closing Chat");
                break;

            case 2:
                COMMAND_HANDLER = !COMMAND_HANDLER;
                button.displayString = getSuffix(COMMAND_HANDLER, "Command Handler");
                break;

            case 3:
                DEATH_SCREEN = !DEATH_SCREEN;
                button.displayString = getSuffix(DEATH_SCREEN, "Death Screen");
                break;

            case 4:
                FULLBRIGHT = !FULLBRIGHT;
                button.displayString = getSuffix(FULLBRIGHT, "Fullbright");
                break;

            case 5:
                FULLSCREEN = !FULLSCREEN;
                button.displayString = getSuffix(FULLSCREEN, "Fullscreen");
                break;

            case 6:
                INTERNAL_ERROR = !INTERNAL_ERROR;
                button.displayString = getSuffix(INTERNAL_ERROR, "Internal Error");
                break;

            case 7:
                INVENTORY_POSITION = !INVENTORY_POSITION;
                button.displayString = getSuffix(INVENTORY_POSITION, "Inventory Position");
                break;

            case 8:
                MOUSE_DELAY = !MOUSE_DELAY;
                button.displayString = getSuffix(MOUSE_DELAY, "Mouse Delay");
                break;

            case 9:
                PLAYER_ARM = !PLAYER_ARM;
                button.displayString = getSuffix(PLAYER_ARM, "Player Arm");
                break;

            case 10:
                VOID_FLICKERING = !VOID_FLICKERING;
                button.displayString = getSuffix(VOID_FLICKERING, "Void Flickering");
                break;

            case 11:
                WORLD_SWITCHING = !WORLD_SWITCHING;
                button.displayString = getSuffix(WORLD_SWITCHING, "World Switching");
                break;

        }
    }

    @Override
    protected String getButtonTooltip(int buttonId) {
        switch (buttonId) {
            case 0:
                return I18n.format("gui.description.asyncscreenshots");

            case 1:
                return I18n.format("gui.description.closingchat");

            case 2:
                return I18n.format("gui.description.commandhandler");

            case 3:
                return I18n.format("gui.description.deathscreen");

            case 4:
                return I18n.format("gui.description.fullbright");

            case 5:
                return I18n.format("gui.description.fullscreen");

            case 6:
                return I18n.format("gui.description.internalerror");

            case 7:
                return I18n.format("gui.description.inventoryposition");

            case 8:
                return I18n.format("gui.description.mousedelay");

            case 9:
                return I18n.format("gui.description.playerarm");

            case 10:
                return I18n.format("gui.description.voidflickering");

            case 11:
                return I18n.format("gui.description.worldswitching");
        }

        return null;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        Patcher.INSTANCE.saveConfig();
    }
}
