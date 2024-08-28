package fr.lacaleche.pipeproxy.modules.handler;

import fr.lacaleche.pipe.common.modules.handler.MinecraftModuleHandler;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipeproxy.events.ProxyPipeListenerManager;

public class ProxyModuleHandler extends MinecraftModuleHandler {

    @Override
    public void disableModule(IModule module) {
        super.disableModule(module);

        if (Core.get().getListenerManager() instanceof ProxyPipeListenerManager manager) {
            manager.unregisterProxyListeners(module);
        }
    }
    
}
