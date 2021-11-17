package eu.decentsoftware.holograms.api.actions;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

public abstract class ActionType {

	private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

	/*
	 * Cache
	 */

	private static final Map<String, ActionType> VALUES = Maps.newHashMap();

	public static ActionType getByName(String name) {
		return VALUES.get(name);
	}

	public static Collection<ActionType> getActionTypes() {
		return VALUES.values();
	}

	/*
	 * Actions
	 */

	public static final ActionType MESSAGE = new ActionType("MESSAGE") {
		@Override
		public boolean execute(Player player, String... args) {
			Validate.notNull(player);

			String string = String.join(" ", args);
			Common.tell(player, string.replace("{player}", player.getName()));
			return true;
		}
	};

	public static final ActionType COMMAND = new ActionType("COMMAND") {
		@Override
		public boolean execute(Player player, String... args) {
			Validate.notNull(player);

			String string = String.join(" ", args);
			Bukkit.getScheduler().runTask(DecentHologramsAPI.get().getPlugin(), () -> {
				//
				Bukkit.dispatchCommand(player, string.replace("{player}", player.getName()));
			});
			return true;
		}
	};

	public static final ActionType CONSOLE = new ActionType("CONSOLE") {
		@Override
		public boolean execute(Player player, String... args) {
			Validate.notNull(player);

			String string = String.join(" ", args);
			Bukkit.getScheduler().runTask(DecentHologramsAPI.get().getPlugin(), () -> {
				//
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replace("{player}", player.getName()));
			});
			return true;
		}
	};

	public static final ActionType CONNECT = new ActionType("CONNECT") {
		@Override
		public boolean execute(Player player, String... args) {
			Validate.notNull(player);
			if (args != null && args.length >= 1) {
				BungeeUtils.connect(player, args[0]);
			}
			return true;
		}
	};

	public static final ActionType TELEPORT = new ActionType("TELEPORT") {
		@Override
		public boolean execute(Player player, String... args) {
			Validate.notNull(player);

			String string = String.join(" ", args);
			Location location = LocationUtils.asLocation(string);
			if (location == null) {
				return false;
			}
			player.teleport(location);
			return true;
		}
	};

	public static final ActionType SOUND = new ActionType("SOUND") {
		@Override
		public boolean execute(Player player, String... args) {
			Validate.notNull(player);

			if (args.length == 0) return true;
			Sound sound = Sound.valueOf(args[0]);
			if (args.length < 3) {
				player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
			} else {
				player.playSound(player.getLocation(), sound, Float.parseFloat(args[1]), Float.parseFloat(args[2]));
			}
			return true;
		}
	};

	public static final ActionType PERMISSION = new ActionType("PERMISSION") {
		@Override
		public boolean execute(Player player, String... args) {
			Validate.notNull(player);
			return args[0] != null && !args[0].trim().isEmpty() && player.hasPermission(args[0]);
		}
	};

	public static final ActionType NEXT_PAGE = new ActionType("NEXT_PAGE") {
		@Override
		public boolean execute(Player player, String... args) {
			if (args == null || args.length == 0) return true;
			Hologram hologram = DECENT_HOLOGRAMS.getHologramManager().getHologram(args[0]);
			if (hologram == null) return true;
			int nextPage = hologram.getPlayerPage(player) + 1;
			if (nextPage < 0 || hologram.size() <= nextPage) return true;
			hologram.show(player, nextPage);
			return true;
		}
	};

	public static final ActionType PREV_PAGE = new ActionType("PREV_PAGE") {
		@Override
		public boolean execute(Player player, String... args) {
			if (args == null || args.length == 0) return true;
			Hologram hologram = DECENT_HOLOGRAMS.getHologramManager().getHologram(args[0]);
			if (hologram == null) return true;
			int prevPage = hologram.getPlayerPage(player) - 1;
			if (prevPage < 0 || hologram.size() <= prevPage) return true;
			hologram.show(player, prevPage);
			return true;
		}
	};

	/*
	 * Abstract Methods
	 */

	@Getter
	private final String name;

	public ActionType(String name) {
		this.name = name;
		VALUES.put(this.name, this);
	}

	public abstract boolean execute(Player player, String... args);

}
