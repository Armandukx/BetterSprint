package io.armandukx.bettersprint;

import io.armandukx.bettersprint.util.UpdateChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;


@Mod(
        name = BetterSprint.NAME,
        modid = BetterSprint.MODID,
        version = BetterSprint.VERSION,
        useMetadata = true,
        clientSideOnly = true
)
public class BetterSprint {
    public static final String NAME = "Better Sprint";
    public static final String MODID = "bettersprint";
    public static final String VERSION = "1.0.0";
    public static final String prefix =
            EnumChatFormatting.YELLOW + "[B" + EnumChatFormatting.GREEN + "S" + EnumChatFormatting.RED + "M] " + EnumChatFormatting.RESET;
    private static boolean sprint = false;
    private KeyBinding toggle;
    private Minecraft mc;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new UpdateChecker());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        toggle = new KeyBinding("Toggle Sprint", 23, "key.categories.movement");
        ClientRegistry.registerKeyBinding(toggle);

        mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && mc.thePlayer != null && mc.theWorld != null) {
            int key = mc.gameSettings.keyBindSprint.getKeyCode();
            if (toggle.isPressed()) {
                sprint = !sprint;
                if (!sprint && key > 0) {
                    toggle.setKeyBindState(key, Keyboard.isKeyDown(key));
                }
            }
            toggle.setKeyBindState(key, sprint);
        }
    }
}
