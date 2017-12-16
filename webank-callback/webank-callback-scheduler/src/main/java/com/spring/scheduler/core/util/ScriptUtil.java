package com.spring.scheduler.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import com.spring.scheduler.core.log.JobFileAppender;

/**
 * ClassName: ScriptUtil 
 * @Description: *  1、内嵌编译器如"PythonInterpreter"无法引用扩展包，因此推荐使用java调用控制台进程方式"Runtime.getRuntime().exec()"来运行脚本(shell或python)；
 *  2、因为通过java调用控制台进程方式实现，需要保证目标机器PATH路径正确配置对应编译器；
 *  3、暂时脚本执行日志只能在脚本执行结束后一次性获取，无法保证实时性；因此为确保日志实时性，可改为将脚本打印的日志存储在指定的日志文件上；
 *  4、python 异常输出优先级高于标准输出，体现在Log文件中，因此推荐通过logging方式打日志保持和异常信息一致；否则用prinf日志顺序会错乱
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class ScriptUtil {

    /**
     * make script file
     *
     * @param scriptFileName
     * @param content
     * @throws Exception
     */
    public static void markScriptFile(String scriptFileName, String content) throws Exception {
        // filePath/
        File filePathDir = new File(JobFileAppender.logPath);
        if (!filePathDir.exists()) {
            filePathDir.mkdirs();
        }

        // filePath/gluesource/
        File filePathSourceDir = new File(filePathDir, "gluesource");
        if (!filePathSourceDir.exists()) {
            filePathSourceDir.mkdirs();
        }

        // make file,   filePath/gluesource/666-123456789.py
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(scriptFileName);
            fileOutputStream.write(content.getBytes("UTF-8"));
            fileOutputStream.close();
        } catch (Exception e) {
            throw e;
        }finally{
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }
    }

    /**
     * ��־�ļ������ʽ
     *
     * �ŵ㣺֧�ֽ�Ŀ�����ʵʱ�����ָ����־�ļ���ȥ
     * ȱ�㣺
     *      ��׼����ʹ���������ȼ��̶������ܺͽű���˳��һ��
     *      Java�޷�ʵʱ��ȡ
     *
     * @param command
     * @param scriptFile
     * @param logFile
     * @param params
     * @return
     * @throws IOException
     */
    public static int execToFile(String command, String scriptFile, String logFile, String... params) throws IOException {
        // ��׼�����print ��null if watchdog timeout��
        // ���������logging + �쳣 ��still exists if watchdog timeout��
        // ��׼����
        FileOutputStream fileOutputStream = new FileOutputStream(logFile, true);
        PumpStreamHandler streamHandler = new PumpStreamHandler(fileOutputStream, fileOutputStream, null);

        // command
        CommandLine commandline = new CommandLine(command);
        commandline.addArgument(scriptFile);
        if (params!=null && params.length>0) {
            commandline.addArguments(params);
        }

        // exec
        DefaultExecutor exec = new DefaultExecutor();
        exec.setExitValues(null);
        exec.setStreamHandler(streamHandler);
        int exitValue = exec.execute(commandline);  // exit code: 0=success, 1=error
        return exitValue;
    }

}
