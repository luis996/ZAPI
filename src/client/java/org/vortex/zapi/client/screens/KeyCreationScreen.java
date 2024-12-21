package org.vortex.zapi.client.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.vortex.zapi.client.SettingsHolder;
import org.vortex.zapi.client.ZapiClient;

import java.util.UUID;

public class KeyCreationScreen extends Screen {
    public KeyCreationScreen(Text title) {
        super(title);
    }
    public ButtonWidget generateButton;
    public ButtonWidget copyButton;
    public ButtonWidget toggleButton;
    public Text toggleButtonText;
    public Text topText;
    @Override
    protected void init() {
        generateButton = ButtonWidget.builder(Text.of("Generate"), (btn) -> {
            SettingsHolder.key = UUID.randomUUID().toString();
            copyButton.active = true;
            topText = Text.of(SettingsHolder.key);
            // x trueCenter = (screen width / 2) - (button width / 2)
            // Trust me bro it works
            // Same applies with Y
        }).dimensions(((this.width / 2) - (75 + (5/2))), ((this.height / 2) - (20 / 2))+30, 75, 20).build();
        copyButton = ButtonWidget.builder(Text.of("Copy"), (btn) -> MinecraftClient.getInstance().keyboard.setClipboard(SettingsHolder.key)).dimensions(((this.width / 2) + 5/2), ((this.height / 2) - (20 / 2))+30, 75, 20).build();
        if (ZapiClient.useAuth) {
            ZapiClient.useAuth = false;
            generateButton.active = false;
            copyButton.active = false;
            topText = Text.of("Avoid turning off auth. It is insecure for everyone.");
            toggleButtonText = Text.of("Auth: OFF");
        } else {
            ZapiClient.useAuth = true;
            generateButton.active = true;
            if (SettingsHolder.key == null) {
                topText = Text.of("Please generate a key.");
                copyButton.active = false;
            } else {
                topText = Text.of(SettingsHolder.key);
                copyButton.active = true;
            }
            toggleButtonText = Text.of("Auth: ON");
        }

        toggleButton = ButtonWidget.builder(toggleButtonText, (btn) -> {
            if (ZapiClient.useAuth) {
                ZapiClient.useAuth = false;
                generateButton.active = false;
                copyButton.active = false;
                topText = Text.of("Avoid turning off auth. It is insecure for everyone.");
                toggleButtonText = Text.of("Auth: OFF");
            } else {
                ZapiClient.useAuth = true;
                generateButton.active = true;
                if (SettingsHolder.key == null) {
                    topText = Text.of("Please generate a key.");
                    copyButton.active = false;
                } else {
                    topText = Text.of(SettingsHolder.key);
                    copyButton.active = true;
                }
                toggleButtonText = Text.of("Auth: ON");
            }
            this.toggleButton.setMessage(toggleButtonText);

        }).dimensions(((this.width / 2) - (75/2)), ((this.height / 2) - (20 / 2))+30+30, 75, 20).build();
        this.addDrawableChild(generateButton);
        this.addDrawableChild(copyButton);
        this.addDrawableChild(toggleButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
        // We'll subtract the font height from the Y position to make the text appear above the button.
        // Subtracting an extra 10 pixels will give the text some padding.
        // textRenderer, text, x, y, color, hasShadow
        assert client != null;
        context.drawCenteredTextWithShadow(client.textRenderer, topText, (this.width / 2), 20, 0xFFFFFF);
    }


}
