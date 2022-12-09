package com.qihoo360.loader2;

import com.qihoo360.replugin.helper.LogDebug;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * author:gaoguanling
 * date:2022/6/9
 * time:17:26
 * email:gaoguanling@360.cn
 * link:
 */
public class ShellExecutor {
    private static final String TAG = "ShellExecutor";

    public static String execute(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            StringBuilder result = new StringBuilder();
            try {
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
                return result.toString();
            } catch (IOException ignore) {
                LogDebug.e(TAG, "Error shell:" + cmd);
            } finally {
                writer.close();
                reader.close();
            }
        } catch (IOException ignore) {
            LogDebug.e(TAG, "Error shell:" + cmd);
        }
        return "";
    }
}
