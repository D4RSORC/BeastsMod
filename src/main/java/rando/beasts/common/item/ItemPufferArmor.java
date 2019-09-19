package rando.beasts.common.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import java.util.List;
import java.util.Map;

public class ItemPufferArmor extends BeastsArmor {
    public ItemPufferArmor(String type, EquipmentSlotType armorType) {
        super("puffer_" + type, ArmorMaterial.LEATHER, armorType);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if(((NonNullList<ItemStack>)entityIn.getArmorInventoryList()).stream().allMatch(s -> s.getItem() instanceof ItemPufferArmor)) for (ItemStack s : entityIn.getArmorInventoryList()) {
            if(!containsEnchantment(s, Enchantments.THORNS, 2)) s.addEnchantment(Enchantments.THORNS, 2);
        } else for (ItemStack s : entityIn.getArmorInventoryList()) {
            if (containsEnchantment(s, Enchantments.THORNS, 2)) {
            	removeEnchantment(s, Enchantments.THORNS, 2);
            }
        }
    }
    
    private void removeEnchantment(ItemStack stack, Enchantment enchantment, int lvl) {
    	Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
    	enchantments.remove(enchantment, lvl);
    	EnchantmentHelper.setEnchantments(enchantments, stack);
    }

    private boolean containsEnchantment(ItemStack stack, Enchantment enchantment, int lvl) {
    	Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
    	return enchantments.containsKey(enchantment) && enchantments.get(enchantment) == lvl;
    }

}
