package name.shieldwall;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.Task;

import java.util.ArrayList;
import java.util.Iterator;

public class ShieldWall extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("插件已加载！");
    }
    
    /**
     * 方块放置事件
     * @param event 事件
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();
        final Block block = event.getBlockReplace();
        if (player == null || item == null || block == null) {
            return;
        }
        final Level level = player.getLevel();
        CompoundTag tag = item.getNamedTag();
        if (item.getId() == 241 && item.getDamage() == 3) {
            level.addSound(block, Sound.RANDOM_ANVIL_USE);
            //>315 <45  X
            //>135 <225 X
            final double yaw = player.getYaw();
            getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
                @Override
                public void onRun() {
                    final ArrayList<Vector3> blockList = new ArrayList<Vector3>();
                    blockList.add(block);
                    for (int y = block.getFloorY() ; y < (block.getFloorY() + 6); y++) {
                        if ((yaw > 315 || yaw < 45) || (yaw > 135 && yaw < 225)) {
                            for (int x = block.getFloorX() ; x < (block.getFloorX() + 4); x++) {
                                Vector3 vector3 = new Vector3(x, y, block.getFloorZ());
                                if (level.getBlock(vector3).getId() == 0) {
                                    level.setBlock(vector3, Block.get(241, 3));
                                    blockList.add(vector3);
                                }
                            }
                            for (int x = block.getFloorX() ; x > (block.getFloorX() - 4); x--) {
                                Vector3 vector3 = new Vector3(x, y, block.getFloorZ());
                                if (level.getBlock(vector3).getId() == 0) {
                                    level.setBlock(vector3, Block.get(241, 3));
                                    blockList.add(vector3);
                                }
                            }
                        }else {
                            for (int z = block.getFloorZ() ; z < (block.getFloorZ() + 4); z++) {
                                Vector3 vector3 = new Vector3(block.getFloorX(), y, z);
                                if (level.getBlock(vector3).getId() == 0) {
                                    level.setBlock(vector3, Block.get(241, 3));
                                    blockList.add(vector3);
                                }
                            }
                            for (int z = block.getFloorZ() ; z > (block.getFloorZ() - 4); z--) {
                                Vector3 vector3 = new Vector3(block.getFloorX(), y, z);
                                if (level.getBlock(vector3).getId() == 0) {
                                    level.setBlock(vector3, Block.get(241, 3));
                                    blockList.add(vector3);
                                }
                            }
                        }
                    }
                    Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                        @Override
                        public void onRun(int i) {
                            Iterator<Vector3> it = blockList.iterator();
                            while (it.hasNext()) {
                                level.setBlock(it.next(), Block.get(0));
                                it.remove();
                            }
                        }
                    }, 100);
                }
            });
        }
    }

}
