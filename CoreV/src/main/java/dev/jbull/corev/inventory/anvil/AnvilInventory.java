package dev.jbull.corev.inventory.anvil;

import dev.jbull.corev.inventory.abstracts.InventoryAbstract;
import dev.jbull.corev.utils.ItemBuilder;
import lombok.Data;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AnvilInventory extends InventoryAbstract {
    private Inventory inventory;
    private AnvilContainer container;

    public AnvilInventory(String tittle, int size) {
        super(tittle, size);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        super.setItem(slot, itemStack);
    }

    @Override
    public void open(Player player) {
        AnvilContainer anvilContainer = new AnvilContainer(((CraftPlayer) player).getHandle());
        EntityPlayer entityPlayer =  ((CraftPlayer) player).getHandle();
        inventory = anvilContainer.getBukkitView().getTopInventory();
        for (int slot : getSlots().keySet()){
            inventory.setItem(slot, getItem(slot));
        }
        int c =  entityPlayer.nextContainerCounter();
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c,"minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));
        entityPlayer.activeContainer = anvilContainer;
        entityPlayer.activeContainer.windowId = c;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
        System.out.println(inventory.getHolder());
        container = anvilContainer;
        AnvilMap.getInstance().put(player, this);
    }

    @Override
    public void close(Player player) {
        EntityPlayer entityPlayer =  ((CraftPlayer) player).getHandle();
        entityPlayer.activeContainer.getBukkitView().getTopInventory().clear();
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutCloseWindow());
        AnvilMap.getInstance().remove(player);
    }

    public AnvilContainer getContainer() {
        return container;
    }
}
