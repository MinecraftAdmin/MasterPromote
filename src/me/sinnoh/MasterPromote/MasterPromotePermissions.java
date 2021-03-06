package me.sinnoh.MasterPromote;



import me.sinnoh.MasterPromote.Api.PlayerPromoteEvent;
import me.sinnoh.MasterPromote.Api.PlayerPromoteEvent.PROMOTIONTYPE;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MasterPromotePermissions 
{
	
	private MasterPromote plugin = MasterPromote.instance;
	public String activePermissions;
	private Plugin pex = Bukkit.getPluginManager().getPlugin("PermissionsEx");
	private Plugin gm = Bukkit.getPluginManager().getPlugin("GroupManager");
	private Plugin pb = Bukkit.getPluginManager().getPlugin("PermissionsBukkit");
	private Plugin bp = Bukkit.getPluginManager().getPlugin("bPermissions");
	private Plugin pr = Bukkit.getPluginManager().getPlugin("Privileges");
	private Plugin yp = Bukkit.getPluginManager().getPlugin("YAPP");
	
	public void loadPermission()
	{
		if(Bukkit.getPluginManager().isPluginEnabled(pex))
		{
			this.activePermissions = "PermissionsEx";
		}
		else if(Bukkit.getPluginManager().isPluginEnabled(gm))
		{
			this.activePermissions = "GroupManager";
		}
		else if(Bukkit.getPluginManager().isPluginEnabled(pb))
		{
			this.activePermissions = "PermissionsBukkit";
		}
		else if(Bukkit.getPluginManager().isPluginEnabled(bp))
		{
			this.activePermissions = "bPermissions";
		}
		else if(Bukkit.getPluginManager().isPluginEnabled(pr))
		{
			this.activePermissions = "Privileges";
		}
		else if(Bukkit.getPluginManager().isPluginEnabled(yp))
		{
			this.activePermissions = "YAPP";
		}
		else
		{
			this.activePermissions = "none";
		}
	}
	
	public void promote(Player player, String group, PROMOTIONTYPE type)
	{
		PlayerPromoteEvent	event = new PlayerPromoteEvent(player, group, type, activePermissions);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCanceled())
		{
			sUtil.log("PlayerPromotionEvent has been canceled");
			return;
		}
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		if(!plugin.config.getString("PromoteSyntax").equalsIgnoreCase("none"))
		{
			String command = plugin.config.getString("PromoteSyntax");
			command = command.replace("<player>", player.getName());
			command = command.replace("<group>", group);
			Bukkit.dispatchCommand(console, command);
		}
		else if(activePermissions.equals("PermissionsEx"))
		{
			//Bukkit.dispatchCommand(console, "pex user " + player.getName() + " group set " + group);
			PermissionUser pexuser = PermissionsEx.getUser(player);
			for(PermissionGroup g : pexuser.getGroups())
			{
				pexuser.removeGroup(g);
			}
			pexuser.addGroup(group);
		}
		else if(activePermissions.equals("GroupManager"))
		{
			Bukkit.dispatchCommand(console, "manuadd " + player.getName() + " " + group);
		}
		else if(activePermissions.equals("PermissionsBukkit"))
		{
			Bukkit.dispatchCommand(console, "permissions player setgroup " + player.getName() + " " + group);
		}
		else if(activePermissions.equals("bPermissions"))
		{
			Bukkit.dispatchCommand(console, "world " + player.getWorld().getName());
			Bukkit.dispatchCommand(console, "user " + player.getName());
			Bukkit.dispatchCommand(console, "user setgroup " + group);
		}
		else if(activePermissions.equals("Privileges"))
		{
			Bukkit.dispatchCommand(console, "priv group set " + player.getName() + " " + group);
		}
		else if(activePermissions.equalsIgnoreCase("YAPP"))
		{
			Bukkit.dispatchCommand(console, "yapp o:" + player.getName());
			Bukkit.dispatchCommand(console, "yapp +g "  + group);
			Bukkit.dispatchCommand(console, "yapp @");
		}
	}


}
