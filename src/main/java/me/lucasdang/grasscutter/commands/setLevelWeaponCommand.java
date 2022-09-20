package me.lucasdang.grasscutter.commands;

import emu.grasscutter.command.Command;
import emu.grasscutter.command.CommandHandler;
import emu.grasscutter.data.GameData;
import emu.grasscutter.data.excels.WeaponPromoteData;
import emu.grasscutter.game.avatar.Avatar;
import emu.grasscutter.game.inventory.GameItem;
import emu.grasscutter.game.player.Player;
import emu.grasscutter.net.proto.ItemParamOuterClass;
import emu.grasscutter.server.packet.send.*;
import emu.grasscutter.utils.Position;

import java.util.ArrayList;
import java.util.List;

@Command(label = "weaponlevel", aliases = "lvweap", usage = "[level]")
public class setLevelWeaponCommand implements CommandHandler {
    @Override
    public void execute(Player sender, Player targetPlayer, List<String> args) {
        if (args.size() < 1) {
            this.sendUsageMessage(sender);
            return;
        }

        int level = Integer.parseInt(args.get(0));

        GameItem weapon = targetPlayer.getTeamManager().getCurrentAvatarEntity().getAvatar().getWeapon();
        Avatar avatar = sender.getAvatars().getAvatarById(targetPlayer.getTeamManager().getCurrentAvatarEntity().getAvatar().getAvatarId());

        int oldLevel = weapon.getLevel();
        int oldPromoteLevel = weapon.getPromoteLevel();

        int promoteLevel = 0;

        // Promote Level
        if (level >= 20 && level < 40) {
            promoteLevel = 1;
        } else if (level >= 40 && level < 50) {
            promoteLevel = 2;
        } else if (level >= 50 && level < 60) {
            promoteLevel = 3;
        } else if (level >= 60 && level < 70) {
            promoteLevel = 4;
        } else if (level >= 70 && level < 80) {
            promoteLevel = 5;
        } else if (level >= 80 && level <= 90) {
            promoteLevel = 6;
        }

        weapon.setLevel(level);
        weapon.setPromoteLevel(promoteLevel);
        weapon.save();

        avatar.recalcStats();
        avatar.save();

//        sender.sendPacket(new PacketWeaponUpgradeRsp(weapon, oldLevel, new ArrayList<ItemParamOuterClass.ItemParam>()));
//        sender.sendPacket(new PacketWeaponPromoteRsp(weapon, oldPromoteLevel));
        sender.sendPacket(new PacketAvatarAddNotify(avatar, false));
//        sender.sendPacket(new PacketAvatarEquipChangeNotify(avatar, weapon));

        CommandHandler.sendMessage(targetPlayer, "Changed level!");

        this.reloadLevel(targetPlayer);
    }

    public void reloadLevel(Player targetPlayer) {
        try {
            int scene = targetPlayer.getSceneId();

            Position targetPlayerPos = targetPlayer.getPosition();
            targetPlayer.getWorld().transferPlayerToScene(targetPlayer, 1, targetPlayerPos);
            targetPlayer.getWorld().transferPlayerToScene(targetPlayer, scene, targetPlayerPos);
            targetPlayer.getScene().broadcastPacket(new PacketSceneEntityAppearNotify(targetPlayer));

        } catch (Exception e) {
            CommandHandler.sendMessage(targetPlayer, "Failed to reload! Relog to apply changes.");
        }
    }
}
