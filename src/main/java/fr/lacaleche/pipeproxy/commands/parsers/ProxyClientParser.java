package fr.lacaleche.pipeproxy.commands.parsers;

import fr.lacaleche.pipe.common.commands.parsers.ClientParser;
import fr.lacaleche.pipeproxy.models.client.ProxyClientImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.parser.ParserDescriptor;

public class ProxyClientParser<C> extends ClientParser<C, ProxyClientImpl> {

    public static <C> @NonNull ParserDescriptor<C, ProxyClientImpl> proxyClientParser() {
        return ParserDescriptor.of(new ProxyClientParser<>(), ProxyClientImpl.class);
    }

}
