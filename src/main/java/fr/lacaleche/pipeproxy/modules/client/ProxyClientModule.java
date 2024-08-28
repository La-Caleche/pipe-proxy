package fr.lacaleche.pipeproxy.modules.client;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.packets.CheckPermissionsPacket;
import fr.lacaleche.pipeproxy.ProxyPipe;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.ProxyPlugin;
import fr.lacaleche.pipeproxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipeproxy.modules.client.commands.LangCommand;
import fr.lacaleche.pipeproxy.modules.client.commands.ProxyClientCommand;
import fr.lacaleche.pipeproxy.modules.client.listeners.LoginListener;
import fr.lacaleche.pipeproxy.modules.client.listeners.LogoutListener;
import fr.lacaleche.pipeproxy.modules.client.listeners.ModelSavedListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AModule(target = ModuleTarget.PROXY)
public class ProxyClientModule extends Module {

    public ProxyClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        ProxyPipe pipe = ProxyPipeImpl.get();
        ProxyPlugin plugin = pipe.getPlugin();

        List<RankImpl> ranks = new ModelFilter<RankImpl>().model(RankImpl.class).saveInCache().getAll().toList();
        Logger.customDebug("%d ranks cached from database...", ranks.size());

        Collection<? extends Player> players = plugin.getServer().getAllPlayers();
        if (players.isEmpty()) return;

        Logger.customDebug("Loading clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            new ModelFilter<ClientImpl>()
                    .model((Class<ClientImpl>) Pipe.clientClass())
                    .cache(c -> c.getUUID().equals(player.getUniqueId()))
                    .sql((sql) -> sql.where(new Where("uuid", player.getUniqueId())))
                    .def(() -> (ClientImpl) Pipe.get().instanciateClient(player.getUniqueId(), player.getUsername())).getOne();
        }
    }

    @Override
    public void onDisable() {
        ProxyPipe pipe = ProxyPipeImpl.get();
        ProxyPlugin plugin = pipe.getPlugin();

        Collection<? extends Player> players = plugin.getServer().getAllPlayers();
        if (players.isEmpty()) return;

        Logger.customDebug("Removing clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            Client client = ProxyPipeImpl.get().getClient(player.getUniqueId());
            pipe.getQuitCallbacks().values().stream()
                    .flatMap(Collection::stream)
                    .forEach(callback -> callback.accept(null, player, client));
            client.expireNow();
        }

        List<RankImpl> cachedRanks = new ArrayList<RankImpl>(Core.get().getModelManager().get(RankImpl.class));
        Logger.customDebug("Removing %s ranks from cache...".formatted(cachedRanks.size()));
        cachedRanks.forEach(RankImpl::expireNow);

        pipe.removeJoinCallbacks(this);
        pipe.removeJoinServerCallbacks(this);
        pipe.removeQuitCallbacks(this);
    }

    @Override
    public void registerListeners() {
        ProxyPipeListenerManager proxyManager = ProxyPipeImpl.get().getListenerManager();
        proxyManager.registerProxyListener(this, new LoginListener());
        proxyManager.registerProxyListener(this, new LogoutListener());
        proxyManager.registerCustomListener(this, new ModelSavedListener());
    }

    @Override
    public void registerCommands() {
        ProxyPipeImpl.get().getCommandManager().registerNewCommand(this, new LangCommand());
        ProxyPipeImpl.get().getCommandManager().registerNewCommand(this, new ProxyClientCommand());
    }

    @Override
    public void registerPackets() {
        Core.get().getPacketManager().registerPacket(CheckPermissionsPacket.class);
        Core.get().getPacketManager().registerPacket(ModelSavedPacket.class);
    }

    @Override
    public void registerPlaceholders() {
        Pipe pipe = ProxyPipeImpl.get();
        PipeText text = pipe.text();

        text.registerPlaceHolder("rank", (placeHolderArguments, locale) -> {
            Object next = placeHolderArguments.next();
            if (next instanceof Player player) {
                Client client = pipe.getClient(player.getUniqueId());
                return client.getRank().getColoredRankName(locale);
            } else if (next instanceof Client client) {
                return client.getRank().getColoredRankName(locale);
            }
            return Component.text("Unknown").color(NamedTextColor.GRAY);
        });

        this.registerPlayerPlaceHolder("player");
    }

    public void registerPlayerPlaceHolder(String key) {
        Pipe pipe = ProxyPipeImpl.get();
        PipeText text = pipe.text();

        text.registerPlaceHolder(key, (placeHolderArguments, locale) -> {
            Object next = placeHolderArguments.next();
            if (next instanceof Player player) {
                Client client = pipe.getClient(player.getUniqueId());
                return client.getRank().colorize(player.getUsername());
            } else if (next instanceof Client client) {
                return client.getRank().colorize(client.getUsername());
            }
            return Component.text("Unknown").color(NamedTextColor.GRAY);
        });
    }

}
