package fr.lacaleche.pipeproxy.module;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.pipeproxy.ProxyPlugin;

public class ProxyModule extends Module {

    public ProxyModule() {
        this.setFeatureManager(new ProxyFeatureManager());
    }

    public ProxyPlugin getParent() {
        return this.getHandler().getParent();
    }

}
