package com.forgecraft.modding.inventory;

import com.forgecraft.modding.Main;
import com.forgecraft.modding.blocks.container.ContainerFusionFurnace;
import com.forgecraft.modding.blocks.tileentity.TileEntityFusionFurnace;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GUIFusionFurnace extends GuiContainer
{
	private static final ResourceLocation GUI_fusionfurnece = new ResourceLocation(Main.MODID + ":textures/gui/container/gui_fusionfurnace.png");
	private final InventoryPlayer player;
	private final IInventory inventoryFusionFurnace;
	
	public GUIFusionFurnace(InventoryPlayer player, IInventory inventory)
	{
		super(new ContainerFusionFurnace(player, inventory));
		
		this.player = player;
		inventoryFusionFurnace = inventory;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String string = inventoryFusionFurnace.getDisplayName().getUnformattedText();
		fontRenderer.drawString(string, xSize / 2 - fontRenderer.getStringWidth(string) / 2, 6, 4210752);
		fontRenderer.drawString(player.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
		String fuelText = inventoryFusionFurnace.getField(5) + " / " + 5000;
		fontRenderer.drawString(fuelText, 50, 44, 0x7d7d7d);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_fusionfurnece);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);
		
		if(TileEntityFusionFurnace.isFuel(inventoryFusionFurnace))
		{
			int fuelLevel = getFuelLevel(24);
			drawTexturedModalRect(marginHorizontal + 48, marginVertical + 55, 4, 167, fuelLevel, 14);
		}
		
		int fusionTimeLevel = getFusionTimeLevel(13);
		drawTexturedModalRect(marginHorizontal + 47, marginVertical + 35 - fusionTimeLevel, 176, 13 - fusionTimeLevel, 14, fusionTimeLevel);
		
		int processTimeLevel = getProcessTimeLevel(24);
		drawTexturedModalRect(marginHorizontal + 91, marginVertical + 22, 176, 14, processTimeLevel + 1, 16);
	}

	private int getProcessTimeLevel(int pixel)
	{
		int currentProcess = inventoryFusionFurnace.getField(1);
		int maxProcess = pixel * (inventoryFusionFurnace.getField(3) / 22);
		return maxProcess != 0 && currentProcess != 0 ? currentProcess * pixel / maxProcess : 0;
	}

	private int getFusionTimeLevel(int pixel)
	{
		int currentFusion = inventoryFusionFurnace.getField(0);
		int maxFusion = pixel * (inventoryFusionFurnace.getField(2) / 13);
		return maxFusion != 0 && currentFusion != 0 ? currentFusion * pixel / maxFusion : 0;
	}
	
	private int getFuelLevel(int pixel)
	{
		int maxFuel = pixel * (5000 / 96);
		return inventoryFusionFurnace.getField(5) * pixel / maxFuel;
	}
}
