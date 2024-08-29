package fr.lacaleche.pipeproxy.models.client.interfaces;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.common.clients.Client;

import java.util.List;

public interface ProxyClient extends Client {

    boolean isOnline();

    Player getPlayer();

}
