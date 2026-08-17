package com.lantern_config.client;

import com.lantern_config.LanternMode;
import com.lantern_config.ModNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ModNetworkingClient {
	public static void sendModeUpdate(BlockPos pos, LanternMode mode) {
		FriendlyByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		buf.writeVarInt(mode.ordinal());
		ClientPlayNetworking.send(ModNetworking.SET_MODE_PACKET, buf);
	}
}