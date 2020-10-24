package ywxt.pi.player

import java.awt.Color
import java.awt.Font
import java.awt.GridBagLayout
import java.awt.Point
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.geom.RoundRectangle2D
import javax.swing.*

/**
 * See also: https://github.com/vincenzopalazzo/android-toasts-for-swing
 * @author https://github.com/vincenzopalazzo
 */
class Toast(private val mOwner: JFrame, val duration: Int = SHORT) : JDialog(mOwner) {
    private var mText: String? = null
    private var timer: Timer? = null
    private var label: JLabel = JLabel()
    private var mPosition = 0
    private var mBackgroundColor = Color.DARK_GRAY
    private var mForegroundColor = Color.WHITE
    private val mFont = (UIManager.get("ToolTip.font") as Font).deriveFont(60.0f)


    private val toastLocation: Point
        get() {
            val ownerLoc = mOwner.location
            if (mPosition == TOP) {
                val x = (ownerLoc.getX() + (mOwner.width - width) / 2).toInt()
                val y = (ownerLoc.getY() + 30).toInt()
                return Point(x, y)
            }
            val x = (ownerLoc.getX() + (mOwner.width - width) / 2).toInt()
            val y = (ownerLoc.getY() + mOwner.height / 2).toInt()
            return Point(x, y)
        }

    fun setText(text: String?) {
        mText = text
        label.text = text
        setSize(mText!!.length * CHARACTER_LENGTH_MULTIPLIER, 150)
    }

    override fun setForeground(foregroundColor: Color) {
        mForegroundColor = foregroundColor
    }

    fun display() {
        if (isVisible) {
            timer?.restart()
            return
        }

        try {
            location = toastLocation
            isVisible = true
            timer = Timer(duration) {
                isVisible = false
            }.apply {
                isRepeats = false
                start()
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    companion object {
        private const val WINDOW_RADIUS = 15
        private const val CHARACTER_LENGTH_MULTIPLIER = 65

        /**
         * The Constant SHORT Set the shortest amount of time to display the toast
         */
        const val SHORT = 1500

        /**
         * The Constant SHORT Set the longest time to display the toast
         */
        const val LONG = 6000

        /**
         * The Constant ERROR Set the color red to display the toast
         */
        val ERROR = Color.RED!!

        /**
         * The Constant SUCCESS Set the color green to display the toast
         */
        val SUCCESS = Color.GREEN!!

        /**
         * The Constant NORMAL Set the color GRAY to display the toast
         */
        val NORMAL = Color.DARK_GRAY!!

        /**
         * The Constant DOWN Set the position toast on the top
         */
        const val TOP = 1

        /**
         * The Constant DOWN Set the position toast on the bottom
         */
        const val BOTTOM = 0

    }


    init {
        layout = GridBagLayout()
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent) {
                shape = RoundRectangle2D.Double(
                    0.0,
                    0.0,
                    width.toDouble(),
                    height.toDouble(),
                    WINDOW_RADIUS.toDouble(),
                    WINDOW_RADIUS.toDouble()
                )
            }
        })
        getRootPane().windowDecorationStyle = JRootPane.NONE
        isAlwaysOnTop = true
        isUndecorated = true
        focusableWindowState = false
        modalityType = ModalityType.MODELESS
        contentPane.background = mBackgroundColor
        label.background = mBackgroundColor
        label.foreground = mForegroundColor
        label.font = mFont
        add(label)
    }
}