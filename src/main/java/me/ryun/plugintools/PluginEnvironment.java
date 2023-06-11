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

import java.util.Objects;

/**
 * The class for handling the development or production/release environment for the Plugin
 * @author <a href="https://github.com/sss-ryun">SSS Ryun</a>
 */
public class PluginEnvironment {
    private static PluginEnvironment self = null;

    private final boolean isDevelopment;
    private final boolean isVerbose;

    /**
     * A private instance method to avoid the clutter when selecting the methods for each variable you need to use
     * @param isDevelopment Whether the plugin is essentially in development(true) or production(false)
     * @param isVerbose Whether the plugin outputs Verbose logs in the Console
     */
    private PluginEnvironment(boolean isDevelopment, boolean isVerbose) {
        this.isDevelopment = isDevelopment;
        this.isVerbose = isVerbose;
    }

    /**
     * This is kind-of singleton but not at the same time since you can change the instance with the {@link Factory} class
     * @param isDevelopment Whether the plugin is essentially in development(true) or production/release(false)
     * @param isVerbose Whether the plugin outputs Verbose logs in the Console
     * @return The new instance of this class
     */
    private static PluginEnvironment instantiate(boolean isDevelopment, boolean isVerbose) {
        self = new PluginEnvironment(isDevelopment, isVerbose);

        System.gc(); //Throw away the last instance. Probably not needed

        return self;
    }

    /**
     * Get the instance of this class or fail-fast if it's not instantiated beforehand
     * @return The latest instance of this class
     */
    public static PluginEnvironment get() {
        //Fail-fast
        Objects.requireNonNull(self, "Plugin Environment not instantiated!");

        return self;
    }

    /**
     * Is this plugin in development(true) or production/release(false)?
     * @return boolean
     */
    public boolean isDevelopment() {
        return isDevelopment;
    }

    /**
     * Is this plugin logging everything or not?
     * @return boolean
     */
    public boolean isVerbose() {
        return isVerbose;
    }

    /**
     * The Factory for setting the Plugin Environment
     */
    public static class Factory {
        private boolean isDevelopment;
        private boolean isVerbose;

        /**
         * Create a new {@link Factory} instance that can later be created using {@link Factory#create()} once done setting the appropriate variables
         */
        private Factory() {
            isDevelopment = true;
            isVerbose = false;
        }

        /**
         * Make a new {@link Factory} instance
         * @return {@link Factory}
         */
        public static Factory make() {
            return new Factory();
        }

        /**
         * Sets the isDevelopment value
         * @param value boolean
         * @return The same instance
         */
        public Factory isDevelopment(boolean value) {
            this.isDevelopment = value;

            return this;
        }

        /**
         * Sets the isVerbose value
         * @param value boolean
         * @return The same instance
         */
        public Factory isVerbose(boolean value) {
            this.isVerbose = value;

            return this;
        }

        /**
         * Creates and sets the new {@link PluginEnvironment} instance
         * @return {@link PluginEnvironment}
         */
        public PluginEnvironment create() {
            return PluginEnvironment.instantiate(this.isDevelopment, this.isVerbose);
        }
    }
}
