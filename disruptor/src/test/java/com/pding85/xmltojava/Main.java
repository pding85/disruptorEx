package com.pding85.xmltojava;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources";
        System.out.println(path);
        String jarPath = path + "\\trang.jar" ;

        String xmlPath = args[0];
        String xsdPath = args[1];

        launchProcess("  java -jar " + jarPath + " " + xmlPath + " " + xsdPath,
                new HashMap<>(), false);

        String javaCmd = "xjc -d ./ -encoding utf-8 " + xsdPath;
        launchProcess(javaCmd,
                new HashMap<>(), false);

    }

    public static String launchProcess(final String command,
                                       final Map<String, String> environment, boolean backend) throws IOException {
        String[] cmds = command.split(" ");

        ArrayList<String> cmdList = new ArrayList<>();
        for (String tok : cmds) {
            if (!StringUtils.isBlank(tok)) {
                cmdList.add(tok);
            }
        }

        return launchProcess(command, cmdList, environment, backend);
    }


    public static String launchProcess( final String cmd, final List<String> cmdlist,
                                       final Map<String, String> environment, boolean backend) throws IOException {
        try {
        Process process = launchProcess(cmdlist, environment);
        StringBuilder sb = new StringBuilder();
        String output = getOutput(process.getInputStream());
        String errorOutput = getOutput(process.getErrorStream());
        sb.append(output);
        sb.append("\n");
        sb.append(errorOutput);

        int ret = process.waitFor();
        if (ret != 0) {
            LOG.warn(cmd + " is terminated abnormally. ret={}, str={}", ret, sb.toString());
        }
        LOG.debug("command {}, ret {}, str={} :", cmd, ret, sb.toString());
        return sb.toString();
        } catch (Throwable e) {
            LOG.error("Failed to run " + cmd + ", " + e.getCause(), e);
        }

        return "";
    }

    protected static java.lang.Process launchProcess(final List<String> cmdlist,
                                                     final Map<String, String> environment) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(cmdlist);
        builder.redirectErrorStream(true);
        Map<String, String> process_evn = builder.environment();
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            process_evn.put(entry.getKey(), entry.getValue());
        }

        return builder.start();
    }
    public static String getOutput(InputStream input) {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
