package fr.lacaleche.pipeproxy.commands.injectors;

import com.velocitypowered.api.command.CommandSource;
import fr.lacaleche.pipeproxy.commands.PipeProxyPipeCommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.injection.ParameterInjector;
import org.incendo.cloud.util.annotation.AnnotationAccessor;

public class CommandContextInjector<C> implements ParameterInjector<C, PipeProxyPipeCommandContext> {

    @Override
    public @Nullable PipeProxyPipeCommandContext create(
            final @NonNull CommandContext<C> context,
            final @NonNull AnnotationAccessor annotationAccessor
    ) {
        if (!(context.sender() instanceof CommandSource source)) {
            throw new IllegalArgumentException("Cannot get PipeProxyCommandContext for " + context.sender().getClass().getSimpleName());
        }
        return new PipeProxyPipeCommandContext(source);
    }

}
