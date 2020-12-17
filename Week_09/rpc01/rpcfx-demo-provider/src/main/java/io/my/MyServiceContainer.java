package io.my;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyServiceContainer {

    private static Map<String, Object> map = new HashMap<>();

    public static Map<String, Object> getMap(){
        return map;
    }

    static {
        try {
            Set<Class> set = ScanUtils.getAnnotationClasses("io.kimmking.rpcfx.demo.provider", MyService.class);
            for (Class c : set) {
                Class s = c;
                Class[] classes = s.getInterfaces();
                for (Class klass : classes) {
                    map.put(klass.getName(), c.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
