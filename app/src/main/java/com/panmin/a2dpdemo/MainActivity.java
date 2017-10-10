package com.panmin.a2dpdemo;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BluetoothAdapter mBTAdapter;
    private BluetoothA2dp mBTA2DP;
    private BluetoothHeadset mHeadset;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {


        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if(profile==BluetoothProfile.A2DP) {
                mBTA2DP = (BluetoothA2dp) proxy;
            }else if(profile == BluetoothProfile.HEADSET){
                mHeadset = (BluetoothHeadset) proxy;
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {

        }
    };

    private EditText et_ble_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_ble_data = (EditText) findViewById(R.id.et_ble_data);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(mReceiver,intentFilter);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTAdapter.getProfileProxy(this, mProfileServiceListener, BluetoothProfile.A2DP);
        mBTAdapter.getProfileProxy(this, mProfileServiceListener, BluetoothProfile.HEADSET);

        final String macAddress = "00:1B:35:12:22:59";
        //配对
        findViewById(R.id.btn_pair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
                Log.i(TAG, "remoteDevice deviceName=" + remoteDevice.getName());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    remoteDevice.createBond();
                }else {
                    try {
                        Method createBondMethod =BluetoothDevice.class.getMethod("createBond");
                        createBondMethod.invoke(remoteDevice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        //取消配对
        findViewById(R.id.btn_remove_pair).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
                Boolean returnValue = null;
                try {
                    Log.i(TAG, "remoteDevice deviceName=" + remoteDevice.getName());
                    Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");

                    returnValue = (Boolean) removeBondMethod.invoke(remoteDevice);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "removeBond=" + returnValue.booleanValue());
            }
        });
        //a2dp连接
        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
                if(mBTA2DP.getConnectionState(remoteDevice)!= BluetoothProfile.STATE_CONNECTED){
                    try {
                        mBTA2DP.getClass().getMethod("connect", BluetoothDevice.class)
                                .invoke(mBTA2DP, remoteDevice);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //断开a2dp连接
        findViewById(R.id.btn_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
                if(mBTA2DP.getConnectionState(remoteDevice)== BluetoothProfile.STATE_CONNECTED){
                    try {
                        mBTA2DP.getClass().getMethod("disconnect", BluetoothDevice.class)
                                .invoke(mBTA2DP, remoteDevice);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        //hfp连接
        findViewById(R.id.btn_HFP_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
                if(mHeadset.getConnectionState(remoteDevice)!= BluetoothProfile.STATE_CONNECTED){
                    try {
                        mHeadset.getClass().getMethod("connect", BluetoothDevice.class)
                                .invoke(mHeadset, remoteDevice);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //断开hfp连接
        findViewById(R.id.btn_HFP_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
                if(mHeadset.getConnectionState(remoteDevice)== BluetoothProfile.STATE_CONNECTED){
                    try {
                        mHeadset.getClass().getMethod("disconnect", BluetoothDevice.class)
                                .invoke(mHeadset, remoteDevice);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //ble连接
        findViewById(R.id.btn_ble_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMacAddress = macAddress.replace("1B","1C");
                BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(newMacAddress);
                mBluetoothGatt = remoteDevice.connectGatt(MainActivity.this,false,gattCallback);
            }
        });
        //ble写入数据
        findViewById(R.id.btn_ble_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ble_data = et_ble_data.getText()+"";
                if(mWriteCharacteristic!=null) {
                    byte[] bytes = ble_data.getBytes();
                    mWriteCharacteristic.setValue(bytes);
                    boolean b = mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
                    Log.i(TAG, "ble写入数据:"+b);
                }else {
                    Log.e(TAG, "mWriteCharacteristic is null ");
                }
            }
        });
        //断开ble连接
        findViewById(R.id.btn_ble_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothGatt.disconnect();
            }
        });
    }

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.i(TAG, "onConnectionStateChange: ");
            Log.i(TAG, "=====status:" + status + "====newState:" + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                boolean bo = mBluetoothGatt.discoverServices();
                Log.i(TAG,"BLE连接成功,Attempting to start service discovery:" + bo);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {   //newState == 0
                Log.i(TAG,"BLE连接断开");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i(TAG, "onServicesDiscovered: ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onServicesDiscovered: 搜索服务成功");
                getGattServices(mBluetoothGatt.getServices());
            } else {
                Log.i(TAG,"搜索服务失败，onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i(TAG, "onCharacteristicRead: ");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i(TAG, "onCharacteristicWrite: ");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i(TAG, "onCharacteristicChanged: ");
            whichChanged(characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.i(TAG, "onDescriptorRead: ");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.i(TAG, "onDescriptorWrite: ");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.i(TAG, "onReliableWriteCompleted: ");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.i(TAG, "onReadRemoteRssi: ");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.i(TAG, "onMtuChanged: ");
        }
    };

    private void whichChanged(BluetoothGattCharacteristic characteristic) {
        byte[] value = characteristic.getValue();
        String UUIDStr = characteristic.getUuid().toString();
        Log.i(TAG,"处理接收到的characteristic UUID = " +UUIDStr);
        final String data = Tools.parse(value);
        Log.i(TAG,"DATA = " + data);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,data,Toast.LENGTH_SHORT).show();
            }
        });

        /*if (UUIDStr.equals(SampleGattAttributes.FOR_SERIAL_PORT_READ)) {  //b351发送缓冲
            //从模块读数据，数据方向，模块到主机
            if (value != null) {
                recvDataFromPeer(value);
            } else {
                Log.i(TAG,"characteristic.getValue()== null");
            }
        } else if (UUIDStr.equals(SampleGattAttributes.MAX_PACKET_SIZE)) {  //b353 最大数据包字节数
            // 发送数据包的最大有效字节数
            if (value != null) {
                //nMaxPacketSize = value[0];
            } else {
                Log.i(TAG,"characteristic.getValue()== null");
            }
        } else if (UUIDStr.equals(SampleGattAttributes.NO_RESPONSE_MAX_PACKET_COUNT)) {
            //b354 不确认可以直接发送的数据包个数，发送超过这个数之后，必须等待确认
            // 不确认可以直接发送的数据包个数
            if (value != null) {
                //nNoResponseAllowMaxPacketCount = value[0];
            }
        } else if (UUIDStr.equals(SampleGattAttributes.PACKET_TIMEOUT)) {
            //b357 超时时间，单位秒。当发送一个数据包之后，在超时时间之后如果还没有收到应答，则我们认为数据丢失了
            // 数据包应答超时时间,单位秒
            if (value != null) {
                Log.i(TAG,"数据包应答超时时间=="+value[0]);
            } else {
                Log.i(TAG,"characteristic.getValue()== null");
            }
        } else if (UUIDStr.equals(SampleGattAttributes.DEVICE_RECEIVED_PACKET_SEQUENCE)) {
            //b355 模块接收到的正确数据包的序号，模块正确接收到数据包之后，通过这个特征告知Host，已经接收到这个序号了
            // 模块接收到的正确数据包的序号
            if (value != null) {
                //Log.i("模块接收成功 序号 " + Tools.byteToHexString(value[0]));//add by mr at 20150529
                if(state==1)
                { //state 发送数据方式 0速率测试发送 1正常发送数据
                    //del by mr at 20150601
                    recvDataPackeSequenced(value[0], 5);
                }

            } else {
                Log.i(TAG,"characteristic.getValue()== null");
            }
        } else if (UUIDStr.equals(SampleGattAttributes.DEVICE_RECEIVED_ERROR_PACKET_SEQUENCE)) {
            //b358模块接收到的错误
            // 模块接收到的错误数据包序号
            if (value != null) {
                //Log.i("模块接收失败 序号 " + Tools.byteToHexString(value[0]));
                if(state==1)
                { //state 发送数据方式 0速率测试发送 1正常发送数据
                    recvDataPackeSequenced(value[0], 8);
                }
            } else {
                Log.i(TAG,"characteristic.getValue()== null");
            }
        } else if (UUIDStr.equals(SampleGattAttributes.DEVICE_CAN_RECEIVE_PACKET)) {
            //b35A 模块给主机的流控信号，当值为非0时，主机可以发送数据，为0时，主机不能给模块发送数据。
            // 模块给主机的流控信号，非0可以接收数据，0则不能发送数据
            //reopen by mr at 20150602之前没有使用
            if (value != null) {
                byte flag = value[0];
                if (flag == 0) {
                    bPeerCanReceive = false;
                    Log.i(TAG,"------Device Can not Receive------");
                } else {
                    bPeerCanReceive = true;
                }
            } else {
                bPeerCanReceive = true;
            }
        } else if (UUIDStr.equals(SampleGattAttributes.RESET_SEQUENCE)) {// 复位序号 b35C
            Log.i(TAG,"RESET_SEQUENCE --> " + SampleGattAttributes.RESET_SEQUENCE);
        }*/
    }
    private BluetoothGattCharacteristic mWriteCharacteristic;

    private void getGattServices(List<BluetoothGattService> gattServices) {
        ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        if (gattServices == null) {
            return;
        }
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        String uuid = null;
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            Log.d(TAG,"Service--->"+uuid);
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                Log.d(TAG,"gattCharacteristic--->"+gattCharacteristic.getUuid().toString());
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        setCharacteristicNotificationAndReadValue(mGattCharacteristics);
    }

    private void setCharacteristicNotificationAndReadValue(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics2) {
        for (int i = 0; i < mGattCharacteristics2.size(); i++) {
            for (int j = 0; j < mGattCharacteristics2.get(i).size(); j++) {
                final BluetoothGattCharacteristic characteristic = mGattCharacteristics2.get(i).get(j);
                String uuid = characteristic.getUuid().toString();
                if (uuid.equals(SampleGattAttributes.FOR_SERIAL_PORT_READ)) {
                    //0000b351
                    //FOR_SERIAL_PORT_READ_Characteristic = characteristic;
                    setCharacteristicNotification(characteristic, true);
                } else if (uuid.equals(SampleGattAttributes.SERIAL_PORT_WRITE)) {
                    //0000b352
                    //SERIAL_PORT_WRITE_Characteristic = characteristic;

                    //add by mr at 20150608
                    mWriteCharacteristic = characteristic;
                    mWriteCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                } else if (uuid.equals(SampleGattAttributes.MAX_PACKET_SIZE)) {
                    //0000b353
                    //MAX_PACKET_SIZE_Characteristic = characteristic;
                } else if (uuid.equals(SampleGattAttributes.NO_RESPONSE_MAX_PACKET_COUNT)) {
                    //0000b354
                    //NO_RESPONSE_MAX_PACKET_COUNT_Characteristic = characteristic;
                } else if (uuid.equals(SampleGattAttributes.DEVICE_RECEIVED_PACKET_SEQUENCE)) {
                    //0000b355
                    //DEVICE_RECEIVED_PACKET_SEQUENCE_Characteristic = characteristic;
                    setCharacteristicNotification(characteristic, true);
                } else if (uuid.equals(SampleGattAttributes.HOST_RECEIVED_PACKET_SEQUENCE)) {
                    //0000b356
                    //HOST_RECEIVED_PACKET_SEQUENCE_Characteristic = characteristic;
                } else if (uuid.equals(SampleGattAttributes.PACKET_TIMEOUT)) {
                    //0000b357
                    //PACKET_TIMEOUT_Characteristic = characteristic;
                } else if (uuid.equals(SampleGattAttributes.DEVICE_RECEIVED_ERROR_PACKET_SEQUENCE)) {
                    //0000b358
                    //DEVICE_RECEIVED_ERROR_PACKET_SEQUENCE_Characteristic = characteristic;
                    setCharacteristicNotification(characteristic, true);
                } else if (uuid.equals(SampleGattAttributes.HOST_RECEIVED_ERROR_PACKET_SEQUENCE)) {
                    //0000b359
                    //HOST_RECEIVED_ERROR_PACKET_SEQUENCE_Characteristic = characteristic;
                } else if (uuid.equals(SampleGattAttributes.DEVICE_CAN_RECEIVE_PACKET)) {
                    //0000b35A
                    //DEVICE_CAN_RECEIVE_PACKET_Characteristic = characteristic;
                    setCharacteristicNotification(characteristic, true);
                } else if (uuid.equals(SampleGattAttributes.HOST_CAN_RECEIVE_PACKET)) {
                    //0000b35B
                    //HOST_CAN_RECEIVE_PACKET_Characteristic = characteristic;
                } else if (uuid.equals(SampleGattAttributes.RESET_SEQUENCE)) {
                    //0000b35C
                    //RESET_SEQUENCE_Characteristic = characteristic;
                }
            }
        }

        //serviceHnadler.postDelayed(readCharacteristicTimeoutRunnable, DEFAULT_READ_CHARACTERISTIC__TIMEOUT * 1000);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBTAdapter == null || mBluetoothGatt == null) {
            Log.i(TAG,"BluetoothAdapter not initialized");
            return;
        }
        boolean ba = mBluetoothGatt.setCharacteristicNotification(characteristic,enabled);
        Log.i(TAG,"setCharacteristicNotification for --> "+characteristic.getUuid().toString()
                + " to --> " + enabled+ " result --> " + ba);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(characteristic.getUuid());
        if (descriptor != null) {
            Log.i(TAG,"write descriptor uuid -->"+ characteristic.getUuid().toString());
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }


   private BroadcastReceiver mReceiver = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {
           String action = intent.getAction();
           if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {//配对
               BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               int bondState = device.getBondState();
               if (bondState == BluetoothDevice.BOND_BONDING) {
                   Toast.makeText(MainActivity.this, "绑定中。。。", Toast.LENGTH_SHORT).show();
                   Log.i(TAG, "onReceive: 绑定中。。。");
               }else if(bondState == BluetoothDevice.BOND_BONDED) {
                   Toast.makeText(MainActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                   Log.i(TAG, "onReceive: 绑定成功");
               }else if(bondState == BluetoothDevice.BOND_NONE) {
                   Toast.makeText(MainActivity.this, "取消绑定", Toast.LENGTH_SHORT).show();
                   Log.i(TAG, "onReceive: 取消绑定");
               }
           }else if(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED.equals(action)){//A2DP
               BluetoothDevice device;
               switch (intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1)) {
                   case BluetoothA2dp.STATE_CONNECTING:
                       Toast.makeText(MainActivity.this, "a2dp连接中。。。", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " a2dp连接中。。。");
                       break;
                   case BluetoothA2dp.STATE_CONNECTED:
                       Toast.makeText(MainActivity.this, "a2dp连接成功", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " a2dp连接成功");
                       break;
                   case BluetoothA2dp.STATE_DISCONNECTING:
                       Toast.makeText(MainActivity.this, "a2dp断开连接中。。。", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " a2dp断开连接中。。。");
                       break;
                   case BluetoothA2dp.STATE_DISCONNECTED:
                       Toast.makeText(MainActivity.this, "a2dp断开连接成功", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " a2dp断开连接成功");
                       break;
                   default:
                       break;
               }
           }else if(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)){//hfp
               BluetoothDevice device;
               switch (intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, -1)) {
                   case BluetoothHeadset.STATE_CONNECTING:
                       Toast.makeText(MainActivity.this, "hfp连接中。。。", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " hfp连接中。。。");
                       break;
                   case BluetoothHeadset.STATE_CONNECTED:
                       Toast.makeText(MainActivity.this, "hfp连接成功", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " hfp连接成功");
                       break;
                   case BluetoothHeadset.STATE_DISCONNECTING:
                       Toast.makeText(MainActivity.this, "hfp断开连接中。。。", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " hfp断开连接中。。。");
                       break;
                   case BluetoothHeadset.STATE_DISCONNECTED:
                       Toast.makeText(MainActivity.this, "hfp断开连接成功", Toast.LENGTH_SHORT).show();
                       device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                       Log.i(TAG, "device: " + device.getName() + " hfp断开连接成功");
                       break;
                   default:
                       break;
               }
           }
       }
   };

}
