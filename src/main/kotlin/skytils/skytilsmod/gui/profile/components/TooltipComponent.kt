/*
 * Skytils - Hypixel Skyblock Quality of Life Mod
 * Copyright (C) 2022 Skytils
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package skytils.skytilsmod.gui.profile.components

import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UMinecraft
import gg.essential.universal.wrappers.UPlayer
import gg.essential.vigilance.gui.VigilancePalette
import net.minecraft.item.ItemStack
import skytils.skytilsmod.utils.ItemUtil
import skytils.skytilsmod.utils.stripControlCodes
import java.awt.Color

class TooltipComponent(item: ItemStack, backgroundColor: Color = VigilancePalette.getBackground(), radius: Float = 5f) :
    UIRoundedRectangle(radius) {

    val lore = item.getTooltip(UPlayer.getPlayer()!!, false)

    val itemTitle = UIRoundedRectangle(radius).constrain {
        y = 0.pixels
        height = 20.pixels
        width = (ChildBasedSizeConstraint() + 4.pixels) coerceAtLeast 100.percent
        color = ItemUtil.getRarity(item).color.withAlpha(170).constraint
    } childOf this
    val item: ItemComponent = ItemComponent(item).constrain {
        x = 2.pixels
        y = 2.pixels
        height = 16.pixels
        width = 16.pixels
    } childOf itemTitle
    val itemName: UIText = UIText(lore[0].replace("§c✪", "Ⓜ").stripControlCodes()).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = UMinecraft.getFontRenderer().getStringWidth(lore[0]).pixels
    } childOf itemTitle

    val contentContainer by UIContainer().constrain {
        y = SiblingConstraint()
        width = ChildBasedSizeConstraint() + 6.pixels
        height = ChildBasedSizeConstraint() + 6.pixels
    } childOf this
    val content = UIContainer().constrain {
        x = 3.pixels
        y = 3.pixels
        width = ChildBasedMaxSizeConstraint()
        height = ChildBasedSizeConstraint()
    } childOf contentContainer

    init {
        lore.drop(1).forEach { line ->
            if (line.stripControlCodes().isEmpty()) {
                return@forEach
            }
            addLine(line)

        }
    }

    fun addLine(text: String = "", configure: UIText.() -> Unit = {}) = apply {
        val component = UIText(text).constrain {
            y = SiblingConstraint(padding = 3f)
        } childOf content
        component.configure()
    }

    init {
        constrain {
            width = CopyConstraintFloat() boundTo contentContainer
            height = ChildBasedSizeConstraint()
            color = backgroundColor.constraint
        }
    }
}