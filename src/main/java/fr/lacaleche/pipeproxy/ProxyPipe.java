package fr.lacaleche.pipeproxy;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.models.clients.CoreClient;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.utils.server.interfaces.ServerManager;
import fr.lacaleche.pipeproxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public interface ProxyPipe extends Pipe {

    IModuleHandler registerNewPlugin(ProxyPlugin plugin);

    Map<ProxyPlugin, IModuleHandler> getRegisteredPlugins();

    void unregister(ProxyPlugin plugin);

    IModuleHandler getHandler(ProxyPlugin plugin);

    ProxyPipeListenerManager getListenerManager();

    ProxyPlugin getPlugin();

    void setPlugin(ProxyPlugin plugin);

    /**
     * TODO
     */
    void addJoinCallback(Module module, TriConsumer<PostLoginEvent, Player, ProxyClient> callback);

    /**
     * TODO
     */
    void addJoinServerCallback(Module module, TriConsumer<ServerConnectedEvent, Player, ProxyClient> callback);

    /**
     * TODO
     */
    void addQuitCallbacks(Module module, TriConsumer<DisconnectEvent, Player, ProxyClient> callback);

    /**
     * TODO
     */
    void removeJoinCallbacks(Module module);

    /**
     * TODO
     */
    void removeJoinServerCallbacks(Module module);

    /**
     * TODO
     */
    void removeQuitCallbacks(Module module);

    /**
     * TODO
     */
    Function<ProxyClient, Player> playerMapper();

    /**
     * TODO
     */
    void setPlayerMapper(Function<ProxyClient, Player> playerMapper);

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<PostLoginEvent, Player, ProxyClient>>> getJoinCallbacks();

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<ServerConnectedEvent, Player, ProxyClient>>> getJoinServerCallbacks();

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<DisconnectEvent, Player, ProxyClient>>> getQuitCallbacks();

    /**
     * TODO
     */
    void setServerManager(ServerManager serverManager);

    /**
     * TODO
     */
    ServerManager getServerManager();

    /**
     * TODO
     */
    ProxyClient getClient(Player player);

    /**
     * TODO
     */
    ProxyClient getClient(UUID uuid);

    static ProxyPipeImpl get() {
        return (ProxyPipeImpl) ProxyPipeImpl.get();
    }

}
