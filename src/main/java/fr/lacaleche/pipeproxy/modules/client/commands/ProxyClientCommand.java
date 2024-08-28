package fr.lacaleche.pipeproxy.modules.client.commands;

import com.velocitypowered.api.command.CommandSource;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.Permissions;
import fr.lacaleche.pipe.common.commands.interfaces.CloudCommand;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.commands.PipeProxyCommandContext;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class ProxyClientCommand implements CloudCommand {

    @Command("proxyclient|pc|pclient info <target>")
    @Permissions(minPermLevel = 20, permissions = "proxy.command.proxy-client.info")
    public void info(
            final CommandSource sender,
            final PipeProxyCommandContext context,
            @Argument(value = "target", parserName = "client")
            final Client target
    ) {
        Client client = context.clientOrReject(target);
        if (client == null) return;

        ProxyPipeImpl.get().getTaskManager().newTask(builder -> builder.run(task -> {
            sender.sendMessage(context.locale().t("global.player.infos").ph("player", client).ph("rank", client).ph("locale", client).ct());
        }).async(true));
    }

    @Command("proxyclient|pc|pclient expire <target>")
    @Permissions(minPermLevel = 100, permissions = "proxy.command.proxy-client.expire")
    public void expire(
            final CommandSource sender,
            final PipeProxyCommandContext context,
            @Argument(value = "target", parserName = "offline_cached_client")
            final Client target
    ) {
        Client client = context.clientOrReject(target);
        if (client == null) return;

        ProxyPipeImpl.get().getTaskManager().newTask(builder -> builder.run(task -> {
            client.expireNow();
            sender.sendMessage(context.locale().t("proxy.command.proxy-client.expire.success").ph("player", client.getUsername()).ct());
        }).async(true));
    }

}
