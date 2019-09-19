package rando.beasts.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.inventory.ContainerLandwhaleInventory;
import rando.beasts.common.utils.BeastsReference;

public class GuiLandwhaleInventory extends ContainerScreen<ContainerLandwhaleInventory> {
    private static final ResourceLocation GUI = new ResourceLocation(BeastsReference.ID, "textures/gui/container/landwhale.png");
    private EntityLandwhale landwhale;
    
    public GuiLandwhaleInventory(ContainerLandwhaleInventory container, PlayerInventory inv, ITextComponent name) {
    	super(container, inv, name);    	
    }
    
    public GuiLandwhaleInventory(ContainerLandwhaleInventory container, EntityLandwhale landwhale, PlayerEntity player, ITextComponent titleIn) {
        super(container, player.inventory, titleIn);
        
        this.landwhale = landwhale;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        InventoryScreen.drawEntityOnScreen(i + 60, j + 50, 20, mouseX, mouseY, this.container.landwhale);
    }
}
