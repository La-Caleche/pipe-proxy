package fr.lacaleche.pipeproxy;

import com.google.inject.Injector;
import com.velocitypowered.api.proxy.ProxyServer;

public interface ProxyPlugin {

    Injector injector();

    ProxyServer getServer();

}
