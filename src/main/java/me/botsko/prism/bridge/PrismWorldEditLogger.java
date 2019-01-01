package me.botsko.prism.bridge;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionFactory;
import me.botsko.prism.actionlibs.RecordingQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;

public class PrismWorldEditLogger extends AbstractDelegateExtent {
	private final Actor player;
	private final World world;

	public PrismWorldEditLogger(Actor player, Extent extent, World world) {
		super(extent);
		this.player = player;
		this.world = world;
	}

	@Override
    public boolean setBlock(Vector pt, BlockStateHolder newBlock) throws WorldEditException {
		if (Prism.config.getBoolean("prism.tracking.world-edit")) {
            Location loc = BukkitAdapter.adapt(world, pt);
			Block oldBlock = loc.getBlock();
            Material newMaterial = BukkitAdapter.adapt(newBlock.getBlockType());
            BlockData newData = BukkitAdapter.adapt(newBlock);
			RecordingQueue.addToQueue(ActionFactory.createBlockChange("world-edit", loc, oldBlock.getType(),
					oldBlock.getBlockData(), newMaterial, newData, Bukkit.getPlayer(player.getUniqueId())));
		}
		
		return super.setBlock(pt, newBlock);
	}
}