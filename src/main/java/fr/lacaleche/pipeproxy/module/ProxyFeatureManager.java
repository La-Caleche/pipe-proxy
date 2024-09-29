package fr.lacaleche.pipeproxy.module;

import fr.lacaleche.core.modules.FeatureManagerImpl;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.utils.PipeDebug;

public class ProxyFeatureManager extends FeatureManagerImpl {

    @Override
    public <T> void cancelEvent(Object event, IFeature<T> feature, String message) {
        PipeDebug.setCancelled(event, true, () -> Logger.debugDev(this.parseMessage(event, feature, message)));
    }

}
