package kim.azure.kether.hook

import ink.ptms.um.event.MobSkillLoadEvent
import ink.ptms.um.skill.SkillConfig
import ink.ptms.um.skill.SkillMeta
import ink.ptms.um.skill.SkillResult
import ink.ptms.um.skill.type.EntityTargetSkill
import ink.ptms.um.skill.type.LocationTargetSkill
import io.rokuko.azureflow.api.AzureFlowAPI
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.cint


@SubscribeEvent
fun azureDropSkill(e: MobSkillLoadEvent) {
    if (e.nameIs("akl-drop")) {
        e.register(AzureDropSkill(e.config))
    }
}

class AzureDropSkill(config: SkillConfig) : LocationTargetSkill, EntityTargetSkill {
    private val item = config.getPlaceholderString(arrayOf("i", "item"), "")
    private val amount = config.getPlaceholderString(arrayOf("a", "amount"), "")

    override fun cast(meta: SkillMeta, location: Location): SkillResult {

        val player = meta.caster.entity as? Player ?: return SkillResult.ERROR

        val factory = AzureFlowAPI.getFactory(item.get(meta.caster))
        val stack = factory?.build()?.virtualItemStack(player) ?: return SkillResult.ERROR
        repeat(amount.get(meta.caster).cint) {
            location.world?.dropItem(location, stack)
        }
        return SkillResult.SUCCESS
    }

    override fun cast(meta: SkillMeta, entity: Entity): SkillResult {
        val location = entity.location

        val player = meta.caster.entity as? Player ?: return SkillResult.ERROR

        val factory = AzureFlowAPI.getFactory(item.get(meta.caster))
        val stack = factory?.build()?.virtualItemStack(player) ?: return SkillResult.ERROR
        repeat(amount.get(meta.caster).cint) {
            location.world?.dropItem(location, stack)
        }
        return SkillResult.SUCCESS
    }

}