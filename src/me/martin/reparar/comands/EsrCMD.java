package me.sailez.reparar.comands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.sailez.reparar.Main;
import me.sailez.reparar.api.APIs;
import me.sailez.reparar.api.Hologramas;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class EsrCMD implements CommandExecutor{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lb, String[] args) {
		Player p = (Player)s;
		if(lb.equalsIgnoreCase("esimplerepair")) {
			if(!p.hasPermission("esr.use")) {
				p.sendMessage("§4§lERRO §cVoce nao possui permissao suficiente.");
				return true;
			}
		}
		if(args.length == 0) {
			p.sendMessage("§4§lERRO §cUtilize: /esr (setnpc, removenpc, reload)");
			return true;
		}
		if(args[0].equalsIgnoreCase("setnpc")) {
			
            final Villager villager = (Villager)p.getWorld().spawn(p.getLocation(), (Class)Villager.class);
            villager.setAdult();
            villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 5));
            villager.setCustomNameVisible(false);
            villager.setMetadata("RepairNPC", new FixedMetadataValue(Main.pl, new Object()));
            
            Hologramas holo = new Hologramas(p);
            
    		Hologram h = HologramsAPI.createHologram(Main.pl, villager.getLocation().clone().add(0, 2.85, 0));
    			
    		h.appendTextLine("§e§lREPARAR");
    		h.appendTextLine("§7Conserte seu item, e deixe ele novinho!");
    		h.appendTextLine("§7Preco: §aR$§2" + APIs.preco);
    		h.appendTextLine("§7");
    			
    		holo.setHolo(h);
    		
			EntityLiving handle = ((CraftLivingEntity) villager).getHandle();
		      handle.getDataWatcher().watch(15, (byte) 1);
		      handle.b(true);
		}
		if(args[0].equalsIgnoreCase("removenpc")) {
			ItemStack it = new ItemStack(Material.STICK);
			ItemMeta mt = it.getItemMeta();
			mt.setDisplayName("§eRemovedor de NPCs");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("");
			lore.add("§7Use este item especial para");
			lore.add("§7Remover NPCs setados.");
			lore.add("");
			mt.setLore(lore);
			it.setItemMeta(mt);
			p.getInventory().addItem(it);
		}
		if(args[0].equalsIgnoreCase("reload")) {
			Main.pl.config.reloadConfig();
			Main.pl.config.saveConfig();
			p.sendMessage("§2§lSUCESSO §aPlugin recarregado com sucesso.");
		}
		return false;
	}

}
