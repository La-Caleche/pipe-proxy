package fr.lacaleche.pipeproxy.modules.permission;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;
import fr.lacaleche.pipeproxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipeproxy.modules.permission.listeners.PermissionsSetupListener;

@AModule(target = ModuleTarget.PROXY)
public class PermissionModule extends Module {

    public PermissionModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        ProxyPipeListenerManager manager = ProxyPipeImpl.get().getListenerManager();
        manager.registerProxyListener(this, new PermissionsSetupListener());
    }
}
