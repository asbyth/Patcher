package dev.asbyth.patcher.asm.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsyncScreenshots implements Runnable {

    private final int width, height;
    private final int[] pixelValues;
    private final Framebuffer framebuffer;
    private final File screenshotDirectory;

    public AsyncScreenshots(int width, int height, int[] pixelValues, Framebuffer framebuffer, File screenshotDirectory) {
        this.width = width;
        this.height = height;
        this.pixelValues = pixelValues;
        this.framebuffer = framebuffer;
        this.screenshotDirectory = screenshotDirectory;
    }

    @Override
    public void run() {
        processPixelValues(pixelValues, width, height);

        BufferedImage image;
        File screenshot = getTimestampedPNGFileForDirectory(screenshotDirectory);

        try {
            if (OpenGlHelper.isFramebufferEnabled()) {
                image = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, 1);

                int tHeight;

                for (int heightSize = tHeight = framebuffer.framebufferTextureHeight - framebuffer.framebufferHeight; tHeight < framebuffer.framebufferTextureHeight; ++tHeight) {
                    for (int widthSize = 0; widthSize < framebuffer.framebufferWidth; ++widthSize) {
                        image.setRGB(widthSize, tHeight - heightSize, pixelValues[tHeight * framebuffer.framebufferTextureWidth + widthSize]);
                    }
                }
            } else {
                image = new BufferedImage(width, height, 1);
                image.setRGB(0, 0, width, height, pixelValues, 0, width);
            }

            ImageIO.write(image, "png", screenshot);
            IChatComponent chatComponent = new ChatComponentText(
                    EnumChatFormatting.GRAY + "[" + EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "Patcher" + EnumChatFormatting.GRAY + "] " +
                            "Screenshot saved to " + screenshot.getName());
            chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, screenshot.getCanonicalPath()));
            Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
        } catch (Exception e) {
            e.printStackTrace();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("uh oh!"));
        }
    }

    private static void processPixelValues(int[] pixels, int displayWidth, int displayHeight) {
        int[] xValues = new int[displayWidth];

        for (int yValues = displayHeight / 2, val = 0; val < yValues; ++val) {
            System.arraycopy(pixels, val * displayWidth, xValues, 0, displayWidth);
            System.arraycopy(pixels, (displayHeight - 1 - val) * displayWidth, pixels, val * displayWidth, displayWidth);
            System.arraycopy(xValues, 0, pixels, (displayHeight - 1 - val) * displayWidth, displayWidth);
        }
    }

    private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        String dateFormatting = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        int screenshotCount = 1;
        File screenshot;

        while (true) {
            screenshot = new File(gameDirectory, dateFormatting + ((screenshotCount == 1) ? "" : ("_" + screenshotCount)) + ".png");
            if (!screenshot.exists()) {
                break;
            }

            ++screenshotCount;
        }

        return screenshot;
    }
}
