package fr.lacaleche.pipeproxy;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.commands.interfaces.CoreCommandManager;
import fr.lacaleche.core.models.clients.AbstractCoreClient;
import fr.lacaleche.core.models.clients.CoreClient;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.AbstractPipe;
import fr.lacaleche.pipe.common.utils.server.interfaces.ServerManager;
import fr.lacaleche.pipeproxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipeproxy.models.client.ProxyClientImpl;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;
import fr.lacaleche.pipeproxy.module.ProxyModuleHandler;

import java.util.*;
import java.util.function.Function;

public class ProxyPipeImpl extends AbstractPipe implements ProxyPipe {

    private final Map<ProxyPlugin, IModuleHandler> plugins;

    private final Map<Module, List<TriConsumer<PostLoginEvent, Player, ProxyClient>>> joinCallbacks;
    private final Map<Module, List<TriConsumer<ServerConnectedEvent, Player, ProxyClient>>> joinServerCallbacks;
    private final Map<Module, List<TriConsumer<DisconnectEvent, Player, ProxyClient>>> quitCallbacks;

    private Function<ProxyClient, Player> playerMapper;
    private ProxyPlugin mainPlugin;
    private ServerManager serverManager;

    public ProxyPipeImpl() {
        super();

        this.plugins = new HashMap<>();

        this.joinCallbacks = new HashMap<>();
        this.joinServerCallbacks = new HashMap<>();
        this.quitCallbacks = new HashMap<>();

        Core.get().getClassMatcher().registerMatch(CoreClient.class, ProxyClientImpl.class)
                .registerMatch(AbstractCoreClient.class, ProxyClientImpl.class);
    }

    @Override
    public ProxyPlugin getPlugin() {
        return this.mainPlugin;
    }

    @Override
    public void setPlugin(ProxyPlugin plugin) {
        if (this.mainPlugin != null) throw new IllegalStateException("Plugin already set");
        this.mainPlugin = plugin;
    }

    @Override
    public IModuleHandler registerNewPlugin(ProxyPlugin plugin) {
        final CoreCommandManager<?> commandManager = Core.get().getCommandManager();
        final IModuleHandler handler = new ProxyModuleHandler(plugin);
        commandManager.registerHandler(handler);
        this.plugins.put(plugin, handler);
        return handler;
    }

    @Override
    public IModuleHandler getHandler(ProxyPlugin plugin) {
        return this.plugins.get(plugin);
    }

    @Override
    public Map<ProxyPlugin, IModuleHandler> getRegisteredPlugins() {
        return this.plugins;
    }

    @Override
    public void unregister(ProxyPlugin plugin) {
        this.plugins.remove(plugin);
    }

    @Override
    public ProxyPipeListenerManager getListenerManager() {
        return Core.get().getListenerManager();
    }

    @Override
    public Function<ProxyClient, Player> playerMapper() {
        return this.playerMapper;
    }

    @Override
    public void setPlayerMapper(Function<ProxyClient, Player> playerMapper) {
        if (this.playerMapper != null) throw new IllegalStateException("Player mapper already set");
        this.playerMapper = playerMapper;
    }

    @Override
    public void addJoinCallback(Module module, TriConsumer<PostLoginEvent, Player, ProxyClient> callback) {
        List<TriConsumer<PostLoginEvent, Player, ProxyClient>> moduleCallbacks = this.joinCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.joinCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void addJoinServerCallback(Module module, TriConsumer<ServerConnectedEvent, Player, ProxyClient> callback) {
        List<TriConsumer<ServerConnectedEvent, Player, ProxyClient>> moduleCallbacks = this.joinServerCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.joinServerCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void addQuitCallbacks(Module module, TriConsumer<DisconnectEvent, Player, ProxyClient> callback) {
        List<TriConsumer<DisconnectEvent, Player, ProxyClient>> moduleCallbacks = this.quitCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.quitCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void removeJoinCallbacks(Module module) {
        this.joinCallbacks.remove(module);
    }

    @Override
    public void removeJoinServerCallbacks(Module module) {
        this.joinServerCallbacks.remove(module);
    }

    @Override
    public void removeQuitCallbacks(Module module) {
        this.quitCallbacks.remove(module);
    }

    @Override
    public Map<Module, List<TriConsumer<PostLoginEvent, Player, ProxyClient>>> getJoinCallbacks() {
        return this.joinCallbacks;
    }

    @Override
    public Map<Module, List<TriConsumer<ServerConnectedEvent, Player, ProxyClient>>> getJoinServerCallbacks() {
        return this.joinServerCallbacks;
    }

    @Override
    public Map<Module, List<TriConsumer<DisconnectEvent, Player, ProxyClient>>> getQuitCallbacks() {
        return this.quitCallbacks;
    }

    @Override
    public ServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public ProxyClient getClient(Player player) {
        return this.getClient(player.getUniqueId());
    }

    @Override
    public ProxyClient getClient(UUID uuid) {
        return (ProxyClient) Core.get().getClient(uuid);
    }

    private static class ProxyPipeSingletonHelper {
        private static final ProxyPipe INSTANCE = new ProxyPipeImpl();
    }

    public static ProxyPipe get() {
        return ProxyPipeSingletonHelper.INSTANCE;
    }

}
