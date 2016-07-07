package ovh.akio.mc.ElytraFlight;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandElytra implements CommandExecutor {
	public Main plugin;
	public CommandElytra(Main instance){
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		String prefix = plugin.getConfig().getString("messages.prefix") + " ";
		
		
		// From console
		if(!(arg0 instanceof Player)){
			arg0.sendMessage(prefix + plugin.getConfig().getString("messages.notPlayer"));
			return true;
		}
		
		Player p = (Player) arg0;
		
		switch(arg3.length){
		
		// If not arguments specified
			case 0:
				if(!p.hasPermission("elytra.get")){
					p.sendMessage(prefix + plugin.getConfig().getString("messages.notPermCommand"));
					return true;
				}
				ItemStack toGive = createStack(plugin.getConfig().getInt("elytra.itemDefaultUses"));
				p.getInventory().addItem(toGive);
				break;
		
			
			case 1:
				if(arg3[0].equals("help")){
					showHelp(p);
					return true;
							
							
				}else if(arg3[0].equals("particles")){
					if(p.hasPermission("elytra.activate")){
						ElytraPlayer ep = Main.Elytra.get(arg0);
						if(ep.active){
							ep.active = false;
							p.sendMessage(prefix + plugin.getConfig().getString("messages.particlesOff"));
						}else{
							ep.active = true;
							p.sendMessage(prefix + plugin.getConfig().getString("messages.particlesOn"));
						}
						return true;
					}else{
						p.sendMessage(prefix + plugin.getConfig().getString("messages.notPermCommand"));
						return true;
					}
				
					
				}else if(arg3[0].equals("change")){
					p.sendMessage("§3§l-=[ElytraFlight]=-");
					p.sendMessage("/elytra change list: " + plugin.getConfig().getString("helpMsg.elytra_list"));
					p.sendMessage("/elytra change <particles> : " + plugin.getConfig().getString("helpMsg.elytra_change"));
					return true;
					
				}else if(arg3[0].equals("reload")){
					if(arg0.hasPermission("elytra.reload")){
						if(!(new File(plugin.getDataFolder() + plugin.getConfig().getName()).exists())) { 
							plugin.getLogger().log(Level.INFO, "Copying default config.yml file.");
							plugin.getConfig().options().copyDefaults(true);
							plugin.saveConfig();
						}else{
							plugin.getLogger().log(Level.INFO, "Found an existing config.yml.");
							plugin.reloadConfig();
							plugin.saveConfig();
						}
						return true;
					}else{
						p.sendMessage(prefix + plugin.getConfig().getString("messages.notPermCommand"));
						return true;
					}
				
				}else{
					if(p.hasPermission("elytra.get")){
						try{
							Integer pnb = Integer.parseInt(arg3[0]);
							ItemStack toGive1 = createStack(pnb);
							p.getInventory().addItem(toGive1);
						}catch(Exception e){
							showHelp(p);
						}
						return true;
					}else{
						p.sendMessage(prefix + plugin.getConfig().getString("messages.notPermCommand"));
						return true;
					}
				}
			case 2:
				
				if(arg3[0].equals("change")){
					if(p.hasPermission("elytra.change")){
						if(arg3[1].equals("list")){
							p.sendMessage(prefix + Arrays.toString(Particle.values()));
							return true;
						}else if(Particle.valueOf(arg3[1]) != null){
							ElytraPlayer ep = Main.Elytra.get(arg0);
							ep.particle = Particle.valueOf(arg3[1]);
							p.sendMessage(prefix + plugin.getConfig().getString("messages.particlesChange").replace("{0}", arg3[1]));
							return true;
						}else{
							showHelp(p);
							return true;
						}
					}else{
						p.sendMessage(prefix + plugin.getConfig().getString("messages.notPermCommand"));
						return true;
					}
				}else{
					showHelp(p);
					return true;
				}
		default:
				showHelp(p);
				return true;
		}
			return true;
	}

	
	public void showHelp(Player p){
		p.sendMessage("§3§l-=[ElytraFlight]=-");
		p.sendMessage("/elytra : " + plugin.getConfig().getString("helpMsg.elytra"));
		p.sendMessage("/elytra <nb> : " + plugin.getConfig().getString("helpMsg.elytra_number"));
		p.sendMessage("/elytra particles : " + plugin.getConfig().getString("helpMsg.elytra_particle"));
		p.sendMessage("/elytra change list: " + plugin.getConfig().getString("helpMsg.elytra_list"));
		p.sendMessage("/elytra change <particles> : " + plugin.getConfig().getString("helpMsg.elytra_change"));
		p.sendMessage("/elytra reload : " + plugin.getConfig().getString("helpMsg.elytra_reload"));
	}
	
	public ItemStack createStack(int uses){
		ItemStack elytra = new ItemStack(Material.EMERALD);
		ItemMeta elytraMeta = elytra.getItemMeta();
		elytraMeta.setDisplayName("§a§lElytra JetPack - " + uses);
		elytra.setItemMeta(elytraMeta);
		return elytra;
	}
}
