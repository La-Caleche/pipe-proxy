package fr.lacaleche.pipeproxy.modules.client.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.utils.PipeDebug;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collection;

public class LoginListener {

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        PipeDebug.eventCalled(event);
        Player player = event.getPlayer();

        Client client = new ModelFilter<ClientImpl>()
                .model((Class<ClientImpl>) Pipe.clientClass())
                .cache(c -> c.getUUID().equals(player.getUniqueId()))
                .sql((sql) -> sql.where(new Where("uuid", player.getUniqueId())))
                .def(() -> (ClientImpl) Pipe.get().instanciateClient(player.getUniqueId(), player.getUsername())).getOne();

        client.cancelExpiration();

        if (client.getUsername() == null || !client.getUsername().equals(player.getUsername()))
            client.setUsername(player.getUsername());

        if (client.getRank().getPermissionLevel() == 0)
            player.disconnect(Component.text("This server is still in development.", NamedTextColor.RED));

        ProxyPipeImpl.get().getJoinCallbacks().values().stream()
                .flatMap(Collection::stream)
                .forEach(callback -> callback.accept(event, player, client));
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        Client client = ProxyPipeImpl.get().getClient(player.getUniqueId());
        ProxyPipeImpl.get().getJoinServerCallbacks().values().stream()
                .flatMap(Collection::stream)
                .forEach(callback -> callback.accept(event, player, client));
    }
}
