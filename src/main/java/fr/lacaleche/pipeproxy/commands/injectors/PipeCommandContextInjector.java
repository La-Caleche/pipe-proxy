package fr.lacaleche.pipeproxy.commands.injectors;

import com.velocitypowered.api.command.CommandSource;
import fr.lacaleche.pipe.common.commands.PipeCommandContext;
import fr.lacaleche.pipeproxy.commands.ProxyCommandContext;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.injection.ParameterInjector;
import org.incendo.cloud.util.annotation.AnnotationAccessor;

public class PipeCommandContextInjector<C> implements ParameterInjector<C, PipeCommandContext> {

    @Override
    public @Nullable PipeCommandContext<CommandSource, ProxyClient> create(
            final @NonNull CommandContext<C> context,
            final @NonNull AnnotationAccessor annotationAccessor
    ) {
        if (!(context.sender() instanceof CommandSource source)) {
            throw new IllegalArgumentException("Cannot get PipeCommandContextInjector for " + context.sender().getClass().getSimpleName());
        }
        return new ProxyCommandContext(source);
    }

}
