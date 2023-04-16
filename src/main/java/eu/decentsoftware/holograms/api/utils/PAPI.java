package eu.decentsoftware.holograms.api.utils;

import com.redefantasy.core.shared.misc.utils.Patterns;
import com.redefantasy.core.spigot.misc.placeholders.PlaceholderController;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PAPI {

    /**
     * Check if PlaceholderAPI is available.
     *
     * @return True if PlaceholderAPI is available.
     */
    public static boolean isAvailablePAPI() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Check if core is available.
     *
     * @return True if core is available.
     */
    public static boolean isAvailableCore() {
        return Bukkit.getPluginManager().isPluginEnabled("core");
    }

    /**
     * Set placeholders to given String for given Player.
     *
     * @param player The player.
     * @param string The string.
     * @return The string with replaced placeholders.
     */
    public static String setPlaceholders(Player player, String string) {
        if (isAvailablePAPI()) {
            return PlaceholderAPI.setPlaceholders(player, string);
        }

        if (isAvailableCore()) {
            return PlaceholderController.setPlaceholders(player.getUniqueId(), string);
        }

        return string;
    }

    /**
     * Set placeholders to given List of Strings for given Player.
     *
     * @param player     The player.
     * @param stringList The string list.
     * @return The string with replaced placeholders.
     */
    public static List<String> setPlaceholders(Player player, List<String> stringList) {
        if (isAvailablePAPI() || isAvailableCore()) {
            return stringList.stream().map(s -> setPlaceholders(player, s)).collect(Collectors.toList());
        }

        return stringList;
    }

    /**
     * Check if the given string contains any placeholders.
     *
     * @param string The string.
     * @return True if the string contains any placeholders, false otherwise.
     */
    public static boolean containsPlaceholders(String string) {
        if (isAvailablePAPI()) {
            return PlaceholderAPI.containsPlaceholders(string);
        }

        if (isAvailableCore()) {
            return Patterns.PLACEHOLDER.matches(string);
        }

        return false;
    }

}
