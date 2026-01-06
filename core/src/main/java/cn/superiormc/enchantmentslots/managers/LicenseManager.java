package cn.superiormc.enchantmentslots.managers;

import cn.superiormc.enchantmentslots.utils.TextUtil;
import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class LicenseManager {

    public static LicenseManager licenseManager;

    public static String getUser = "%%__USER__%%";

    public static String getUserName = "%%__USERNAME__%%";

    public LicenseManager() {
        licenseManager = this;
        initLicense();
    }

    private void initLicense() {
        if (getUserName.equals("%%__USERNAME__%%")) {
            if (getUser.equals("%%__USER__%%")) {
                return;
            }
            String url = "https://api.spigotmc.org/simple/0.2/index.php?action=getAuthor&id=" + getUser;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    String jsonString = response.toString();
                    String username = "";
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        username = jsonObject.getString("username");
                    } catch (JSONException ignored) {
                    }
                    getUserName = username;
                }
                connection.disconnect();
            } catch (IOException ignored) {
            }
            if (getUser.equals("%%__USER__%%")) {
                return;
            }
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    String jsonString = response.toString();
                    String username = "";
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        username = jsonObject.getString("username");
                    } catch (JSONException ignored) {
                    }
                    getUserName = username;
                }
                connection.disconnect();
            } catch (IOException ignored) {
            }
        }
        if (!getUserName.isEmpty() && !getUserName.contains("%")) {
            TextUtil.sendMessage(null, TextUtil.pluginPrefix() + " §fLicense to: " + getUserName + ".");
        }
    }

    public boolean checkJarFiles() {
        if (getUser.equals("123321")) {
            return false;
        }
        try {
            boolean txtFileFound = false;
            String jarFilePath = LicenseManager.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            JarFile jarFile = new JarFile(jarFilePath);
            String comment = jarFile.getComment();
            if (comment != null && !comment.isEmpty()) {
                if (comment.toUpperCase().contains("LEAK") ||
                        comment.toUpperCase().contains("BLACK") ||
                        comment.toUpperCase().contains("NULLED") ||
                        comment.toUpperCase().contains("RESOURCE")) {
                    return false;
                }
            }
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFilePath));
            JarEntry entry;
            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                // 获取条目的名称
                String entryName = entry.getName();
                // 判断是否为第一层的文件（不包含文件夹）
                if (!entryName.contains("/") && entryName.toLowerCase().endsWith(".txt")) {
                    txtFileFound = true;
                    break;
                }
            }
            jarInputStream.close();
            return !txtFileFound;
        } catch (Exception e) {
            return true;
        }
    }
}
