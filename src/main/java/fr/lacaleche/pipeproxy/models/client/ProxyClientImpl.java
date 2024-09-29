package fr.lacaleche.pipeproxy.models.client;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.models.clients.AbstractCoreClient;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.models.client.PipeAbstractClient;
import fr.lacaleche.pipeproxy.ProxyPipe;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.ProxyPlugin;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;
import fr.lacaleche.pipeproxy.module.ProxyModule;

import java.util.UUID;

public class ProxyClientImpl extends PipeAbstractClient<Player> implements ProxyClient {

    private Player cachedPlayer;

    public ProxyClientImpl(UUID uuid, String username) {
        super(uuid, username);
        this.cachedPlayer = ProxyPipe.get().playerMapper().apply(this);
        this.save();
    }

    @Override
    public Player getPlayer() {
        if (this.cachedPlayer == null)
            this.cachedPlayer = ProxyPipe.get().playerMapper().apply(this);
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
