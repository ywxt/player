package ywxt.pi.player

import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
    val window = KFrame("hello")
    window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    window.isVisible = true
    val content = JPanel()
    content.preferredSize = Dimension(800,60)
    window.contentPane = content
    window.pack()

}