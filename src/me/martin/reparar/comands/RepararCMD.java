package me.sailez.reparar.comands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.sailez.reparar.Main;
import me.sailez.reparar.api.Metodos;

public class RepararCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lb, String[] args) {
		Player p = (Player)s;
		if(lb.equalsIgnoreCase("consertar")) {
			if(!p.hasPermission("consertar.use")) {
				p.sendMessage("§4§lERRO §cVoce nao possui permissao suficiente.");
				return true;
			}
			if(args.length == 0) {
		            if (p.getItemInHand() == null || p.getItemInHand().getType().equals((Object)Material.AIR)) {
		                Metodos.mandarTitulo(p, "§4§lERRO!", "§cSegure um item valido!");
		                p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0f, 1.0f);
		            }
		            else if (p.getItemInHand().getDurability() == 0) {
		                Metodos.mandarTitulo(p, "§4§lERRO!", "§cO item e invalido!");
		                p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0f, 1.0f);
		            }
		            else {
		                final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, "§6Loja de reparacoes");
		                final ItemStack i1 = new ItemStack(Material.WOOL, 1, (short)5);
		                final ItemMeta i1meta = i1.getItemMeta();
		                i1meta.setDisplayName("§a§lCONFIRMAR");
		                final ArrayList<String> lore = new ArrayList<String>();
		                lore.add("§7Clique para confirmar o conserto do item");
		                lore.add("§7Preco: §aR$§2" + Main.pl.config.getConfig().getDouble("Preco"));
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
				        return true;
		        }
			}
		}
		return false;
	}

}
