package fr.lacaleche.pipeproxy.modules.prmodule;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipeproxy.ProxyPipeImpl;

@AModule(target = ModuleTarget.PROXY)
public class PluginsManagerModule extends Module {

    public PluginsManagerModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerCommands() {
    }
}
