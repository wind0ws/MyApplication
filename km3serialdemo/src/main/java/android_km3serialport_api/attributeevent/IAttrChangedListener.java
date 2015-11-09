package android_km3serialport_api.attributeevent;

import java.util.EventListener;

/**
 * 属性改变时的监听器
 */
public interface IAttrChangedListener extends EventListener {
    public abstract void onAttrChanged(AttrChangedEvent event);
}
