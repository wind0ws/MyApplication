package android_km3serialport_api.attributeevent;

import java.util.EventObject;

/**
 * 泛型属性改变事件
 */
public class AttrChangedEvent extends EventObject {
    /**
     * Constructs a new instance of this class.
     *
     * @param source the object which fired the event.
     */
    public AttrChangedEvent(Object source, Object oldValue, Object newValue) {
        super(source);
        if (oldValue == null||newValue==null) {
            throw new IllegalArgumentException("oldValue和newValue 均不能为 null");
        }
        this.oldValue=oldValue;
        this.newValue=newValue;
    }

    private Object oldValue ;
    private Object newValue;

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    @Override
    public String toString() {
        return "source:"+source.toString()+",oldVal:"+oldValue.toString()+",newVal:"+newValue.toString();
    }
}
