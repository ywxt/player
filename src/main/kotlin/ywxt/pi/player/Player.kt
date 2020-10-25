package ywxt.pi.player

import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.media.TrackType
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy
import java.io.File
import java.io.FileFilter
import java.nio.file.DirectoryStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.JFrame

class Player(windowTitle: String, path: String) : AutoCloseable {
    private val frame = JFrame(windowTitle)
    private val media = EmbeddedMediaPlayerComponent()
    private val volumeToast = Toast(frame)
    private val titleToast = Toast(frame, Toast.LONG)
    private val config: ConfigFile = ConfigFile.load()
    private val playingList: List<Video> = loadDirectory(path)
    private var currentIndex: Int
    private var currentPosition: Float

    private val started = false
    private val saveConfigThread = Thread {
        config.save()
        Thread.sleep(2000)
    }


    var volume: Int
        get() = media.mediaPlayer().audio().volume()
        set(value) {
            media.mediaPlayer().audio().setVolume(value)
        }

    init {
        frame.contentPane = media
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val fullScreen = media.mediaPlayer().fullScreen()
        fullScreen.strategy(AdaptiveFullScreenStrategy(frame))
        currentIndex = playingList.indexOfFirst { it.path == config.currentItem }
        if (currentIndex == -1) {
            currentIndex = 0
            currentPosition = 0f
        } else {
            currentPosition = config.currentPosition
        }
    }

    fun show() {
        frame.isVisible = true
        media.mediaPlayer().fullScreen().set(true)
        if (started) {
            media.mediaPlayer().events().addMediaPlayerEventListener(object : MediaPlayerEventListener {
                override fun mediaChanged(mediaPlayer: MediaPlayer?, media: MediaRef?) {
                }

                override fun opening(mediaPlayer: MediaPlayer?) {
                }

                override fun buffering(mediaPlayer: MediaPlayer?, newCache: Float) {
                }

                override fun playing(mediaPlayer: MediaPlayer?) {
                }

                override fun paused(mediaPlayer: MediaPlayer?) {
                }

                override fun stopped(mediaPlayer: MediaPlayer?) {
                }

                override fun forward(mediaPlayer: MediaPlayer?) {
                }

                override fun backward(mediaPlayer: MediaPlayer?) {
                }

                override fun finished(mediaPlayer: MediaPlayer?) {
                    this@Player.playNext()
                }

                override fun timeChanged(mediaPlayer: MediaPlayer?, newTime: Long) {
                }

                override fun positionChanged(mediaPlayer: MediaPlayer?, newPosition: Float) {
                    config.currentPosition = newPosition
                }

                override fun seekableChanged(mediaPlayer: MediaPlayer?, newSeekable: Int) {
                }

                override fun pausableChanged(mediaPlayer: MediaPlayer?, newPausable: Int) {
                }

                override fun titleChanged(mediaPlayer: MediaPlayer?, newTitle: Int) {
                }

                override fun snapshotTaken(mediaPlayer: MediaPlayer?, filename: String?) {
                }

                override fun lengthChanged(mediaPlayer: MediaPlayer?, newLength: Long) {
                }

                override fun videoOutput(mediaPlayer: MediaPlayer?, newCount: Int) {
                }

                override fun scrambledChanged(mediaPlayer: MediaPlayer?, newScrambled: Int) {
                }

                override fun elementaryStreamAdded(mediaPlayer: MediaPlayer?, type: TrackType?, id: Int) {
                }

                override fun elementaryStreamDeleted(mediaPlayer: MediaPlayer?, type: TrackType?, id: Int) {
                }

                override fun elementaryStreamSelected(mediaPlayer: MediaPlayer?, type: TrackType?, id: Int) {
                }

                override fun corked(mediaPlayer: MediaPlayer?, corked: Boolean) {
                }

                override fun muted(mediaPlayer: MediaPlayer?, muted: Boolean) {
                }

                override fun volumeChanged(mediaPlayer: MediaPlayer?, volume: Float) {
                    config.volume = volume.toInt()
                }

                override fun audioDeviceChanged(mediaPlayer: MediaPlayer?, audioDevice: String?) {
                }

                override fun chapterChanged(mediaPlayer: MediaPlayer?, newChapter: Int) {
                }

                override fun error(mediaPlayer: MediaPlayer?) {
                }

                override fun mediaPlayerReady(mediaPlayer: MediaPlayer?) {

                }

            })
            saveConfigThread.start()
        }
        volume = config.volume
        play(playingList[currentIndex], currentPosition)

    }

    fun playNext() {
        if (currentIndex >= playingList.lastIndex) {
            currentIndex = 0
        } else {
            currentIndex++
        }
        play(playingList[currentIndex])
    }

    fun playPrevious() {
        if (currentIndex <= 0) {
            currentIndex = playingList.lastIndex
        } else {
            currentIndex--
        }
        play(playingList[currentIndex])
    }

    private fun play(video: Video, position: Float = 0f) {
        media.mediaPlayer().media().play(video.path)
        if (position != 0f) {
            media.mediaPlayer().controls().setPosition(position)
        }
        config.currentItem = video.path
        titleToast.setText(video.title)
        titleToast.display()
    }

    fun volumeUp(step: Int = 5) {
        volume += step
        volumeToast.setText("音量：$volume")
        volumeToast.display()
    }

    fun volumeDown(step: Int = 5) {
        volume -= step
        volumeToast.setText("音量：$volume")
        volumeToast.display()
    }

    /**
     * 播放时暂停，暂停时播放
     */
    fun resume() {
        if (media.mediaPlayer().status().isPlayable) {
            media.mediaPlayer().controls().setPause(media.mediaPlayer().status().isPlaying)
        }
    }

    override fun close() {
        media.release()
        volumeToast.dispose()
        titleToast.dispose()
        frame.dispose()
    }

    private fun loadDirectory(path: String): List<Video> {
        val filter = DirectoryStream.Filter<Path> {
            Files.isDirectory(it)
        }
        val dir = Paths.get(path)
        return Files.newDirectoryStream(dir, filter).use { stream ->
            stream.asSequence().flatMap { loadVideoList(it.toAbsolutePath().toString()) }.toList()
        }

    }

    private fun loadVideoList(path: String): List<Video> {
        val files = File(path).listFiles(FileFilter {
            it.isFile && it.extension == "mp4"
        })
        return if (!files.isNullOrEmpty()) {
            files.asIterable().map {
                Video(it.nameWithoutExtension, it.path)
            }

        } else {
            listOf()
        }
    }

}