package cz.neumimto.skills;

import cz.neumimto.IEntity;
import cz.neumimto.ResourceLoader;
import cz.neumimto.SkillLocalization;
import cz.neumimto.core.ioc.Inject;
import cz.neumimto.damage.SkillDamageSourceBuilder;
import cz.neumimto.entities.EntityService;
import cz.neumimto.players.IActiveCharacter;
import cz.neumimto.utils.Utils;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;

/**
 * Created by NeumimTo on 29.12.2015.
 */
@ResourceLoader.Skill
public class SkillLightning extends ActiveSkill {

    @Inject
    EntityService entityService;

    public SkillLightning() {
        setName("Lightning");
        setLore(SkillLocalization.SKILL_LIGHTNING_LORE);
        setDescription(SkillLocalization.SKILL_LIGHTNING_DESC);
        setDamageType(NDamageType.LIGHTNING);
        SkillSettings skillSettings = new SkillSettings();
        skillSettings.addNode(SkillNode.DAMAGE, 10, 20);
        skillSettings.addNode(SkillNode.RANGE,10 ,10);
        super.settings = skillSettings;
    }

    @Override
    public SkillResult cast(IActiveCharacter iActiveCharacter, ExtendedSkillInfo extendedSkillInfo) {
        int range = (int) settings.getLevelNodeValue(SkillNode.RANGE,extendedSkillInfo.getLevel());
        Living l = Utils.getTargettedEntity(iActiveCharacter,range);
        if (l == null)
            return SkillResult.NO_TARGET;
        IEntity e = entityService.get(l);
        if (e != null) {
            float damage = settings.getLevelNodeValue(SkillNode.DAMAGE,extendedSkillInfo.getLevel());
            SkillDamageSourceBuilder build = new SkillDamageSourceBuilder();
            build.setSkill(this);
            build.setCaster(iActiveCharacter);
            build.type(getDamageType());
            l.damage(damage,build.build());
            l.getLocation().getExtent().createEntity(EntityTypes.LIGHTNING, l.getLocation().getPosition());

        }
        return SkillResult.OK;
    }


}
