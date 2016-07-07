package ovh.akio.mc.ElytraFlight;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ElytraPlayer {
	public Particle particle;
	public boolean active;
	public Player p;
	public ElytraPlayer(Player pl){
		p = pl;
		active = false;
		particle = Particle.FIREWORKS_SPARK;
	}
}
