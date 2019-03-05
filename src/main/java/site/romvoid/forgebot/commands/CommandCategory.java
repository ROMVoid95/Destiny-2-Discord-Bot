/*
 * Copyright 2019 github.com/ROMVoid95
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package site.romvoid.forgebot.commands;

import site.romvoid.forgebot.util.RankSystem;

/**
 * Enum container declaring Catagories for commands
 * 
 * @author ROMVoid
 */
public enum CommandCategory {
    CREATOR("creator", "Development", RankSystem.CREATOR),
    BOT_ADMINISTRATION("bot_administration", "Bot administration", RankSystem.BOT_ADMIN),
    ADMINISTRATIVE("administrative", "Administration", RankSystem.GUILD_ADMIN),
    INFORMATIVE("informative", "Information"),
    DESTINY("destiny", "Destiny"),
    UNKNOWN("nopackage", "Misc");
    private final String packageName;
    private final String displayName;
    private final RankSystem rankRequired;

    
    /**
     * Commands any discord users can call 
     * 
     * @param packageName
     * @param displayName
     */
    CommandCategory(String packageName, String displayName) {

        this.packageName = packageName;
        this.displayName = displayName;
        this.rankRequired = RankSystem.USER;
    }

    /**
     * Commands reserved for Guild Admins, Guild Owners and Bot Creator
     * 
     * @param packageName
     * @param displayName
     * @param rankRequired
     */
    CommandCategory(String packageName, String displayName, RankSystem rankRequired) {

        this.packageName = packageName;
        this.displayName = displayName;
        this.rankRequired = rankRequired;
    }

    /**
     * Information based commands
     * 
     * @param rank
     * @return
     */
    public static CommandCategory getFirstWithPermission(RankSystem rank) {
        if (rank == null) {
            return INFORMATIVE;
        }
        for (CommandCategory category : values()) {
            if (rank.isAtLeast(category.getRankRequired())) {
                return category;
            }
        }
        return INFORMATIVE;
    }

    /**
     * Unknown Command Category 
     * 
     * @param packageName
     * @return
     */
    public static CommandCategory fromPackage(String packageName) {
        if (packageName != null) {
            for (CommandCategory cc : values()) {
                if (packageName.equalsIgnoreCase(cc.packageName)) {
                    return cc;
                }
            }
        }
        return UNKNOWN;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPackageName() {
        return packageName;
    }

    public RankSystem getRankRequired() {
        return rankRequired;
    }
}