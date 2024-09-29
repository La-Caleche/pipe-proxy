package fr.lacaleche.pipeproxy.commands;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.commands.CoreCommandManagerImpl;
import fr.lacaleche.pipe.common.commands.PipeCommandContext;
import fr.lacaleche.pipe.common.commands.PipeCommandManagerImpl;
import fr.lacaleche.pipe.common.commands.parsers.PipeLocaleParser;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.ProxyPlugin;
import fr.lacaleche.pipeproxy.commands.injectors.PipeCommandContextInjector;
import fr.lacaleche.pipeproxy.commands.injectors.ProxyCommandContextInjector;
import fr.lacaleche.pipeproxy.commands.parsers.OfflineCachedClientParser;
import fr.lacaleche.pipeproxy.commands.parsers.OnlineProxyClientParser;
import fr.lacaleche.pipeproxy.commands.parsers.ProxyClientParser;
import fr.lacaleche.pipeproxy.commands.suggestions.OnlineServersSuggestion;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.velocity.CloudInjectionModule;

public class CoreProxyCommandManagerImpl extends PipeCommandManagerImpl<CommandSource> {

    public CoreProxyCommandManagerImpl(ProxyPlugin plugin) {
        super(CoreProxyCommandManagerImpl.createCloudCommandManager(plugin));

        final CloudProxyCommandManager commandManager = (CloudProxyCommandManager) this.getCloudCommandManager();
        this.setCloudCommandManager(commandManager);
        this.setCloudAnnotationParser(new AnnotationParser<>(commandManager, CommandSource.class));

        commandManager.parameterInjectorRegistry()
                .registerInjector(PipeCommandContext.class, new PipeCommandContextInjector<>())
                .registerInjector(ProxyCommandContext.class, new ProxyCommandContextInjector<>());

        commandManager.parserRegistry()
                .registerParser(ProxyClientParser.proxyClientParser())
                .registerNamedParser("online_proxy_client", OnlineProxyClientParser.proxyClientParser())
                .registerNamedParser("offline_cached_client", OfflineCachedClientParser.parser())
                .registerSuggestionProvider("online_servers", new OnlineServersSuggestion<>());

    }

    private static CloudProxyCommandManager createCloudCommandManager(ProxyPlugin plugin) {
        final Injector childInjector = plugin.injector().createChildInjector(
                new CloudInjectionModule<>(
                        CommandSource.class,
                        ExecutionCoordinator.simpleCoordinator(),
                        SenderMapper.identity()
                )
        );

        return childInjector.getInstance(
                Key.get(new TypeLiteral<>() {
                })
        );
    }

    @Override
    public ProxyClient getClient(CommandSource sender) {
        if (!(sender instanceof Player player)) return null;
        return ProxyPipeImpl.get().getClient(player);
    }
}
