package cz.neumimto.skills;

import cz.neumimto.ResourceLoader;
import cz.neumimto.core.ioc.Inject;
import cz.neumimto.effects.EffectService;
import cz.neumimto.effects.common.positive.Invisibility;
import cz.neumimto.players.IActiveCharacter;

/**
 * Created by NeumimTo on 23.12.2015.
 */
@ResourceLoader.Skill
@ResourceLoader.ListenerClass
public class SkillInvisibility extends ActiveSkill {

    @Inject
    private EffectService effectService;

    public SkillInvisibility() {
        setName("Invisibility");
        setDamageType(null);
        SkillSettings settings = new SkillSettings();
        settings.addNode(SkillNode.DURATION, 10, 10);
        setSettings(settings);
        getSkillTypes().add(SkillType.CANT_CAST_WHILE_SILENCED);

    }

    @Override
    public SkillResult cast(IActiveCharacter character, ExtendedSkillInfo info) {
        long duration = (long) settings.getLevelNodeValue(SkillNode.DURATION,info.getLevel());
        Invisibility invisibility = new Invisibility(character, duration);
        effectService.addEffect(invisibility,character);
        return SkillResult.OK;
    }


}
