package fr.lacaleche.pipeproxy.module;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.handler.ModuleHandler;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipeproxy.ProxyPlugin;
import fr.lacaleche.pipeproxy.events.ProxyPipeListenerManager;

public class ProxyModuleHandler extends ModuleHandler {

    private final ProxyPlugin plugin;

    public ProxyModuleHandler(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void disableModule(IModule module) {
        super.disableModule(module);

        if (Core.get().getListenerManager() instanceof ProxyPipeListenerManager manager) {
            manager.unregisterProxyListeners(module);
        }
    }

    @Override
    public ProxyPlugin getParent() {
        return plugin;
    }
    
}
