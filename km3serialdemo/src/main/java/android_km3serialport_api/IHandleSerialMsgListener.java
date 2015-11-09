package android_km3serialport_api;

import android.os.Handler;

/**
 * 串口已分析数据接收器
 */
public interface IHandleSerialMsgListener {
    /**
     * 增加监听器
     * @param handler 要处理数据的handler
     * @return 如果handler已存在或者添加Listener成功则返回true
     */
    boolean addListener(Handler handler);

    /**
     * 移除监听器
     * @param handler 要处理数据的handler
     * @return 如果handler不存在或者移除handler成功则返回true
     */
    boolean removeListener(Handler handler);
}
