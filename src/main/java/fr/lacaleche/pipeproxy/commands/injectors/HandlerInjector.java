package fr.lacaleche.pipeproxy.commands.injectors;

import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipeproxy.ProxyPipe;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.injection.ParameterInjector;
import org.incendo.cloud.util.annotation.AnnotationAccessor;

public class HandlerInjector<C> implements ParameterInjector<C, IModuleHandler> {

    @Override
    public @Nullable IModuleHandler create(
            final @NonNull CommandContext<C> context,
            final @NonNull AnnotationAccessor annotationAccessor
    ) {
        return ProxyPipe.get().getHandler(ProxyPipe.get().getPlugin());
    }

}

