package site.romvoid.forgebot.commands;

/**
 * Visibility of a command
 */
public enum CommandVisibility {
    PRIVATE(), PUBLIC(), BOTH();

    /**
     * Implements the command for Private Use
     */
    public boolean isForPrivate() {
        return this.equals(PRIVATE) || this.equals(BOTH);
    }

    /**
     * Implements the command for Public Use
     */
    public boolean isForPublic() {
        return this.equals(PUBLIC) || this.equals(BOTH);
    }
}
