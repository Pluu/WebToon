package buildTime.report.html

import java.io.InputStream
import java.net.URL

fun URL.openSafeStream(): InputStream {
    return openConnection().apply { useCaches = false }.getInputStream()
}

fun Any.getTextResourceContent(fileName: String): String {
    return javaClass.getResource("/$fileName")!!
        .openSafeStream()
        .bufferedReader()
        .use { it.readText() }
}


object HtmlUtils {
    /**
     * Return the text resource file content as String.
     */
    fun getTemplate(fileName: String): String {
        return getTextResourceContent("$fileName.html")
    }

}