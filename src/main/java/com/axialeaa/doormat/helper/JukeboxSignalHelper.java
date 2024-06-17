package com.axialeaa.doormat.helper;

import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.jukebox.JukeboxManager;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.util.math.MathHelper;

public class JukeboxSignalHelper {

    public static int getOutput(JukeboxBlockEntity blockEntity) {
        JukeboxManager manager = blockEntity.getManager();
        JukeboxSong song = manager.getSong();

        int output = 0;

        if (song != null)
            output = MathHelper.lerpPositive((float) manager.getTicksSinceSongStarted() / song.getLengthInTicks(), 0, 15);
        else if (!blockEntity.isEmpty())
            output = 15;

        return output;
    }

}
