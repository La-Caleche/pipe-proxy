package fr.lacaleche.pipeproxy.commands;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.GlobalCommandManager;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.ProxyPlugin;
import fr.lacaleche.pipeproxy.commands.parsers.*;
import fr.lacaleche.pipeproxy.commands.injectors.CommandContextInjector;
import fr.lacaleche.pipeproxy.commands.suggestions.OnlineServersSuggestion;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.CloudInjectionModule;

public class PipeProxyCommandManager extends GlobalCommandManager<CommandSource> {

    public PipeProxyCommandManager() {
        ProxyPlugin plugin = ProxyPipeImpl.get().getPlugin();
        final Injector childInjector = plugin.injector().createChildInjector(
                new CloudInjectionModule<>(
                        CommandSource.class,
                        ExecutionCoordinator.simpleCoordinator(),
                        SenderMapper.identity()
                )
        );

        final CloudProxyCommandManager commandManager = childInjector.getInstance(
                Key.get(new TypeLiteral<CloudProxyCommandManager>() {})
        );

        this.setCloudCommandManager(commandManager);
        this.setCloudAnnotationParser(new AnnotationParser<>(commandManager, CommandSource.class));

        commandManager.parameterInjectorRegistry()
                        .registerInjector(PipeProxyPipeCommandContext.class, new CommandContextInjector<>());

        commandManager.parserRegistry()
                .registerNamedParser("proxy_client", ProxyClientParser.proxyClientParser())
                .registerNamedParser("online_proxy_client", OnlineProxyClientParser.proxyClientParser())
                .registerNamedParser("offline_cached_client", OfflineCachedClientParser.parser())
                .registerSuggestionProvider("online_servers", new OnlineServersSuggestion<>());

    }

    @Override
    public Client getClient(CommandSource sender) {
        if (!(sender instanceof Player player)) return null;
        return ProxyPipeImpl.get().getClient(player);
    }
}
