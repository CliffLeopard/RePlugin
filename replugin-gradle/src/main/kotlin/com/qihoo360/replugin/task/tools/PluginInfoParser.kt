package com.qihoo360.replugin.task.tools

import com.qihoo360.replugin.host.HostExtension
import net.dongliu.apk.parser.ApkFile
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import javax.xml.parsers.SAXParserFactory

/**
 * 从manifest的xml中抽取PluginInfo信息
 *
 * @author RePlugin Team
 */
class PluginInfoParser(pluginFile: File, extension: HostExtension) : DefaultHandler() {

    private val androidName = "android:name"
    private val androidValue = "android:value"
    private val tagName = "com.qihoo360.plugin.name"
    private val tagVersionLow = "com.qihoo360.plugin.version.low"
    private val tagVersionHigh = "com.qihoo360.plugin.version.high"
    private val tagVersionVer = "com.qihoo360.plugin.version.ver"
    private val tagFrameWorkVer = "com.qihoo360.framework.ver"
    private val pluginInfo: PluginInfo

    init {
        val apkFile = ApkFile(pluginFile)
        val manifestXmlStr = apkFile.manifestXml
        val inputStream = manifestXmlStr.byteInputStream()
        val parser = SAXParserFactory.newInstance().newSAXParser()
        pluginInfo = PluginInfo()
        parser.parse(inputStream, this)

        val fullName = pluginFile.name
        pluginInfo.path = extension.pluginDir + '/' + fullName

        val postfix: String = extension.pluginFilePostfix
        pluginInfo.name = fullName.substring(0, fullName.length - postfix.length)
    }


    fun getPluginInfo(): PluginInfo {
        return pluginInfo
    }

    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        if ("meta-data" == qName) {
            when (attributes.getValue(androidName)) {
                tagName -> pluginInfo.name = attributes.getValue(androidValue)
                tagVersionLow -> pluginInfo.low = attributes.getValue(androidValue).toLong()
                tagVersionHigh -> pluginInfo.height = attributes.getValue(androidValue).toLong()
                tagVersionVer -> pluginInfo.ver = attributes.getValue(androidValue).toLong()
                tagFrameWorkVer -> pluginInfo.frm = attributes.getValue(androidValue).toLong()
            }

        } else if ("manifest" == qName) {
            pluginInfo.pkg = attributes.getValue("package")
            pluginInfo.ver = attributes.getValue("android:versionCode").toLong()
        }
    }
}