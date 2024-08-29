package fr.lacaleche.pipeproxy.commands.parsers;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.parsers.CachedClientParser;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class OfflineCachedClientParser<C> extends CachedClientParser<C> {

    public static <C> @NonNull ParserDescriptor<C, Client> parser() {
        return ParserDescriptor.of(new OfflineCachedClientParser<>(), Client.class);
    }

    @Override
    public @NonNull ArgumentParseResult<Client> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.readString();
        try {
            final Client client = offlineClients().stream()
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
                .map(Client::getUsername)
                .collect(Collectors.toList());
    }

    private Collection<UUID> onlinePlayers() {
        return ProxyPipeImpl.get().getPlugin().getServer().getAllPlayers().stream().map(Player::getUniqueId).toList();
    }

    private Collection<ClientImpl> offlineClients() {
        return Core.get().getModelManager().get((Class<ClientImpl>) Pipe.clientClass()).stream()
                .filter(client -> onlinePlayers().stream().noneMatch(uuid -> uuid.equals(client.getUUID()))).toList();
    }

}
