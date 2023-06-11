/*
 * Copyright (c) 2023 SSS Ryun, otherwise known as SSS_Ryun or simply Ryun
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.ryun.plugintools;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

/**
 * The class for easily retrieving the values of plugin.yml from {@link JavaPlugin}
 * @author <a href="https://github.com/sss-ryun">SSS Ryun</a>
 */
public class PluginInfo {
    /**
     * The default return value if the value cannot be found in the plugin.yml
     */
    public static final String NO_VALUE_SET = "No value set";

    private static PluginInfo self = null;

    private final Map<String, String> pluginInfo;

    /**
     * A private instance method to avoid the clutter when selecting the methods for each variable you need to use
     * @param plugin {@link JavaPlugin}
     */
    private PluginInfo(JavaPlugin plugin) {
        InputStream io = plugin.getResource("plugin.yml");

        //Fail-fast
        Objects.requireNonNull(io, "plugin.yml does not exist!");

        Yaml pluginYaml = new Yaml();

        this.pluginInfo = pluginYaml.load(io);
    }

    /**
     * This is kind-of singleton but not at the same time since you can change the instance with the {@link Factory} class
     * @param plugin The {@link JavaPlugin} where to grab the plugin.yml file
     * @return The new instance of this class
     */
    private static PluginInfo instantiate(JavaPlugin plugin) {
        self = new PluginInfo(plugin);

        return self;
    }

    /**
     * Get the instance of this class or fail-fast if it's not instantiated beforehand
     *
     * @return The latest instance of this class
     */
    public static PluginInfo get() {
        //Fail-fast
        Objects.requireNonNull(self, "Plugin Info not instantiated!");

        return self;
    }

    /**
     * Get the API Version of the {@link JavaPlugin} according to the plugin.yml
     * @return String
     */
    public String getAPIVersion() {
        return pluginInfo.getOrDefault("api-version", NO_VALUE_SET);
    }

    /**
     * Get the Author of the {@link JavaPlugin} according to the plugin.yml
     * @return String
     */
    public String getAuthor() {
        return pluginInfo.getOrDefault("author", NO_VALUE_SET);
    }

    /**
     * Get the Description of the {@link JavaPlugin} according to the plugin.yml
     * @return String
     */
    public String getDescription() {
        return pluginInfo.getOrDefault("description", NO_VALUE_SET);
    }

    /**
     * Get the Main Class of the {@link JavaPlugin} according to the plugin.yml
     * @return String
     */
    public String getMainClass() {
        return pluginInfo.getOrDefault("main", NO_VALUE_SET);
    }

    /**
     * Get the Name of the {@link JavaPlugin} according to the plugin.yml
     * @return String
     */
    public String getName() {
        return pluginInfo.getOrDefault("name", NO_VALUE_SET);
    }

    /**
     * Get the Prefix of the {@link JavaPlugin} according to the plugin.yml
     * @return String
     */
    public String getPrefix() {
        return pluginInfo.getOrDefault("prefix", NO_VALUE_SET);
    }

    /**
     * Get the Version of the {@link JavaPlugin} according to the plugin.yml
     * @return String
     */
    public String getVersion() {
        return pluginInfo.getOrDefault("version", NO_VALUE_SET);
    }

    /**
     * The Factory for setting the Plugin Info
     */
    public static class Factory {
        private JavaPlugin plugin;

        /**
         * Create a new {@link Factory} instance that can later be created using {@link Factory#create()} once done setting the appropriate variables
         */
        private Factory() {
            plugin = null;
        }

        /**
         * Make a new {@link Factory} instance
         * @return {@link Factory}
         */
        public static Factory make() {
            return new Factory();
        }

        /**
         * Sets the {@link JavaPlugin} value
         * @param value {@link JavaPlugin}
         * @return The same instance
         */
        public Factory setPlugin(@Nonnull JavaPlugin value) {
            this.plugin = value;

            return this;
        }

        /**
         * Creates and sets the new {@link PluginInfo} instance
         * @return {@link PluginInfo}
         */
        public PluginInfo create() {
            Objects.requireNonNull(plugin, "Plugin value has to be set!");

            return PluginInfo.instantiate(this.plugin);
        }
    }
}
