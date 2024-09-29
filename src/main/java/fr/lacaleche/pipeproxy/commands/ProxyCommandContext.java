package fr.lacaleche.pipeproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.commands.AbstractCommandContext;
import fr.lacaleche.core.commands.enums.CommandExecutor;
import fr.lacaleche.core.models.clients.CoreClient;
import fr.lacaleche.pipe.common.commands.PipeCommandContext;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.models.client.interfaces.ProxyClient;

import java.util.Optional;

public class ProxyCommandContext extends PipeCommandContext<CommandSource, ProxyClient> {

    public ProxyCommandContext(CommandSource sender) {
        super(sender);
    }

    public Optional<Player> parsePlayer(Player player) {
        if (!(sender() instanceof Player) && player == null) {
            sender().sendMessage(locale().t("global.only_for_players").buildComponent());
            return Optional.empty();
        }

        return Optional.of(player == null ? (Player) sender() : player);
    }

    @Override
    public <T> Optional<ProxyClient> searchClient(T target) {
        if (!(target instanceof Player player)) return Optional.empty();
        return Optional.of(ProxyPipeImpl.get().getClient(player));
    }

    @Override
    public ProxyClient clientOrReject(ProxyClient client) {
        if (this.client() == null && client == null) {
            sender().sendMessage(locale().t("global.only_for_players").buildComponent());
            return null;
        }
        return client == null ? this.client() : client;
    }

    @Override
    public CommandExecutor executor() {
        if (sender() instanceof Player)
            return CommandExecutor.PLAYER;
        else if (sender() instanceof ConsoleCommandSource)
            return CommandExecutor.SERVER;
        return CommandExecutor.COMMON;
    }
}
