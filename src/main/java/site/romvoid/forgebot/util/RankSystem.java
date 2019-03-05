package site.romvoid.forgebot.util;

/**
 *
 */
public enum RankSystem {
    BANNED_USER("Will be ignored"),
    BOT("Will be ignored"),
    USER("Regular user"),
    INTERACTION_BOT("Bot can interact"),
    GUILD_BOT_ADMIN("Bot admin for a guild"),
    GUILD_ADMIN("Admin in a guild"),
    GUILD_OWNER("Owner of a guild"),
    CONTRIBUTOR("Contributor"),
    BOT_ADMIN("Bot administrator"),
    SYSTEM_ADMIN("System admin"),
    CREATOR("Creator");
    private final String description;

    RankSystem(String description) {
        this.description = description;
    }

    /**
     * find a rank by name
     *
     * @param search the role to search for
     * @return rank || null
     */
    public static RankSystem findRank(String search) {
        for (RankSystem rankSystem : values()) {
            if (rankSystem.name().equalsIgnoreCase(search)) {
                return rankSystem;
            }
        }
        return null;
    }

    public boolean isAtLeast(RankSystem rank) {
        return this.ordinal() >= rank.ordinal();
    }

    public boolean isHigherThan(RankSystem rank) {
        return this.ordinal() > rank.ordinal();
    }

    public String getDescription() {
        return description;
    }
}
