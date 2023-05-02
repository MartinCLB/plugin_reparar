package me.sailez.reparar.api;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.sailez.reparar.Main;

public class APIs {
	
	public static double preco = Main.pl.config.getConfig().getDouble("Preco");
	
	@SuppressWarnings("deprecation")
	public static void reparar(Player p) {
		
		Main.pl.economy.withdrawPlayer(p.getName(), preco);
		p.getItemInHand().setDurability((short)0);
		Metodos.mandarTitulo(p, "§2§lSUCESSO!", "§aO item desejado foi reparado!");
		
		p.closeInventory();
		
		p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 10);
		p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
	}
}
