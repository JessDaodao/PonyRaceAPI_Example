package fun.eqad.ponyraceapi_example;

import fun.eqad.ponyrace.api.PonyRaceAPI;
import fun.eqad.ponyrace.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PonyRaceAPI_Example extends JavaPlugin {

    private PonyRaceAPI ponyRaceAPI;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PonyRace") == null) {
            getLogger().severe("PonyRace未安装");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // 获取PonyRace API
        ponyRaceAPI = ((fun.eqad.ponyrace.PonyRace) Bukkit.getPluginManager().getPlugin("PonyRace")).getAPI();

        getLogger().info("PonyRaceAPI_Example已成功加载");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("prtest")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "info":
                testGetInfo(player);
                break;
            case "setrace":
                if (args.length < 2) {
                    player.sendMessage("用法: /prtest setrace <种族>");
                    return true;
                }
                testSetRace(player, args[1]);
                break;
            case "setmana":
                if (args.length < 2) {
                    player.sendMessage("用法: /prtest setmana <值>");
                    return true;
                }
                testSetMana(player, args[1]);
                break;
            case "setstamina":
                if (args.length < 2) {
                    player.sendMessage("用法: /prtest setstamina <值>");
                    return true;
                }
                testSetStamina(player, args[1]);
                break;
            case "setenrage":
                if (args.length < 2) {
                    player.sendMessage("用法: /prtest setenrage <值>");
                    return true;
                }
                testSetEnrage(player, args[1]);
                break;
            default:
                showHelp(player);
        }

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage(ChatColor.YELLOW + "/prtest info - 显示当前种族和资源信息");
        player.sendMessage(ChatColor.YELLOW + "/prtest setrace <种族> - 设置你的种族");
        player.sendMessage(ChatColor.YELLOW + "/prtest setmana <值> - 设置魔力值");
        player.sendMessage(ChatColor.YELLOW + "/prtest setstamina <值> - 设置体力值");
        player.sendMessage(ChatColor.YELLOW + "/prtest setenrage <值> - 设置怒气值(仅麒麟)");
    }

    private void testGetInfo(Player player) {
        // 返回玩家种族
        String race = ponyRaceAPI.getPlayerRace(player);
        player.sendMessage(ChatColor.YELLOW + "种族: " + (race != null ? race : "无"));

        // 返回玩家是否选择种族
        player.sendMessage(ChatColor.YELLOW + "已选择种族: " + ponyRaceAPI.hasChosenRace(player));

        // 判断该玩家是否为指定种族
        if (race != null) {
            player.sendMessage(ChatColor.YELLOW + "是" + race + ": " + ponyRaceAPI.hasRace(player, race));
        }

        // 返回玩家魔力值
        player.sendMessage(ChatColor.AQUA + "魔力值: " + ponyRaceAPI.getPlayerMana(player) + "/100");

        // 返回玩家体力值
        player.sendMessage(ChatColor.GREEN + "体力值: " + ponyRaceAPI.getPlayerStamina(player) + "/100");

        // 返回玩家怒气值
        player.sendMessage(ChatColor.RED + "怒气值: " + ponyRaceAPI.getPlayerEnrage(player) + "/100");

        // 获取更详细的玩家数据
        PlayerDataManager data = ponyRaceAPI.getPlayerData(player.getUniqueId());
        if (data != null) {
            player.sendMessage(ChatColor.YELLOW + "数据对象: " + (data.isHasChosen() ? "有效" : "无效"));
        } else {
            player.sendMessage(ChatColor.RED + "数据对象: 无");
        }

        player.sendMessage(ChatColor.YELLOW + "PonyRace版本: " + ponyRaceAPI.getPlugin().getDescription().getVersion());
    }

    // 设置玩家种族
    private void testSetRace(Player player, String race) {
        boolean success = ponyRaceAPI.setPlayerRace(player, race);
        if (success) {
            player.sendMessage(ChatColor.GREEN + "成功将你的种族设置为: " + race);
            testGetInfo(player);
        } else {
            player.sendMessage(ChatColor.RED + "设置种族失败！");
        }
    }

    // 设置玩家魔力值
    private void testSetMana(Player player, String valueStr) {
        try {
            int value = Integer.parseInt(valueStr);
            boolean success = ponyRaceAPI.setPlayerMana(player, value);
            if (success) {
                player.sendMessage(ChatColor.GREEN + "成功将魔力值设置为: " + value);
                testGetInfo(player);
            } else {
                player.sendMessage(ChatColor.RED + "设置魔力值失败！");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "无效的数字格式！");
        }
    }

    // 设置玩家体力值
    private void testSetStamina(Player player, String valueStr) {
        try {
            int value = Integer.parseInt(valueStr);
            boolean success = ponyRaceAPI.setPlayerStamina(player, value);
            if (success) {
                player.sendMessage(ChatColor.GREEN + "成功将体力值设置为: " + value);
                testGetInfo(player);
            } else {
                player.sendMessage(ChatColor.RED + "设置体力值失败！");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "无效的数字格式！");
        }
    }

    // 设置玩家怒气值
    private void testSetEnrage(Player player, String valueStr) {
        try {
            int value = Integer.parseInt(valueStr);
            boolean success = ponyRaceAPI.setPlayerEnrage(player, value);
            if (success) {
                player.sendMessage(ChatColor.GREEN + "成功将怒气值设置为: " + value);
                testGetInfo(player);
            } else {
                player.sendMessage(ChatColor.RED + "设置怒气值失败！(你可能不是麒麟)");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "无效的数字格式！");
        }
    }
}