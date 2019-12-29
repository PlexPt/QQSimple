package me.zpp0196.qqpurify.pro;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ProguardMapping {

    public ProguardMapping() {
    }

    public ProguardMapping(long version) {
        this.version = version;
    }

    @SerializedName("version")
    public long version;
    @SerializedName("classes")
    public Map<String, ClassMapping> classes;

    public static class ClassMapping {

        public ClassMapping() {
        }

        public ClassMapping(String clazz) {
            this.clazz = clazz;
        }

        @SerializedName("clazz")
        public String clazz;
        @SerializedName("fields")
        public Map<String, String> fields;
        @SerializedName("methods")
        public Map<String, String> methods;
    }
}
