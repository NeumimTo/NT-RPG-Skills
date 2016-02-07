package cz.neumimto.skills;

import cz.neumimto.IEntity;
import cz.neumimto.ResourceLoader;
import cz.neumimto.SkillLocalization;
import cz.neumimto.core.ioc.Inject;
import cz.neumimto.damage.SkillDamageSource;
import cz.neumimto.damage.SkillDamageSourceBuilder;
import cz.neumimto.entities.EntityService;
import cz.neumimto.players.IActiveCharacter;
import cz.neumimto.utils.Utils;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DamageEntityEvent;

/**
 * Created by NeumimTo on 5.2.2016.
 */
@ResourceLoader.Skill
@ResourceLoader.ListenerClass
public class BrainSap extends ActiveSkill {

    @Inject
    private EntityService entityService;

    public BrainSap() {
        SkillSettings settings = new SkillSettings();
        settings.addNode(SkillNode.COOLDOWN,1000f,10f);
        settings.addNode(SkillNode.RANGE,10f,1f);
        settings.addNode(SkillNode.DAMAGE,10f,10f);
        setLore(SkillLocalization.SKILL_BRAINSAP_LORE);
        super.settings = settings;
        setName("Soulbind");
        setDescription(SkillLocalization.SKILL_BRAINSAP_DESC);
    }

    @Override
    public SkillResult cast(IActiveCharacter iActiveCharacter, ExtendedSkillInfo extendedSkillInfo) {
        float range = extendedSkillInfo.getSkillData().getSkillSettings().getLevelNodeValue(SkillNode.RANGE,extendedSkillInfo.getLevel());
        Living targettedEntity = Utils.getTargettedEntity(iActiveCharacter, (int) range);
        if (targettedEntity != null) {
            SkillDamageSourceBuilder builder = new SkillDamageSourceBuilder();
            builder.setSkill(this);
            builder.setCaster(iActiveCharacter);
            SkillDamageSource s = builder.build();
            IEntity entity = entityService.get(targettedEntity);
            float damage = extendedSkillInfo.getSkillData().getSkillSettings().getLevelNodeValue(SkillNode.DAMAGE,extendedSkillInfo.getLevel());
            entity.getEntity().damage(damage,s);
            return SkillResult.OK;
        }
        return SkillResult.CANCELLED;
    }

    @Listener(order = Order.LAST)
    public void onDamage(DamageEntityEvent event) {
        if (event.isCancelled())
            return;
        if (event.getCause().first(SkillDamageSource.class).isPresent()) {
            SkillDamageSource source = event.getCause().first(SkillDamageSource.class).get();
            if (source.getSkill() == this) {
                IActiveCharacter caster = source.getCaster();
                characterService.healCharacter(caster, (float) event.getFinalDamage());
            }
        }
    }

}
