package android_km3serialport_api.attributeevent;

import java.util.Vector;

/**
 *
 */
public class ListenerManager {
    public ListenerManager() {
        listeners=new Vector<>();
    }
    private Vector<IAttrChangedListener> listeners;
    public synchronized void addListener(IAttrChangedListener listener){
        listeners.addElement(listener);
    }
    public synchronized void removeListener(IAttrChangedListener listener) {
        listeners.removeElement(listener);
    }
    public void fireAEvent(AttrChangedEvent event){
        Vector<IAttrChangedListener> currentListeners=null;
        synchronized (this){
           currentListeners= (Vector<IAttrChangedListener>) listeners.clone();
        }
        IAttrChangedListener oneListener;
        for(int i=0;i<currentListeners.size();i++) {
            oneListener = currentListeners.elementAt(i);
            oneListener.onAttrChanged(event);//触发属性改变事件
        }
//        for(IValueChangedListener listener:currentListeners){
//            listener.performed(event);
//        }

    }
}
