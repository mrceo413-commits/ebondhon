package com.ebondhon.utils;

import android.content.Context;

import com.ebondhon.models.AppData;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CacheManager {

    private static final String FILE_NAME = "app_data.json";

    public static void saveData(Context ctx, String json) {
        try {
            File file = new File(ctx.getFilesDir(), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
            writer.write(json);
            writer.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadData(Context ctx) {
        try {
            File file = new File(ctx.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AppData parseData(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return new Gson().fromJson(json, AppData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasCachedData(Context ctx) {
        File file = new File(ctx.getFilesDir(), FILE_NAME);
        return file.exists() && file.length() > 0;
    }
}
