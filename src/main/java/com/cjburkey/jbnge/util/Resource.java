package com.cjburkey.jbnge.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import com.cjburkey.jbnge.Log;

public class Resource {
    
    private final String domain;
    private final String path;
    private final String full;
    private final String fullPath;
    
    public Resource(String domain, String path) {
        this.domain = (domain == null) ? "" : clean(domain);
        this.path = (path == null) ? "" : clean(path);
        this.full = (this.domain.length() > 0) ? (domain + ':' + path) : path;
        this.fullPath = "assets/" + ((this.domain.length() > 0) ? (this.domain + "/") : "") + this.path;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public String getPath() {
        return path;
    }
    
    public String toString() {
        return full;
    }
    
    public String getFullPath() {
        return fullPath;
    }
    
    public InputStream getStream() {
        try {
            return Resource.class.getResourceAsStream('/' + fullPath);
        } catch (Exception e) {
            Log.exception(e);
        }
        return null;
    }
    
    public String readFullTextFile() {
        InputStream stream;
        BufferedReader reader = null;
        try {
            stream = getStream();
            if (stream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = "";
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append('\n');
            }
            return output.toString();
        } catch (Exception e) {
            Log.exception(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                Log.exception(e);
            }
        }
        return null;
    }
    
    public static Resource build(String resource) {
        if (!resource.contains(":")) {
            return new Resource("", resource);
        }
        String[] spl = resource.split(Pattern.quote(":"));
        return new Resource(spl[0], spl[1]);
    }
    
    private static String clean(String input) {
        input = input.replaceAll(Pattern.quote("\\"), "/").trim();
        while (input.startsWith("/")) {
            input = input.substring(1);
        }
        while (input.endsWith("/")) {
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }
    
}