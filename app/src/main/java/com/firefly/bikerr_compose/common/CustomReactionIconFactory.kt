package com.firefly.bikerr_compose.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.firefly.bikerr_compose.R
import io.getstream.chat.android.compose.ui.util.ReactionDrawable
import io.getstream.chat.android.compose.ui.util.ReactionIcon
import io.getstream.chat.android.compose.ui.util.ReactionIconFactory

class CustomReactionIconFactory(
    private val customReactions: Map<String, ReactionDrawable> = mapOf(
        "happy" to ReactionDrawable(
            iconResId = R.drawable.grin,
            selectedIconResId = R.drawable.grin
        ),
        "cry" to ReactionDrawable(
            iconResId = R.drawable.cry,
            selectedIconResId = R.drawable.cry
        ),
        "worry" to ReactionDrawable(
            iconResId = R.drawable.worry,
            selectedIconResId = R.drawable.worry
        ),
        "thumbsup" to ReactionDrawable(
            iconResId = R.drawable.thumbsup,
            selectedIconResId = R.drawable.thumbsup
        ),
        "heart" to ReactionDrawable(
            iconResId = R.drawable.twohearts,
            selectedIconResId = R.drawable.twohearts
        )
    )
) : ReactionIconFactory {
    @Composable
    override fun createReactionIcon(type: String): ReactionIcon {
        val reactionDrawable = requireNotNull(customReactions[type])
        return ReactionIcon(
            painter = painterResource(reactionDrawable.iconResId),
            selectedPainter = painterResource(reactionDrawable.selectedIconResId)
        )
    }

    @Composable
    override fun createReactionIcons(): Map<String, ReactionIcon> {
        return customReactions.mapValues {
            createReactionIcon(it.key)
        }
    }

    override fun isReactionSupported(type: String): Boolean {
        return customReactions.containsKey(type)
    }
}