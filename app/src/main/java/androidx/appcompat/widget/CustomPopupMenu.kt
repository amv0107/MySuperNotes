package androidx.appcompat.widget

import android.content.Context
import android.view.View

/**
 * link: www.youtube.com/watch?v=wR6AtxwKuEM
 *
 * @author Roman Andrushchenko
 */
class CustomPopupMenu constructor (context: Context, view: View): PopupMenu(context, view){

    init {
        mPopup.setForceShowIcon(true)
    }
}