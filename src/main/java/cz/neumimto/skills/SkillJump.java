package cz.neumimto.skills;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import cz.neumimto.ResourceLoader;
import cz.neumimto.SkillLocalization;
import cz.neumimto.configuration.Localization;
import cz.neumimto.damage.SkillDamageSource;
import cz.neumimto.players.IActiveCharacter;
import cz.neumimto.utils.Utils;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSources;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;

import java.util.Optional;

/**
 * Created by NeumimTo on 23.12.2015.
 */
@ResourceLoader.Skill
public class SkillJump extends ActiveSkill {

    public static String name = "Jump";

    public SkillJump() {
        setName(name);
        setDamageType(null);
        setDescription(SkillLocalization.SKILL_JUMP_DESC);
        SkillSettings skillSettings = new SkillSettings();
        skillSettings.addNode(SkillNode.VELOCITY, 2,2);
        settings = skillSettings;
    }


    @Override
    public SkillResult cast(IActiveCharacter character, ExtendedSkillInfo info) {
        Vector3d rotation = character.getEntity().getRotation();
        Vector3d direction = Quaterniond.fromAxesAnglesDeg(rotation.getX(), -rotation.getY(), rotation.getZ()).getDirection();
        Vector3d velocity = direction.add(0,1,0).mul(settings.getLevelNodeValue(SkillNode.VELOCITY, info.getLevel()));
        character.getEntity().offer(Keys.VELOCITY,velocity);
        return SkillResult.OK;
    }
}