package me.sailez.reparar;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.sailez.reparar.api.eConfiguracao;
import me.sailez.reparar.comands.EsrCMD;
import me.sailez.reparar.comands.RepararCMD;
import me.sailez.reparar.listeners.Eventos;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class Main extends JavaPlugin {
	
	public static ConsoleCommandSender cs = Bukkit.getConsoleSender();
	public static Main pl;
	
	public PluginManager pm = getServer().getPluginManager();
	
	public eConfiguracao config = new eConfiguracao(this, "configuracao.yml");
	
	public Economy economy;
	
	@Override
	public void onLoad() {
		
		cs.sendMessage("§6[eReparar] Carregando..");
		
		if (this.pm.getPlugin("Vault") == null) {
		     cs.sendMessage("§6[eReparar] §7Falha ao carregar o plugin: §f'Vault'");
		     pm.disablePlugin(this);
		     return;
		}
		if(config.existeConfig()) {
			cs.sendMessage("§6[eReparar] §7Configuracao: arquivo §f'configuracao.yml' §7carregada.");
		}
	}
	@Override
	public void onEnable() {
		cs.sendMessage("§6[eReparar] Plugin iniciado corretamente.");
		
		Bukkit.getPluginManager().registerEvents(new Eventos(), this);
		getCommand("consertar").setExecutor(new RepararCMD());
		getCommand("esimplerepair").setExecutor(new EsrCMD());
		
		config.saveDefaultConfig();
		
		setupEconomy();
		
		pl = this;
		
		for(World worlds : Bukkit.getWorlds()) {
			for(Entity entidades : worlds.getEntities()) {
				if(entidades instanceof Villager) {
					Villager v = (Villager)entidades;
					if(v.hasMetadata("RepairNPC")) {
						EntityLiving handle = ((CraftLivingEntity) v).getHandle();
					      handle.getDataWatcher().watch(15, (byte) 1);
					      handle.b(true);
					}
				}
			}
		}
	}
	@Override
	public void onDisable() {
		
		cs.sendMessage("§6[Reparar] Plugin desligado corretamente.");
		
		HandlerList.unregisterAll();
	}
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
