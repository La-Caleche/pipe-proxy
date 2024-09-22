package fr.lacaleche.pipeproxy.commands.suggestions;

import fr.lacaleche.pipe.common.utils.server.PipeServer;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

public class OnlineServersSuggestion<C> implements BlockingSuggestionProvider.Strings<C> {

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> commandContext, @NonNull CommandInput input) {
        return ProxyPipeImpl.get().getServerManager().getServers().stream().map(PipeServer::getHost).toList();
    }

}
