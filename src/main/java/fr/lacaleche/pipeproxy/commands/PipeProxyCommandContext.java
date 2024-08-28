package fr.lacaleche.pipeproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.GlobalCommandContext;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;

import java.util.Optional;

public class PipeProxyCommandContext extends GlobalCommandContext<CommandSource> {

    public PipeProxyCommandContext(CommandSource sender) {
        super(sender);
    }

    public Optional<Player> parsePlayer(Player player) {
        if (!(sender() instanceof Player) && player == null) {
            sender().sendMessage(locale().t("global.only_for_players").from("Fly").ct());
            return Optional.empty();
        }

        return Optional.of(player == null ? (Player) sender() : player);
    }

    @Override
    public <T> Optional<Client> searchClient(T target) {
        if (!(target instanceof Player player)) return Optional.empty();
        return Optional.of(ProxyPipeImpl.get().getClient(player));
    }

    @Override
    public Client clientOrReject(Client client) {
        if (this.client() == null && client == null) {
            sender().sendMessage(locale().t("global.only_for_players").from("Fly").ct());
            return null;
        }
        return client == null ? this.client() : client;
    }
}
