package com.hawkfalcon.CartWarp;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CartWarp extends JavaPlugin implements Listener {
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		final File f = new File(getDataFolder(), "config.yml");
		if (!f.exists()){
			saveDefaultConfig();
		}
	}

	public void onDisable() {
		this.saveConfig();	}
	boolean enable = true;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("cwon")) {
			if (sender.hasPermission("CartWarp.cwon")) {
				getServer().broadcastMessage(ChatColor.WHITE + "[" + ChatColor.RED + "CartWarp" + ChatColor.WHITE + "] " + ChatColor.BLUE + "CartWarp has been enabled!");
				enable = true;
			} else {
				sender.sendMessage(ChatColor.RED + "You don't have permission!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("cfoff")) {
			if (sender.hasPermission("CartWarp.cwoff")) {
				if(enable == true){
					getServer().broadcastMessage(ChatColor.WHITE + "[" + ChatColor.RED + "CartWarp" + ChatColor.WHITE + "] " + ChatColor.BLUE + "CartWarp has been disabled!");
					enable = false;
				} else {
					sender.sendMessage(ChatColor.RED + "You don't have permission!");
				}

			}else{
				sender.sendMessage(ChatColor.RED + "Plugin is disabled! Enable with /cwon");
			}
		}

		if (cmd.getName().equalsIgnoreCase("cartwarpsetspawn")||cmd.getName().equalsIgnoreCase("cwsetspawn")) {

			if (sender.hasPermission("CartWarp.setspawn")) {
				Location loc = player.getLocation();
				String location = (loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ());
				this.getConfig().set("spawn_location", location);
				this.saveConfig();				
				player.sendMessage(ChatColor.GOLD + "Spawn point set!");


			}
		}
		return true;
	}

	@EventHandler
	public void onVehicleExit(VehicleExitEvent event){
		final Player player = (Player) event.getExited();
		String location = this.getConfig().getString("spawn_location");
		String[] loc = location.split("\\|");

		World world = Bukkit.getWorld(loc[0]);
		Double x = Double.parseDouble(loc[1]);
		Double y = Double.parseDouble(loc[2]);
		Double z = Double.parseDouble(loc[3]);
		 
		final Location finalloc = new Location(world, x, y, z);
		if(enable == true){
			if (!player.hasPermission("cartwarp.bypass")){
				if (player instanceof Player){
					if (event.getVehicle() instanceof Minecart){
						String theMessage = getConfig().getString("exit_message");
						player.sendMessage(ChatColor.GOLD + theMessage);
						Bukkit.getScheduler().scheduleSyncDelayedTask(this,new Runnable(){public void run(){
							player.teleport(finalloc);
						}},1L);

					}
				}
			}	
		}
	}
}

