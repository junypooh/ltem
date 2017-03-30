package com.kt.giga.home.b2b.core.dms.service;

import com.kt.giga.home.b2b.core.dms.domain.DeviceCloseRequest;
import com.kt.giga.home.b2b.core.dms.domain.DeviceOpenRequest;
import com.kt.giga.home.b2b.core.dms.domain.DeviceResponse;

/**
 * Created by 민우 on 2016-12-05.
 */
public interface IDeviceService {

    DeviceResponse openDevice(DeviceOpenRequest deviceOpenRequest);

    DeviceResponse closeDevice(DeviceCloseRequest deviceCloseRequest);

}
