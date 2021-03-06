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

package net.dries007.holoInventory.util;

import net.dries007.holoInventory.packet.BlockInventoryPacket;
import net.dries007.holoInventory.packet.PacketPipeline;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;

public class InventoryData
{
    public int        id;
    public IInventory te;
    public HashMap<EntityPlayer, NBTTagCompound> playerSet = new HashMap<EntityPlayer, NBTTagCompound>();
    public String name;
    public String type;

    public InventoryData(IInventory te, int id)
    {
        this.id = id;
        this.te = te;
        this.name = te.getInventoryName();
        this.type = te.getClass().getCanonicalName();
    }

    public void sendIfOld(EntityPlayerMP player)
    {
        NBTTagCompound data = toNBT();
        if (!playerSet.containsKey(player) || !playerSet.get(player).equals(data))
        {
            playerSet.put(player, data);
            PacketPipeline.PIPELINE.sendTo(new BlockInventoryPacket(toNBT()), player);
        }
    }

    private NBTTagCompound toNBT()
    {
        NBTTagCompound root = new NBTTagCompound();
        root.setInteger("id", this.id);
        if (name == null) name = ""; //Really mod authors? Really? Null is not a good name.
        root.setString("name", name);
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < te.getSizeInventory(); i++)
        {
            if (te.getStackInSlot(i) != null)
            {
                list.appendTag(te.getStackInSlot(i).writeToNBT(new NBTTagCompound()));
            }
        }
        root.setTag("list", list);
        return root;
    }

    public void update(IInventory inventory)
    {
        te = inventory;
    }

    public String getType()
    {
        return type;
    }
}
