package ovh.akio.mc.ElytraFlight;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;


public class ElytraEvent implements Listener {
	public Main plugin;
	public ElytraEvent(Main instance){
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Main.Elytra.put(e.getPlayer(), new ElytraPlayer(e.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_AIR){
			try{
				Integer uses = Integer.parseInt(e.getItem().getItemMeta().getDisplayName().replace("§a§lElytra JetPack - " , ""));
				ItemStack current = createStack(uses);
				if(e.getItem().equals(current)){
					// To avoid duplication glitch
					if(e.getPlayer().hasPermission("elytra.use")){
						if(e.getHand() != EquipmentSlot.OFF_HAND){
							if(e.getPlayer().getGameMode() != GameMode.CREATIVE){
								uses = uses - 1;
								int itemSlot = e.getPlayer().getInventory().getHeldItemSlot();
								if(uses == 0){
									e.getPlayer().getInventory().setItem(itemSlot, new ItemStack(Material.AIR));
									e.getPlayer().sendMessage(plugin.getConfig().getString("messages.prefix") + " " + plugin.getConfig().getString("messages.noMoreUse"));
								}else{
									e.getPlayer().getInventory().setItem(itemSlot, createStack(uses));
								}
								propulsePlayer(e.getPlayer());
							}else{
								propulsePlayer(e.getPlayer());
							}
						}
					}else{
						e.getPlayer().sendMessage(plugin.getConfig().getString("messages.prefix") + " " + plugin.getConfig().getString("messages.notPermUse"));
					}
					
				}
			}catch(Exception ex){
				plugin.getLogger().log(Level.WARNING, "§3§lItemStack fail : " + ex.getMessage());
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		ElytraPlayer p = Main.Elytra.get(e.getPlayer());
		Location l = e.getPlayer().getLocation();
		if(p.active && e.getPlayer().isGliding()){
			Bukkit.getWorld(e.getPlayer().getWorld().getName()).spawnParticle(p.particle,l.getX(), l.getY(), l.getZ(), 10, 0.1, 0.1, 0.1,0);
		}
	}
	
	public void propulsePlayer(Player p){
		double pitch = ((p.getLocation().getPitch() +90 ) * Math.PI) / 180;
		double yaw  = ((p.getLocation().getYaw() +90)  * Math.PI) / 180;
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		Vector vector = new Vector(x, z, y);
		p.setVelocity(vector.multiply(1.5));
		p.setGliding(true);
	}
	
	public ItemStack createStack(int uses){
		ItemStack elytra = new ItemStack(Material.EMERALD);
		ItemMeta elytraMeta = elytra.getItemMeta();
		elytraMeta.setDisplayName("§a§lElytra JetPack - " + uses);
		elytra.setItemMeta(elytraMeta);
		return elytra;
	}
}
