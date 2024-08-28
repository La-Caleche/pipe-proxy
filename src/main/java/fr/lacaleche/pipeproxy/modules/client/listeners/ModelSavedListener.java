package fr.lacaleche.pipeproxy.modules.client.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.ProxyPlugin;
import fr.lacaleche.pipeproxy.modules.client.ProxyClientModule;

public class ModelSavedListener implements CoreListener {

    @PacketReader(packet = ModelSavedPacket.class)
    public void onModelUpdated(PacketReadEvent event, ModelSavedPacket packet) {
        if (packet.getResourceClass().equals(ClientImpl.class.getName())) {
            Client client = new ModelFilter<ClientImpl>().model((Class<ClientImpl>) Pipe.clientClass())
                    .cache((model) -> model.getId() == packet.getResourceId())
                    .sql((sql) -> sql.where(new Where("id", packet.getResourceId()))).getOne();

            ProxyPlugin plugin = ProxyPipeImpl.get().getPlugin();
            Player player = plugin.getServer().getPlayer(client.getUUID()).orElse(null);
            if (player == null) return;

            client.refresh();
        }
    }

}
