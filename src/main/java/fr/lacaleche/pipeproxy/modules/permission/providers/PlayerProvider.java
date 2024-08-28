package fr.lacaleche.pipeproxy.modules.permission.providers;

import com.google.common.base.Preconditions;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.common.clients.Client;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerProvider implements PermissionProvider, PermissionFunction {

    private final Player player;
    private final Client client;

    public PlayerProvider(Player player, Client client) {
        this.player = player;
        this.client = client;
    }

    @Override
    public @NonNull PermissionFunction createFunction(@NonNull PermissionSubject subject) {
        Preconditions.checkState(subject == this.player, "createFunction called with different argument");
        return this;
    }

    @Override
    public @NonNull Tristate getPermissionValue(@NonNull String permission) {
        if (this.client.isAdmin()) return Tristate.TRUE;
        return this.client.hasPermission(permission) ? Tristate.TRUE : Tristate.FALSE;
    }

}
