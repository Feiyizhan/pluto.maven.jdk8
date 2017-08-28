package com.pluto.maven.jdk8.chat2;

import java.io.File;

/**
 * @author Pluto Xu a4yl9zz
 *
 */
public class SysDep {
    final static String UNIX_NULL_DEV="/dev/null";
    final static String WINDOWS_NULL_DEV="NUL:";
    final static String FAKE_NULL_DEV="jnk";
    
    /**
     * 在支持它的平台上返回名称“空设备”，或者jnk（以创建一个命令良好的临时文件）
     * @author Pluto Xu a4yl9zz
     * @return
     */
    public static String getDevNull() {
        if(new File(UNIX_NULL_DEV).exists()) {
            return UNIX_NULL_DEV;
        }
        
        String sys = System.getProperty("os.name");
        if(sys==null) {
            return FAKE_NULL_DEV;
        }
        
        if(sys.startsWith("Windows")) {
            return WINDOWS_NULL_DEV;
        }
        return FAKE_NULL_DEV;
    }   
    
    /**
     * @author Pluto Xu a4yl9zz
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getDevNull());
        boolean isMacOS = System.getProperty("mrj.version")!=null;
        System.out.println(isMacOS);
        
    }

}
