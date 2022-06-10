package com.qihoo360.replugin.plugin.visitor

import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.visitor.PluginClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * author:gaoguanling
 * date:2021/9/8
 * time:10:01
 * email:gaoguanling@360.cn
 * link:
 */
class ActivityClassVisitor(cv: ClassVisitor, context: InstrumentationContext) :
    PluginClassVisitor(cv, context) {

    private var originSuperClass: String? = null
    private var newSuperClass: String? = null
    private var hook = false

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name
        originSuperClass = superName
        newSuperClass = getSuperName(name, superName)
        if (originSuperClass != newSuperClass)
            hook = true
        super.visit(version, access, name, signature, newSuperClass, interfaces)
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        return if (hook)
            ActivityMethodVisitor(className, super.visitMethod(access, name, descriptor, signature, exceptions))
        else
            super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    inner class ActivityMethodVisitor(val className: String?, mv: MethodVisitor?) : MethodVisitor(Opcodes.ASM9, mv) {
        override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?, isInterface: Boolean) {
            val newOwner = if (owner == originSuperClass) newSuperClass else owner
            super.visitMethodInsn(opcode, newOwner, name, descriptor, isInterface)
        }
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