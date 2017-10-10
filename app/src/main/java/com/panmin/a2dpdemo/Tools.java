package com.panmin.a2dpdemo;

import android.util.Log;

public class Tools {
	private static final String TAG = "Tools";
	/**
	 * 十六进制转换byte[]
	 *
	 * @param hexStr
	 *            str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return byte[] 对应的byte[]
	 */
	public static byte[] hexStrToStr(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return bytes;
	}
	final private static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String parse(final byte[] data) {
		if (data == null || data.length == 0)
			return "";

		final char[] out = new char[data.length * 3 - 1];
		String outOLd1 = "";
		String outOLd2 = "";
		for (int j = 0; j < data.length; j++) {
			int v = data[j] & 0xFF;
			out[j * 3] = HEX_ARRAY[v >>> 4];
			out[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
			//LogUtil.i(TAG,"byte="+data[j]+"----------"+v+"------------"+out[j * 3]+out[j * 3 + 1]);
			if (j != data.length - 1)
				out[j * 3 + 2] = '-';
			outOLd1 += data[j]+",";
			outOLd2 += v+",";
		}
		Log.i(TAG,"outOLd1="+outOLd1);
		//LogUtil.e(TAG,"outOLd2="+outOLd2);
		//LogUtil.e(TAG,"out="+new String(out));
		return "(0x) " + new String(out);
	}

	/* *
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * @param src byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}

	//把byte类型转换成String类型
	public static String byteToHexString(byte buf) {
		byte[] src = new byte[] { buf };
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString() + " ";
	}

	/**
	 * @method checksum:
	 * @param buffer 要计算校验和的数据指针
	 * @param length 要计算校验和的数据长度
	 * @discussion 计算指定数据的checksum 值
	 */
	public static byte checksum(byte[] buffer, int length) {
		byte nSum;
		nSum = 0;
		for (int i = 0; i < length; i++) {
			nSum += buffer[i];
		}
		return nSum;
	}
}
