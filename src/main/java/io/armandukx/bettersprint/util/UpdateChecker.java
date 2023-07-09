package io.armandukx.bettersprint.util;

import com.google.gson.JsonObject;
import io.armandukx.bettersprint.BetterSprint;
import io.armandukx.bettersprint.handler.APIHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

public class UpdateChecker {

    static boolean updateChecked = false;

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if (!updateChecked) {
            updateChecked = true;

            new Thread(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                System.out.println("Checking for updates...");
                JsonObject latestRelease = APIHandler.getResponse("https://api.modrinth.com/updates/HIk2BLBJ/forge_updates.json", false);

                System.out.println("Has promos?");
                if (latestRelease != null && latestRelease.has("promos")) {
                    JsonObject promos = latestRelease.getAsJsonObject("promos");
                    if (promos.has("1.8.9-recommended")) {
                        String recommendedVersion = promos.get("1.8.9-recommended").getAsString().substring(1);

                        DefaultArtifactVersion currentVersion = new DefaultArtifactVersion(BetterSprint.VERSION);
                        DefaultArtifactVersion latestVersion = new DefaultArtifactVersion(recommendedVersion);

                        if (currentVersion.compareTo(latestVersion) < 0) {
                            String releaseURL = "https://modrinth.com/mod/HIk2BLBJ/versions?g=1.8.9";

                            ChatComponentText update = new ChatComponentText(EnumChatFormatting.GREEN + "" + EnumChatFormatting.BOLD + "  [UPDATE]  ");
                            update.setChatStyle(update.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL)));
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            mc.thePlayer.addChatMessage(new ChatComponentText( EnumChatFormatting.BOLD + BetterSprint.prefix + EnumChatFormatting.DARK_RED + BetterSprint.NAME+ " " + BetterSprint.VERSION + " is outdated. Please update to " + latestVersion + ".\n").appendSibling(update));
                        }
                    }
                }
            }).start();
        }
    }
}