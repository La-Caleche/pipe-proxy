package fr.lacaleche.pipeproxy.modules.client.commands;

import com.velocitypowered.api.command.CommandSource;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.Permissions;
import fr.lacaleche.pipe.common.commands.interfaces.CloudCommand;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.commands.PipeProxyCommandContext;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class LangCommand implements CloudCommand {

    @Command("lang|language|locale <locale> [target]")
    public void setLang(
            final CommandSource sender,
            final PipeProxyCommandContext context,
            @Argument("locale")
            final Locale locale,
            @Argument(value = "target", parserName = "cached_client")
            final Client target
    ) {
        Client client = context.clientOrReject(target);
        if (client == null) return ;

        ProxyPipeImpl.get().getTaskManager().newTask(builder -> builder.run(task -> {
            client.setLocale(locale);
            sender.sendMessage(context.locale().t("proxy.command.lang.success").ph("locale", locale).ph("player", client.getUsername()).ct());
        }).async(true));
    }

    @Command("lang|language|locale get [target]")
    @Permissions(minPermLevel = 20, permissions = "proxy.command.locale.get")
    public void getLocale(
            final CommandSource sender,
            final PipeProxyCommandContext context,
            @Argument(value = "target", parserName = "cached_client")
            final Client target
    ) {
        Client client = context.clientOrReject(target);
        if (client == null) return ;

        Locale lang = client.getLocale();
        sender.sendMessage(context.locale().t("proxy.command.lang.get").ph("player", client.getUsername()).ph("lang", lang).ct());
    }

}
