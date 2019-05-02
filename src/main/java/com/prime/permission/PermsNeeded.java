package com.prime.permission;

/**
 * Permission requirements.
 *
 * @author ROMVoid
 */
public class PermsNeeded {
    private final String permsNeededNode;
    private final boolean isAuthorExclusive;
    private final boolean isDefault;

    /**
     * Constructs a new PermissionRequirements object.
     *
     * @param permsNeededNode 		the permission node that allows a user to pass the permission check.
     * @param isBotOnwerOnly      	whether this permission is exclusively granted for bot authors.
     * @param isDefault             whether users should have this permission by default (if no other permission entry covers it).
     */
    public PermsNeeded(String permsNeededNode, boolean isBotOnwerOnly, boolean isDefault) {
        this.permsNeededNode = permsNeededNode;
        this.isAuthorExclusive = isBotOnwerOnly;
        this.isDefault = isDefault;
    }

    /**
     * Checks whether the conditions set in this object are met by a user's permissions.
     *
     * @param userPermissions the user permissions access object.
     * @return true if all conditions are met, false otherwise.
     */
    public boolean coveredBy(UserPermissions userPermissions) {
        // bot authors have all permissions
        if (userPermissions.isBotAuthor())
            return true;

        // author exclusive permissions are not accessible for other users
        if (isAuthorExclusive)
            return false;

        // server owner has all perms on his server
        if (userPermissions.isServerOwner())
            return true;

        //MASTER permissions
        if (userPermissions.getEffectivePermissionEntry(null, "command.*") != null) {
            return true;
        }

        Permission effectiveEntry = userPermissions.getEffectivePermissionEntry(null, permsNeededNode);
        if (effectiveEntry == null) {
            // defaults
            return isDefault;
        } else
            // check permission
            return !effectiveEntry.isNegated();
    }

    /**
     * @return the permission node that lets a user pass
     */
    public String getRequiredPermissionNode() {
        return permsNeededNode;
    }

    /**
     * @return whether this permission is author exclusive.
     */
    public boolean isAuthorExclusive() {
        return isAuthorExclusive;
    }

    /**
     * @return whether this permission is granted by default (if no other permission entry covers it).
     */
    public boolean isDefault() {
        return isDefault;
    }
}
