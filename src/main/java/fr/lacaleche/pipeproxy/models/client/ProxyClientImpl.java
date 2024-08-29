package fr.lacaleche.pipeproxy.models.client;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;

import java.util.UUID;

public class ProxyClientImpl extends ClientImpl implements ProxyClient {

    private Player cachedPlayer;

    public ProxyClientImpl(UUID uuid, String username) {
        super(uuid, username);
        this.cachedPlayer = ProxyPipeImpl.get().getPlugin().getServer().getPlayer(uuid).orElse(null);
        this.save();
    }

    @Override
    public Player getPlayer() {
        if (this.cachedPlayer == null)
            this.cachedPlayer = ProxyPipeImpl.get().getPlugin().getServer().getPlayer(this.getUUID()).orElse(null);
        return this.cachedPlayer;
    }

    @Override
    public boolean isOnline() {
        Player player = this.getPlayer();
        boolean online = player != null && player.isActive();
        if (!online && player != null)
            this.cachedPlayer = null;
        return online;
    }

    @Override
    public boolean expire() {
        if (this.isOnline()) {
            Logger.info("Trying to expire a client that is still online. Username: %s, UUID: %s", this.getUsername(), this.getUUID().toString());
            return false;
        }
        this.cachedPlayer = null;
        return super.expire();
    }
}
