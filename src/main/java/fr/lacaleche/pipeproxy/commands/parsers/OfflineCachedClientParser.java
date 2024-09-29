package fr.lacaleche.pipeproxy.commands.parsers;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.commands.parsers.CachedClientParser;
import fr.lacaleche.core.models.clients.AbstractCoreClient;
import fr.lacaleche.core.models.clients.CoreClient;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.ProxyPlugin;
import fr.lacaleche.pipeproxy.models.client.ProxyClientImpl;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ParserDescriptor;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class OfflineCachedClientParser<C> extends CachedClientParser<C, ProxyClient> {

    public static <C> @NonNull ParserDescriptor<C, ProxyClient> parser() {
        return ParserDescriptor.of(new OfflineCachedClientParser<>(), ProxyClient.class);
    }

    @Override
    public @NonNull ArgumentParseResult<ProxyClient> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final ProxyClient client = offlineClients().stream()
                    .filter(cachedClient -> cachedClient.getUsername().equalsIgnoreCase(input))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Client not found"));
            return ArgumentParseResult.success(client);
        } catch (final IllegalArgumentException exception) {
            return ArgumentParseResult.failure(exception(input, commandContext, "nforonline"));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        return offlineClients()
                .stream()
                .map(CoreClient::getUsername)
                .collect(Collectors.toList());
    }

    private Collection<UUID> onlinePlayers() {
        return ProxyPipeImpl.get().getPlugin().getServer().getAllPlayers().stream().map(Player::getUniqueId).toList();
    }

    private Collection<ProxyClientImpl> offlineClients() {
        return Core.get().getModelManager().get((Class<ProxyClientImpl>) Core.matchClass(AbstractCoreClient.class)).stream()
                .filter(client -> onlinePlayers().stream().noneMatch(uuid -> uuid.equals(client.getUUID()))).toList();
    }

}
