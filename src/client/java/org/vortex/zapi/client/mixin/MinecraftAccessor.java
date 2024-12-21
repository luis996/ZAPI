package org.vortex.zapi.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(MinecraftClient.class)
public interface MinecraftAccessor {

    @Accessor("session")
    @Mutable
    void setSession(Session session);


}
