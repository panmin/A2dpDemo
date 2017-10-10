package com.panmin.a2dpdemo;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for
 * demonstration purposes.
 */
public class SampleGattAttributes {
	private static HashMap<String, String> attributes = new HashMap<String, String>();
	public final static String HEART_RATE_MEASUREMENT = "0000C004-0000-1000-8000-00805f9b34fb";
	public final static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
	public final static String SERIAL_PORT = "0000b350-0000-1000-8000-00805f9b34fb";
	public final static String FOR_SERIAL_PORT_READ = "0000b351-0000-1000-8000-00805f9b34fb"; // 从模块读数据，数据方向，模块到主机
	public final static String SERIAL_PORT_WRITE = "0000b352-0000-1000-8000-00805f9b34fb"; // 向模块写数据，数据方向：主机到模块

	public final static String MAX_PACKET_SIZE = "0000b353-0000-1000-8000-00805f9b34fb"; // 发送数据包的最大有效字节数
	public final static String NO_RESPONSE_MAX_PACKET_COUNT = "0000b354-0000-1000-8000-00805f9b34fb"; // 不确认可以直接发送的数据包个数
	public final static String DEVICE_RECEIVED_PACKET_SEQUENCE = "0000b355-0000-1000-8000-00805f9b34fb"; // 模块接收到的正确数据包的序号
	public final static String HOST_RECEIVED_PACKET_SEQUENCE = "0000b356-0000-1000-8000-00805f9b34fb"; // 主机接收到的正确数据包的序号
	public final static String PACKET_TIMEOUT = "0000b357-0000-1000-8000-00805f9b34fb"; // 数据包应答超时时间,单位秒
	public final static String DEVICE_RECEIVED_ERROR_PACKET_SEQUENCE = "0000b358-0000-1000-8000-00805f9b34fb"; // 模块接收到的错误数据包序号
	public final static String HOST_RECEIVED_ERROR_PACKET_SEQUENCE = "0000b359-0000-1000-8000-00805f9b34fb"; // 主机接收到的错误数据包序号号
	public final static String DEVICE_CAN_RECEIVE_PACKET = "0000b35A-0000-1000-8000-00805f9b34fb"; // 模块给主机的流控信号，非0可以接收数据，0则不能发送数据
	public final static String HOST_CAN_RECEIVE_PACKET = "0000b35B-0000-1000-8000-00805f9b34fb"; // 主机给模块的流控信号，非0可以接收数据，0则不能发送数据
	public final static String RESET_SEQUENCE = "0000b35C-0000-1000-8000-00805f9b34fb";// 复位序号
	static {
		// Sample Services.
		attributes.put("0000fff00000-1000-8000-00805f9b34fb",
				"Heart Rate Service");
		attributes.put("0000180a-0000-1000-8000-00805f9b34fb",
				"Device Information Service");
		// Sample Characteristics.
		attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
		attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
	}

	public static String lookup(String uuid, String defaultName) {
		String name = attributes.get(uuid);
		return name == null ? defaultName : name;
	}
}