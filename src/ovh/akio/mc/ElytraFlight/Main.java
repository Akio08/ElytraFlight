package ovh.akio.mc.ElytraFlight;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin {
	
	public static Map<Player, ElytraPlayer> Elytra = new HashMap<>();
	
	public void onEnable(){
		this.getLogger().log(Level.INFO, "Loading config...");
		loadConfiguration();
		this.getLogger().log(Level.INFO, "Done");
		this.getServer().getPluginManager().registerEvents(new ElytraEvent(this), this);
		this.getCommand("elytra").setExecutor(new CommandElytra(this));
		for(Player p : Bukkit.getOnlinePlayers()){
			Main.Elytra.put(p, new ElytraPlayer(p));
		}
	}
	
	public void onDisable(){
		
	}
	
	//------------------------
	public void loadConfiguration(){
		
		if(!(new File(getDataFolder() + getConfig().getName()).exists())) { 
			this.getLogger().log(Level.INFO, "Copying default config.yml file.");
			getConfig().options().copyDefaults(true);
		    saveConfig();
		}else{
			this.getLogger().log(Level.INFO, "Found an existing config.yml.");
	        reloadConfig();
	        saveConfig();
		}
	}
}
