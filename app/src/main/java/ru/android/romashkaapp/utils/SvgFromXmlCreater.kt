package ru.android.romashkaapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import okhttp3.ResponseBody
import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/*
 * Created by yasina on 05.12.2020
*/
class SvgFromXmlCreater {

    companion object{

        private fun parser(response: String): String{
            var svgtext = response

            //remove extra values
            svgtext = svgtext.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
            svgtext = svgtext.replace("<response>", "")
            svgtext = svgtext.replace("</response>", "")
            svgtext = svgtext.replace("&lt;", "<")
            svgtext = svgtext.replace("&gt;", ">")
            svgtext = svgtext.replace("&#13;", "")
            svgtext = svgtext.replace("<!DOCTYPE .*?>".toRegex(), "")
            svgtext = svgtext.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "")
            svgtext = svgtext.replace(
                "xmlns=\"http://www.w3.org/2000/svg id=\"svg\"\"",
                "xmlns=\"http://www.w3.org/2000/svg\""
            )
            svgtext = svgtext.replace("</svg id=\"svg\">", "</svg>")

            val m: Matcher = Pattern.compile("view_id=\".*?\"").matcher(svgtext)
            while(m.find()) {
                for (i in 0..m.groupCount()) {
                    var b = m.group(i)
                    var rp = b.replace("view_id", "id")
                    var af = "$rp onclick=\"Android.onClicked(id)\""
                    svgtext = svgtext.replace(b, af)
                }
            }

            return svgtext
        }

        fun saveHtmlToLocal(context: Context, areaId: Int, response: String): String?{
            var svg = parser(response)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                //todo show error
                return null
            } else {
                val path = context.getExternalFilesDir(null)
                val letDirectory = File(path, "sectors")
                letDirectory.mkdirs()
                val file = File(letDirectory, "Area$areaId.html")
                FileOutputStream(file).use {
                    it.write(createHtmlData(svg).encodeToByteArray())
                }
                return "file://" + file.path
            }
        }

        private fun createHtmlData(svg: String): String{
            var builder = StringBuilder()
            builder.append(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, user-scalable=yes,  maximum-scale=10.0, target-densitydpi=device-dpi\">\n" +
                        "    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />\n" +
                        "</head>\n" +
                        "<style> " +
                        ".center {\n" +
                        "}" +
                        "</style>" +
                        "<body><div class=\"center\">"
            )
            builder.append(svg)
            builder.append("</div></body>\n" + "</html>")
            return builder.toString()
        }

        open fun savePdfToTheDevice(context: Context, body: ResponseBody, orderId: Int): Boolean {
            return try {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED) {
                    //todo show error
                    return false
                }else{
                    val path = context.getExternalFilesDir(null)
                    val letDirectory = File(path, "tickets")
                    letDirectory.mkdirs()
                    val futureFile = File(letDirectory, "Order$orderId.pdf")
                    var inputStream: InputStream? = null
                    var outputStream: OutputStream? = null
                    try {
                        val fileReader = ByteArray(4096)
                        var fileSizeDownloaded: Long = 0
                        inputStream = body.byteStream()
                        outputStream = FileOutputStream(futureFile)
                        while (true) {
                            val read: Int = inputStream.read(fileReader)
                            if (read == -1) {
                                break
                            }
                            outputStream.write(fileReader, 0, read)
                            fileSizeDownloaded += read.toLong()
                        }
                        outputStream.flush()
                        true
                    } catch (e: IOException) {
                        false
                    } finally {
                        inputStream?.close()
                        outputStream?.close()
                    }
                }

            } catch (e: IOException) {
                false
            }
        }
    }

}