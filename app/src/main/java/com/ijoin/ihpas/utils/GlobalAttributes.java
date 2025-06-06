package com.ijoin.ihpas.utils;


/**
 * 与蓝牙相关的一些属性设置
 */
public interface GlobalAttributes {

    /**
     * 特定服务的UUID
     */
    String UUID_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
    /**
     * 特定服务下的可通知的特征的UUID
     */
    String UUID_CHARA_NOTIFY = "0000fff4-0000-1000-8000-00805f9b34fb";
    /**
     * 特定服务下的可写特征的UUID
     */
    String UUID_CHARA_WRITE = "0000fff3-0000-1000-8000-00805f9b34fb";

    /**
     * Mac地址的头
     */
    String MAC_HEAD = "0A:DD:";

    /**
     * 水平方位校准的指令
     */
    String ORDER_HORIZONTAL_CALIBRATION = "ee03";

    /**
     * 水平方位校准结束的指令
     */
    String ORDER_HORIZONTAL_FINISH = "ee04";
    /**
     * 垂直方位校准的指令
     */
    String ORDER_VERTICAL_CALIBRATION = "ee13";

    /**
     * 垂直方位校准结束的指令
     */
    String ORDER_VERTICAL_FINISH = "ee14";
    /**
     * 六面校准的指令
     */
    String ORDER_SIX_SIDE = "ee05";

    /**
     * 接收的六面校准的指令
     */
    String ORDER_SIX_SIDE_ACCEPT = "ff05";

    /**
     * 参考模块选取指令
     */
    String ORDER_REFERENCE_SELECT = "ee060a";

    /**
     * 测量模块确认的指令
     */
    String ORDER_MEASURE_CONFIRM = "ee060b";

    /**
     * 接收模块选择的指令
     */
    String ORDER_MOUDLE_SELECT = "ff06";

    /**
     * 参考模块进入测量状态（工作）的指令
     */
    String ORDER_REFERENCE_WORK = "ee07";

    /**
     * 接收参考模块发送过来的测量数据指令
     */
    String ORDER_REFERENCE_DATA_ACCEPT = "ff07";
    /**
     * 接收收到磁干扰数据指令
     */
    String ORDER_MAGNETIC_INTERFERENCE_DATA_ACCEPT = "fee1";

    /**
     * 测量模块进入测量状态（工作）的指令
     */
    String ORDER_MEASURE_WORK = "ee08";

    /**
     * 接收测量模块发送过来的测量数据指令
     */
    String ORDER_MEASURE_DATA_ACCEPT = "ff08";

    /**
     * 接收重连后的测量数据指令
     */
    String ORDER_RECONNECTION_DATA_ACCEPT = "ff00";
    /**
     * 参考模块的标定指令
     */
    String ORDER_REFERENCE_MARK = "ee090a";

    /**
     * 参考模块的标定指令
     */
    String ORDER_MODULE_MARK = "ee09";

    /**
     * 测量模块的标定指令
     */
    String ORDER_MEASURE_MARK = "ee090b";

    /**
     * 接收指令中的区分位，参考模块
     */
    String ORDER_REFERENCE_DIFF = "0a";
    /**
     * 接收指令中的区分位，测量模块
     */
    String ORDER_MEASURE_DIFF = "0b";

    /**
     * 设备停止所有工作
     */
    String ORDER_STOP_ALL = "ee02";
    /**
     * 设备停止所有工作并让设备进入等待命令状态
     */
    String ORDER_STOP_ALL_AND_WAITING_ORDER = "ee0c";
    /**
     * 设备 A 的 mac 地址的属性名
     */
    String FIELD_MAC_A = "aMac";

    /**
     * 设备 B 的 mac 地址的属性名
     */
    String FIELD_MAC_B = "bMac";
    /**
     *水平方位校准时的数据接收指令
     */
    String ORDER_HORIZONTAL_RECEVIER_DATA = "ff03";
    /**
     *垂直方位校准时的数据接收指令
     */
    String ORDER_VERTICAL_RECEVIER_DATA = "ff13";
}
