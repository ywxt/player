package ywxt.pi.player

import com.google.gson.GsonBuilder
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

data class Video(
    val title: String,
    val path: String,
)

data class ConfigFile(
    /**
     * 当前播放路径
     */
    var currentItem: String,
    /**
     * 当前播放位置
     */
    var currentPosition: Float = 0f,

    var volume: Int = 60,
) {
    companion object {
        fun load(path: String = "./config.json"): ConfigFile {
            val file = File(path)
            if (!file.exists()) return ConfigFile("")
            val config = Files.readString(Path.of(path, ""))
            return GsonBuilder().create().fromJson(config)
        }
    }

    fun save(path: String = "./config.json") {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val json = gson.toJson(this)
        Files.writeString(
            Path.of(path),
            json,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE
        )
    }
}