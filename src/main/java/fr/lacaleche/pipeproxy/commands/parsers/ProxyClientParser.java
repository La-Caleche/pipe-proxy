package fr.lacaleche.pipeproxy.commands.parsers;

import fr.lacaleche.core.commands.parsers.ClientParser;
import fr.lacaleche.pipeproxy.models.client.ProxyClientImpl;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.parser.ParserDescriptor;

public class ProxyClientParser<C> extends ClientParser<C, ProxyClient> {

    public static <C> @NonNull ParserDescriptor<C, ProxyClient> proxyClientParser() {
        return ParserDescriptor.of(new ProxyClientParser<>(), ProxyClient.class);
    }

}
