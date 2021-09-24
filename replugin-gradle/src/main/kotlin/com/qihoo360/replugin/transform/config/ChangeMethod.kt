package com.qihoo360.replugin.transform.config

import org.objectweb.asm.commons.Method
import java.util.HashMap

/**
 * author:gaoguanling
 * date:2021/9/14
 * time:10:33
 * email:gaoguanling@360.cn
 * link:
 *    ClassA a = new ClassA()
 *    ClassResult res = a.method1(ClassB b, classC c)
 *
 *    两种情况，方法定义的地方，方法访问的地方
 *    a.method(b,c){
 *       co.b(b,c)
 *       co.after(b,c)
 *    }
 *
 */
class ChangeMethod {
    var originMethod: Method? = null
    var methods: Map<String, Map<String, Set<Method>>> =
        HashMap<String, Map<String, Set<Method>>>()

}