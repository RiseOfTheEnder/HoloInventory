/*
 * Copyright (c) 2014. Dries K. Aka Dries007
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.dries007.holoInventory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.dries007.holoInventory.packet.PacketPipeline;
import net.dries007.holoInventory.server.CommandHoloInventory;
import net.dries007.holoInventory.util.CommonProxy;

import static net.dries007.holoInventory.util.Data.MODID;

@Mod(modid = MODID, name = MODID)
public class HoloInventory
{
    @Mod.Instance(value = MODID)
    private static HoloInventory instance;

    private Config config;

    @Mod.Metadata
    private ModMetadata metadata;

    @SidedProxy(serverSide = "net.dries007.holoInventory.util.CommonProxy", clientSide = "net.dries007.holoInventory.util.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler()
    public void fmlEvent(FMLPreInitializationEvent event)
    {
        config = new Config(event.getSuggestedConfigurationFile());
        proxy.preInit();
    }

    @Mod.EventHandler()
    public void fmlEvent(FMLInitializationEvent event)
    {
        PacketPipeline.PIPELINE.initialise();
        proxy.init();
    }

    @Mod.EventHandler()
    public void fmlEvent(FMLPostInitializationEvent event)
    {
        PacketPipeline.PIPELINE.postInitialise();
    }

    @Mod.EventHandler()
    public void fmlEvent(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandHoloInventory());

        proxy.serverStarting();
    }

    public static String getVersion()
    {
        return getInstance().metadata.version;
    }

    public static Config getConfig()
    {
        return instance.config;
    }

    public static HoloInventory getInstance()
    {
        return instance;
    }
}
