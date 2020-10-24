package ywxt.pi.player

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import javax.swing.JFrame

open class KFrame(title: String) : JFrame(title) {
    val scope:CoroutineScope = MainScope()
    override fun dispose() {
        super.dispose()
        scope.cancel()
    }
}