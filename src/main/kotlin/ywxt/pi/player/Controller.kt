package ywxt.pi.player

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame

interface Controller {
    fun onShutdown(event: () -> Unit)

    fun onPlayNext(event: () -> Unit)

    fun onPlayPrevious(event: () -> Unit)

    fun onVolumeUp(event: () -> Unit)

    fun onVolumeDown(event: () -> Unit)

    fun onSwitchPlaying(event: () -> Unit)

    companion object {
        fun ofKeyboard(frame: JFrame): Controller = KeyboardController(frame)

    }

}

class KeyboardController(frame: JFrame) : Controller {

    private var playNext: (() -> Unit)? = null
    private var shutdown: (() -> Unit)? = null
    private var playPrevious: (() -> Unit)? = null
    private var volumeUp: (() -> Unit)? = null
    private var volumeDown: (() -> Unit)? = null
    private var switchPlaying: (() -> Unit)? = null

    init {
        frame.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {}

            override fun keyPressed(e: KeyEvent?) {}

            override fun keyReleased(e: KeyEvent?) {
                when (e?.keyCode) {
                    KeyEvent.VK_UP -> playPrevious?.invoke()
                    KeyEvent.VK_DOWN -> playNext?.invoke()
                    KeyEvent.VK_LEFT -> volumeDown?.invoke()
                    KeyEvent.VK_RIGHT -> volumeUp?.invoke()
                    KeyEvent.VK_ESCAPE -> shutdown?.invoke()
                    KeyEvent.VK_SPACE -> switchPlaying?.invoke()
                }
            }
        })

    }

    override fun onShutdown(event: () -> Unit) {
        shutdown = event
    }

    override fun onPlayNext(event: () -> Unit) {
        playNext = event
    }

    override fun onPlayPrevious(event: () -> Unit) {
        playPrevious = event
    }

    override fun onVolumeUp(event: () -> Unit) {
        volumeUp = event
    }

    override fun onVolumeDown(event: () -> Unit) {
        volumeDown = event
    }

    override fun onSwitchPlaying(event: () -> Unit) {
        switchPlaying = event
    }

}

class InfraredRemoteController : Controller {
    override fun onShutdown(event: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onPlayNext(event: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onPlayPrevious(event: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onVolumeUp(event: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onVolumeDown(event: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onSwitchPlaying(event: () -> Unit) {
        TODO("Not yet implemented")
    }

}