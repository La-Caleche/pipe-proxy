package fr.lacaleche.pipeproxy.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.velocity.VelocityCommandManager;

public class CloudProxyCommandManager extends VelocityCommandManager<CommandSource> {

    @Inject
    public CloudProxyCommandManager(@NonNull PluginContainer plugin, @NonNull ProxyServer proxyServer, @NonNull ExecutionCoordinator<CommandSource> commandExecutionCoordinator, @NonNull SenderMapper<CommandSource, CommandSource> senderMapper) {
        super(plugin, proxyServer, commandExecutionCoordinator, senderMapper);
    }

    @Override
    public @NonNull PermissionResult testPermission(@NonNull CommandSource sender, @NonNull Permission permission) {
        PipeProxyCommandManager commandManager = (PipeProxyCommandManager) ProxyPipeImpl.get().getCommandManager();
        if (commandManager != null && permission.permissionString().startsWith("lc_perms"))
            return commandManager.hasPermission(commandManager.getClient(sender), permission);
        return super.testPermission(sender, permission);
    }
}
