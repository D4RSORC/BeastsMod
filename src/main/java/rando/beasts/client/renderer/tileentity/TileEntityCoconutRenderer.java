package rando.beasts.client.renderer.tileentity;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeGLX;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.GLXCapabilities;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

import rando.beasts.client.model.ModelCoconut;
import rando.beasts.client.renderer.entity.RenderCoconutCrab;
import rando.beasts.common.tileentity.TileEntityCoconut;

public class TileEntityCoconutRenderer extends TileEntityRenderer<TileEntityCoconut> {

	private final ModelCoconut model = new ModelCoconut();

    @Override
    public void render(TileEntityCoconut te, double x, double y, double z, float partialTicks, int destroyStage) {
        if(te != null) super.render(te, x, y, z, partialTicks, destroyStage);
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translatef((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
        GlStateManager.translatef(-0.501F, -1.501F, 0.501F);
        GlStateManager.enableAlphaTest();
        GlStateManager.disableLighting();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
        GlStateManager.enableColorMaterial();
        bindTexture(RenderCoconutCrab.TEXTURE);
        model.render(null, 0, 0, -1, 0, 0, 0.0625F);
        GlStateManager.tearDownSolidRenderingTextureCombine();
        GlStateManager.disableColorMaterial();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
