package fr.lacaleche.pipeproxy.modules.client.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.utils.PipeDebug;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;

import java.util.Collection;

public class LogoutListener {

    @Subscribe
    public void onPlayerLeft(DisconnectEvent event) {
        PipeDebug.eventCalled(event);
        Player player = event.getPlayer();

        Client client = ProxyPipeImpl.get().getClient(player.getUniqueId());
        client.startExpiration();

        ProxyPipeImpl.get().getQuitCallbacks().values().stream()
                .flatMap(Collection::stream)
                .forEach(callback -> callback.accept(event, player, client));
    }

}
