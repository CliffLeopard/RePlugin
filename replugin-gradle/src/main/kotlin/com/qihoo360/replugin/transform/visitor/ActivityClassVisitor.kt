package com.qihoo360.replugin.transform.visitor

import org.objectweb.asm.ClassVisitor

/**
 * author:gaoguanling
 * date:2021/9/8
 * time:10:01
 * email:gaoguanling@360.cn
 * link:
 */
class ActivityClassVisitor(cv: ClassVisitor, context: InstrumentationContext) :
    PluginClassVisitor(cv, context) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name
        super.visit(version, access, name, signature, getSuperName(name, superName), interfaces)
    }

    private fun getSuperName(name: String?, superName: String?): String? {
        if (name.isNullOrEmpty() || superName.isNullOrEmpty())
            return superName

        if (activities.containsKey(superName) && name != activities[superName]) {
            context.classModified = true
            return activities[superName]
        }

        return superName
    }

    companion object {
        private val activities = hashMapOf(
            "android/app/Activity" to "com/qihoo360/replugin/loader/a/PluginActivity",
            "android/app/TabActivity" to "com/qihoo360/replugin/loader/a/PluginTabActivity",
            "android/app/ListActivity" to "com/qihoo360/replugin/loader/a/PluginListActivity",
            "android/app/ActivityGroup" to "com/qihoo360/replugin/loader/a/PluginActivityGroup",
            "android/support/v4/app/FragmentActivity" to "com/qihoo360/replugin/loader/a/PluginFragmentActivity",
            "android/support/v7/app/AppCompatActivity" to "com/qihoo360/replugin/loader/a/PluginAppCompatActivity",
            "android/preference/PreferenceActivity" to "com/qihoo360/replugin/loader/a/PluginPreferenceActivity",
            "android/app/ExpandableListActivity" to "com/qihoo360/replugin/loader/a/PluginExpandableListActivity"
        )
    }
}