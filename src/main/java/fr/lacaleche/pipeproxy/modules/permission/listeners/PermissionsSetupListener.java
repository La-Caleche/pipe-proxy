package fr.lacaleche.pipeproxy.modules.permission.listeners;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipeproxy.modules.permission.providers.PlayerProvider;

public class PermissionsSetupListener {

    @Subscribe
    public void onPlayerPermissionsSetup(PermissionsSetupEvent event, Continuation continuation) {
        if (!(event.getSubject() instanceof Player player)) {
            continuation.resume();
            return;
        }

        Client client = new ModelFilter<ClientImpl>()
                .model((Class<ClientImpl>) Pipe.clientClass())
                .cache(c -> c.getUUID().equals(player.getUniqueId()))
                .sql((sql) -> sql.where(new Where("uuid", player.getUniqueId())))
                .def(() -> (ClientImpl) Pipe.get().instanciateClient(player.getUniqueId(), player.getUsername())).getOne();

        event.setProvider(new PlayerProvider(player, client));
        continuation.resume();
    }

}
