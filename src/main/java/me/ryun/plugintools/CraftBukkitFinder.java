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

import me.ryun.plugintools.exceptions.UnsupportedCraftBukkitServerException;
import org.bukkit.Bukkit;

/**
 * A class that scans the package names for an available CraftBukkit version with backward and forward compatibility
 */
public class CraftBukkitFinder {
    /**
     * The package name before the different version names starts
     */
    public static final String baseClassPath = "org.bukkit.craftbukkit";
    /**
     * The Class to be tested to exist to check if the package name exists
     */
    public static final String testClassName = "CraftArt";
    /**
     * The minimum major version which is the first number
     */
    public static final int MAJOR_MINIMUM = 1;
    /**
     * The maximum major version which is the first number
     */
    public static final int MAJOR_MAXIMUM = 1;
    /**
     * The minimum minor version which is the second number
     */
    public static final int MINOR_MINIMUM = 9;;
    /**
     * The maximum minor version which is the second number
     */
    public static final int MINOR_MAXIMUM = 100;
    /**
     * The final number preceded by an R which is short for "Revision". Note that all Revisions starts from 1,
     * so we don't need to specify
     */
    public static final int REVISION_MAXIMUM = 10;

    private static String oldScanResult = "";

    /**
     * Scans the package names for a working classpath by testing it with a known available class
     * @param isLogging If the scan will log if it found a working version or not for each try
     * @return The package name i.e. org.bukkit.craftbukkit.v1_20_R1
     */
    public static String scan(boolean isLogging) {
        if(!oldScanResult.equals(""))
            return oldScanResult;

        for(int major = MAJOR_MINIMUM; major <= MAJOR_MAXIMUM; major++) {
            for(int minor = MINOR_MINIMUM; minor <= MINOR_MAXIMUM; minor++) {
                //Revisions always start in 1
                for(int revision = 1; revision <= REVISION_MAXIMUM; revision++) {
                    String craftBukkitVersion = "v" + major + "_" + minor + "_R" + revision;
                    try {
                        Class<?> result = Class.forName(baseClassPath + "." + craftBukkitVersion + "." + testClassName);

                        if(isLogging)
                            Bukkit.getLogger().info("Found CraftBukkit: " + craftBukkitVersion);

                        oldScanResult = result.getPackageName();

                        return oldScanResult;
                    } catch (ClassNotFoundException ignored) {
                        if(isLogging)
                            Bukkit.getLogger().warning("Tried to search for CraftBukkit: " + craftBukkitVersion + " but failed. Continuing.");
                    }
                }
            }
        }

        throw new UnsupportedCraftBukkitServerException("Your server uses a Bukkit fork with an incompatible CraftBukkit version or format. This plugin tried to search from v" + CraftBukkitFinder.MAJOR_MINIMUM + "_" + CraftBukkitFinder.MINOR_MINIMUM + "_R1" + " to v" + CraftBukkitFinder.MAJOR_MAXIMUM + "_" + CraftBukkitFinder.MINOR_MAXIMUM + "_R" + CraftBukkitFinder.REVISION_MAXIMUM + ". If you think that this is a mistake, please contact the developer.");
    }
}
