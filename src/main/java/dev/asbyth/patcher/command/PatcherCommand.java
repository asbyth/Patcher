package dev.asbyth.patcher.command;

import dev.asbyth.patcher.gui.PatcherGui;
import dev.asbyth.patcher.util.TickScheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class PatcherCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "patcher";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/patcher";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        TickScheduler.INSTANCE.schedule(0, () -> Minecraft.getMinecraft().displayGuiScreen(new PatcherGui()));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
