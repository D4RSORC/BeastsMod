package rando.beasts.common.utils.handlers;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.NonTamedTargetGoal;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import rando.beasts.common.command.CommandLocateStructure;
import rando.beasts.common.entity.passive.EntityPufferfishDog;
import rando.beasts.common.entity.passive.EntityRabbitman;
import rando.beasts.common.init.BeastsTriggers;
import rando.beasts.common.utils.BeastsReference;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = BeastsReference.ID, bus = Bus.MOD)
public class EventHandler {

	public static void entityJoin(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof OcelotEntity) {
			final OcelotEntity ocelot = (OcelotEntity) event.getEntity();
			if (ocelot != null) {
				ocelot.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(ocelot, EntityPufferfishDog.class,
						10, false, false, target -> target != null && target.getDistance(ocelot) < 32.0));
				ocelot.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(ocelot, EntityRabbitman.class, 10,
						false, false, target -> target != null && target.getDistance(ocelot) < 32.0));
			}
		} else if (event.getEntity() instanceof WolfEntity) {
			final WolfEntity wolf = (WolfEntity) event.getEntity();
			if (wolf != null)
				wolf.targetSelector.addGoal(1, new NonTamedTargetGoal<>(wolf, EntityRabbitman.class, false,
						target -> target != null && target.getDistance(wolf) < 32.0));
		}
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;
		if (player.world.isRemote)
			return;

		Vec3d vec3 = player.getEyePosition(1.0F);
		Vec3d vec3a = player.getLook(1.0F);
		int distance = 14;
		Vec3d vec3b = vec3.add(vec3a.x * distance, vec3a.y * distance, vec3a.z * distance);

		Entity ee = null;
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player,
				player.getBoundingBox().grow(distance + 1));
		double d0 = 0.0D;
		for (Entity entity1 : list) {
			if (entity1 != player) {
				AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(0.30000001192092896D);
				Optional<Vec3d> raytrace = axisalignedbb.rayTrace(vec3, vec3b);
				if (raytrace.isPresent()) {
					RayTraceResult raytraceresult = new EntityRayTraceResult(entity1, raytrace.get());
					double d1 = vec3.squareDistanceTo(raytraceresult.getHitVec());
					if (d1 < d0 || d0 == 0.0D) {
						ee = entity1;
						d0 = d1;
					}
				}
			}
		}
		if (ee instanceof EntityPufferfishDog)
			BeastsTriggers.DISCOVER_PUFFERFISH_DOG.trigger((ServerPlayerEntity) player);
	}

	@SubscribeEvent
	public static void lootTable(LootTableLoadEvent event) {
		// TODO
		if (event.getName().toString().equals("minecraft:gameplay/fishing")) {
			// event.getTable().getPool("main").addEntry(LootHandler.getInjectEntry("fish",
			// 100));
		}
	}

	@SubscribeEvent
	public static void registerCommands(FMLServerStartingEvent event) {
		CommandLocateStructure.register(event.getCommandDispatcher());
	}
}
