package net.prr628craft.pettransfer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PetCast {

    public static Entity getLookedAtEntity(ServerPlayer player, double maxDistance) {
        // 1. Get player's eye position and look direction vector
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookDir = player.getViewVector(1.0F);
        Vec3 reachVec = eyePos.add(lookDir.scale(maxDistance));

        // 2. Define the bounding box (AABB) to search for target entities within
        AABB searchBox = player.getBoundingBox()
                .expandTowards(lookDir.scale(maxDistance))
                .inflate(1.0D, 1.0D, 1.0D);

        // 3. Perform the unobfuscated server-side entity raycast
        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                player,
                eyePos,
                reachVec,
                searchBox,
                (entity) -> !entity.isSpectator() && entity.isPickable(), // Filter out spectators
                maxDistance * maxDistance // Squared distance limit
        );

        // 4. Return the entity if the ray struck one
        if (hitResult != null) {
            return hitResult.getEntity();
        }

        return null; // No entity found
    }
}
