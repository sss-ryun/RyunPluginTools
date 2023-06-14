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

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Implements the {@link Listener} interface to shutdown the server when the last player quits
 * @author <a href="https://github.com/sss-ryun">SSS Ryun</a>
 */
public class ServerShutdownOnQuit implements Listener {

    /**
     * Shutdown the server when there are no online players after the last player quits.
     * @param event {@link PlayerQuitEvent} from the {@link EventHandler}
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(@NotNull PlayerQuitEvent event){
        //The online player count is updated only after this event, so we check for <1 instead
        if(Bukkit.getServer().getOnlinePlayers().size() <= 1)
            Bukkit.getServer().shutdown();
    }
}
