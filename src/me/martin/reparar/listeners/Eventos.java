package me.sailez.reparar.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.sailez.reparar.Main;
import me.sailez.reparar.api.APIs;
import me.sailez.reparar.api.Metodos;

public class Eventos implements Listener
{
    @SuppressWarnings("deprecation")
	@EventHandler
    public void Interact(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        final Villager villager = (Villager)e.getRightClicked();
        e.setCancelled(true);
        if (villager.hasMetadata("RepairNPC")) {
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals((Object)Material.AIR)) {
                Metodos.mandarTitulo(p, "§4§lERRO!", "§cSegure um item valido!");
                p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 1.0f, 1.0f);
            }
            else if (p.getItemInHand().getDurability() == 0) {
                Metodos.mandarTitulo(p, "§4§lERRO!", "§cO item e invalido!");
                p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 1.0f, 1.0f);
            }
            else if(!Main.pl.economy.has(p.getName(), APIs.preco)) {
            	Metodos.mandarTitulo(p, "§4§lERRO", "§cVocê nao possui coins suficiente.");
            	p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 1F, 1F);
            }
            else {
                final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, "§6Loja de reparacoes");
                final ItemStack i1 = new ItemStack(Material.WOOL, 1, (short)5);
                final ItemMeta i1meta = i1.getItemMeta();
                i1meta.setDisplayName("§a§lCONFIRMAR");
                final ArrayList<String> lore = new ArrayList<String>();
                lore.add("§7Clique para confirmar o conserto do item");
                lore.add("§7Preco: §aR$§2" + Main.pl.config.getConfig().getDouble("Preco"));
                lore.add("§7PrecoVIP: §aR$§2" + Main.pl.config.getConfig().getDouble("PrecoVIP"));
                i1meta.setLore(lore);
                i1.setItemMeta(i1meta);
                inv.setItem(11, i1);
                
                final ItemStack i2 = p.getItemInHand();
                inv.setItem(13, i2);
                final ItemStack i3 = new ItemStack(Material.WOOL, 1, (short)14);
                final ItemMeta i3meta = i3.getItemMeta();
                i3meta.setDisplayName("§c§lCANCELAR");
                final ArrayList<String> lore2 = new ArrayList<String>();
                lore2.add("§7Clique para cancelar o conserto do item.");
                i3meta.setLore(lore2);
                i3.setItemMeta(i3meta);
                inv.setItem(15, i3);
                p.openInventory(inv);
            }
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void Dano(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Villager) {
            final Villager villager = (Villager)e.getEntity();
            if (villager.hasMetadata("RepairNPC")) {
                final Player p = (Player)e.getDamager();
                if (e.getDamager() instanceof Player) {
                    if (p.hasPermission("esr.use") && p.getItemInHand().getType() == (Material.STICK)) {
                        villager.setHealth(0.0);
                        Metodos.mandarTitulo(p, "§6ASSASINATO!", "§eVocê assasinou o NPC :c");
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                		p.playEffect(p.getLocation(), Effect.CRIT, 10);

                      for (Hologram hologram : HologramsAPI.getHolograms(Main.pl)) {
                          Location playerLoc   = p.getLocation();
                          Location hologramLoc = hologram.getLocation();

                          if (playerLoc.distance(hologramLoc) < 10) {
                              hologram.delete();
                          }else {
                        	  e.setCancelled(true);
                          }
                      }
                    }
                    else {
                        e.setCancelled(true);
                    }
                }
                else {
                    e.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void Interact(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getWhoClicked() instanceof Player && e.getInventory().getTitle().equals("§6Loja de reparacoes")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§a§lCONFIRMAR")) {
            	APIs.reparar(p);
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lCANCELAR")) {
                Metodos.mandarTitulo(p, "§4§lCANCELADO!", "§cVocê decidiu cancelar a operação");
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                p.closeInventory();
            }
        }
    }
}