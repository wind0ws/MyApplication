package com.micharl.serialportdemo.attributeevent;

import java.util.EventListener;

/**
 * 属性改变时的监听器
 */
public interface IAttrChangedListener extends EventListener {
    public abstract void onAttrChanged(AttrChangedEvent event);
}
