package de.techgamez.pleezon.utils.gui;

import java.io.IOException;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.ingamegui.Module;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.labymod.main.ModTextures;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.LabyModModuleEditorGui;
import net.labymod.settings.PreviewRenderer;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class PasswordElement extends ControlElement {
    private String currentValue;
    private Consumer<String> changeListener;
    private ModTextField textField;
    private boolean hoverExpandButton;


    public PasswordElement(String displayName, IconData iconData, String currentValue, Consumer<String> changeListener) {
        super(displayName, iconData);
        this.hoverExpandButton = false;
        this.currentValue = currentValue;
        this.changeListener = changeListener;
        this.createTextfield();
    }


    public void createTextfield() {
        this.textField = new ModTextField(-2, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, this.getObjectWidth() - 5, 20);
        this.textField.setMaxStringLength(500);
        this.updateValue();
        this.textField.setCursorPositionEnd();
        this.textField.setFocused(false);
        this.textField.setPasswordBox(true);

    }

    public void updateValue() {
        this.textField.setText(this.currentValue == null ? "" : this.currentValue);
        //.replaceAll(".","*")
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        int width = this.getObjectWidth() - 5;
        if (this.textField != null) {
            this.textField.xPosition = maxX - width - 2;
            this.textField.yPosition = y + 1;
            this.textField.drawTextBox();
           // this.updateValue();
            LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_EXPAND);
            this.hoverExpandButton = mouseX > maxX - this.getObjectWidth() - 12 && mouseX < maxX - this.getObjectWidth() - 7 + 8 && mouseY > y + 1 && mouseY < y + 1 + 8;
            LabyMod.getInstance().getDrawUtils().drawTexture((double)(maxX - this.getObjectWidth() - 7), (double)(y + 1), 0.0D, this.hoverExpandButton ? 130.0D : 0.0D, 256.0D, 128.0D, 8.0D, 8.0D);
        }
    }

    public void unfocus(int mouseX, int mouseY, int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        if (this.hoverExpandButton) {
            this.hoverExpandButton = false;
            Minecraft.getMinecraft().displayGuiScreen(new ExpandedStringElementGui(this.textField, Minecraft.getMinecraft().currentScreen, new Consumer<ModTextField>() {
                public void accept(ModTextField accepted) {
                    textField.setText(accepted.getText());
                    textField.setFocused(true);
                    textField.setCursorPosition(accepted.getCursorPosition());
                    textField.setSelectionPos(accepted.getSelectionEnd());
                    changeListener.accept(textField.getText());
                }
            }));
        }

        this.textField.setFocused(false);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, 0);
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            this.changeListener.accept(this.textField.getText());
        }

    }

    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
    }

    public int getObjectWidth() {
        return 85;
    }

    public static class ExpandedStringElementGui extends GuiScreen {
        private GuiScreen backgroundScreen;
        private Consumer<ModTextField> callback;
        private ModTextField preField;
        private ModTextField expandedField;

        public ExpandedStringElementGui(ModTextField preField, GuiScreen backgroundScreen, Consumer<ModTextField> callback) {
            this.backgroundScreen = backgroundScreen;
            this.callback = callback;
            this.preField = preField;
        }

        public void initGui() {
            super.initGui();
            this.backgroundScreen.width = this.width;
            this.backgroundScreen.height = this.height;
            if (this.backgroundScreen instanceof LabyModModuleEditorGui) {
                PreviewRenderer.getInstance().init(net.labymod.settings.elements.StringElement.ExpandedStringElementGui.class);
            }

            this.expandedField = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), this.width / 2 - 150, this.height / 4 + 45, 300, 20);
            this.expandedField.setMaxStringLength(this.preField.getMaxStringLength());
            this.expandedField.setFocused(true);
            this.expandedField.setText(this.preField.getText());
            this.expandedField.setCursorPosition(this.preField.getCursorPosition());
            this.expandedField.setSelectionPos(this.preField.getSelectionEnd());
            this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 4 + 85, 100, 20, LanguageManager.translate("button_done")));
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            drawRect(0, 0, this.width, this.height, -2147483648);
            drawRect(this.width / 2 - 165, this.height / 4 + 35, this.width / 2 + 165, this.height / 4 + 120, -2147483648);
            this.expandedField.drawTextBox();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.expandedField.mouseClicked(mouseX, mouseY, mouseButton);
            this.callback.accept(this.expandedField);
        }

        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            if (keyCode == 1) {
                Minecraft.getMinecraft().displayGuiScreen(this.backgroundScreen);
            }

            if (this.expandedField.textboxKeyTyped(typedChar, keyCode)) {
                this.callback.accept(this.expandedField);
            }

        }

        public void updateScreen() {
            this.backgroundScreen.updateScreen();
            this.expandedField.updateCursorCounter();
        }

        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);
            if (button.id == 1) {
                Minecraft.getMinecraft().displayGuiScreen(this.backgroundScreen);
            }

        }

        public GuiScreen getBackgroundScreen() {
            return this.backgroundScreen;
        }
    }
}
