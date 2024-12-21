package org.vortex.zapi.client.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vortex.zapi.client.screens.KeyCreationScreen;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void addCustomButton(CallbackInfo ci) {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("ZAPI"), (btn) -> {
            assert client != null;
            client.setScreen(new KeyCreationScreen(Text.literal("ZAPI-Config")));
        }).dimensions(10, 6, 60, 20).build();
        this.addDrawableChild(buttonWidget);
    }
}
