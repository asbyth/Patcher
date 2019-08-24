package dev.asbyth.patcher;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Patcher.MODID, name = Patcher.NAME, version = Patcher.VERSION)
public class Patcher {

    public static final String MODID = "patcher";
    public static final String NAME = "Patcher";
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger(Patcher.class.getSimpleName());
}
