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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A call list for all of the {@link TabExecutor} in the {@link org.bukkit.plugin.java.JavaPlugin}
 * @author <a href="https://github.com/sss-ryun">SSS Ryun</a>
 */
public class PluginCommands extends ArrayList<TabExecutor> {
    /**
     * Calls each {@link TabExecutor#onCommand(CommandSender, Command, String, String[])} by order and stops when one returns true, effectively prioritizing by order
     * @param sender {@link CommandSender}
     * @param command {@link Command}
     * @param label {@link String}
     * @param args {@link String[]}
     * @return If a command was executed
     */
    public boolean call(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean result = false;

        for (TabExecutor executor: this) {
            result = executor.onCommand(sender, command, label, args);

            if(result)
                break;
        }

        return result;
    }

    /**
     * Calls each {@link TabExecutor#onCommand(CommandSender, Command, String, String[])} by order without stopping
     * @param sender {@link CommandSender}
     * @param command {@link Command}
     * @param label {@link String}
     * @param args {@link String[]}
     * @return If a command was executed
     */
    public boolean callAll(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean result = false;

        for (TabExecutor executor: this) {
            result |= executor.onCommand(sender, command, label, args);
        }

        return result;
    }

    /**
     * Calls each {@link TabExecutor#onTabComplete(CommandSender, Command, String, String[])} by order and stops when one returns values, effectively prioritizing by order
     * @param sender {@link CommandSender}
     * @param command {@link Command}
     * @param alias {@link String}
     * @param args {@link String[]}
     * @return
     */
    public @Nullable List<String> completeTab(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> results = new ArrayList<>();

        for (TabExecutor executor: this) {
            List<String> result = executor.onTabComplete(sender, command, alias, args);
            if(result != null)
                results.addAll(result);

            if(results.size() > 0)
                break;
        }

        return results;
    }

    /**
     * Calls each {@link TabExecutor#onTabComplete(CommandSender, Command, String, String[])} by order without stopping
     * @param sender {@link CommandSender}
     * @param command {@link Command}
     * @param alias {@link String}
     * @param args {@link String[]}
     * @return
     */
    public @Nullable List<String> completeTabAll(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> results = new ArrayList<>();

        for (TabExecutor executor: this) {
            List<String> result = executor.onTabComplete(sender, command, alias, args);
            if(result != null)
                results.addAll(result);
        }

        return results;
    }
}
