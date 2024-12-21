package org.vortex.zapi.client.mixin;


import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vortex.zapi.client.SettingsHolder;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (SettingsHolder.nofall) {
            if (packet instanceof PlayerMoveC2SPacket) {
                PlayerMoveC2SPacketAccessor packetx = (PlayerMoveC2SPacketAccessor) packet;
                packetx.setOnGround(false);
            }
        }
    }
}
