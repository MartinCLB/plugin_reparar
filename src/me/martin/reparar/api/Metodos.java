package me.sailez.reparar.api;

import org.bukkit.entity.Player;

public class Metodos {
	
	public static void mandarTitulo(Player p, String titulo, String subtitulo) {
		
		Titulos title = new Titulos(titulo, subtitulo, 0, 1, 1);
		title.send(p);
	}
}
