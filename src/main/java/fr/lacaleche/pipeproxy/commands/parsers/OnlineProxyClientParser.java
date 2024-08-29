package fr.lacaleche.pipeproxy.commands.parsers;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.common.commands.parsers.CloudParserException;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.models.client.ProxyClientImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ParserDescriptor;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class OnlineProxyClientParser<C> extends ProxyClientParser<C> {

    protected static CloudParserException exception(
            final @NonNull String input,
            final @NonNull CommandContext<?> context
    ) {
        return CloudParserException.buildException(
                OnlineProxyClientParser.class,
                context,
                Caption.of("argument.parse.failure.client.offline"),
                CaptionVariable.of("input", input)
        );
    }

    public static <C> @NonNull ParserDescriptor<C, ProxyClientImpl> proxyClientParser() {
        return ParserDescriptor.of(new OnlineProxyClientParser<>(), ProxyClientImpl.class);
    }

    @Override
    public @NonNull ArgumentParseResult<ProxyClientImpl> parse(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
        final ArgumentParseResult<ProxyClientImpl> result = super.parse(commandContext, commandInput);
        if (result.failure().isPresent()) return result;

        final Optional<ProxyClientImpl> optionalClient = result.parsedValue();
        if (optionalClient.isEmpty()) return result;

        final ProxyClientImpl client = optionalClient.get();
        if (client.isOnline()) return result;

        return ArgumentParseResult.failure(exception(client.getUsername(), commandContext));
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull CommandContext<C> commandContext, @NonNull CommandInput input) {
        final Iterable<@NonNull String> suggestions = super.stringSuggestions(commandContext, input);
        final Stream<String> targetStream = StreamSupport.stream(suggestions.spliterator(), false);
        final Collection<String> onlinePlayers = ProxyPipeImpl.get().getPlugin().getServer().getAllPlayers().stream().map(Player::getUsername).toList();

        return targetStream
                .filter(onlinePlayers::contains)
                .filter(name -> input.remainingInput().isBlank() || name.toLowerCase(Locale.ROOT).startsWith(input.remainingInput().toLowerCase(Locale.ROOT)))
                .toList();
    }
}
