package ywxt.pi.player

import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
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

    var volume: Int
        get() = media.mediaPlayer().audio().volume()
        set(value) {
            media.mediaPlayer().audio().setVolume(value)
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