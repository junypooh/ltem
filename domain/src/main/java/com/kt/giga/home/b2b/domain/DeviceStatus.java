package com.kt.giga.home.b2b.domain;

import com.google.common.base.MoreObjects;

/**
 * Created by cecil on 2017. 1. 14..
 * <pre>
 * com.kt.giga.home.b2b.entity.DeviceStatus
 *
 * 장치 상태
 *
 * @author Sung-Bum, Lee
 *
 * </pre>
 */
public enum DeviceStatus {

    /**
     * 계약 정보 수신
     */
    CONTRACT_RECEIVED((short) 10, "계약 정보 수신"),
    /**
     * 기기 인증 정보 수신
     */
    PROVISION_RECEIVED((short) 20, "기기 인증 정보 수신"),
    /**
     * API로 기기 인증 정보 전송 완료
     */
    PROVISION_COMPLETED((short) 30, "기기 인증 정보 전송 완료"),
    /**
     * 만료 수신
     */
    EXPIRATION_RECEIVED((short) 40, "만료 해지 수신"),
    /**
     * 해지 완료
     */
    EXPIRED((short) 50, "해지 완료");

    private Short statusCd;
    private String description;

    public Short getStatusCd() {
        return statusCd;
    }

    public String getDescription() {
        return description;
    }

    DeviceStatus(short statusCd, String description) {
        this.statusCd = statusCd;
        this.description = description;
    }

    public static DeviceStatus getStatus(Short statusCd) {
        for (DeviceStatus deviceStatus : values()) {
            if (deviceStatus.statusCd.equals(statusCd))
                return deviceStatus;
        }

        throw new IllegalArgumentException("No matching device status for status code [" + statusCd + "] found.");

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("name", name())
                          .add("statusCd", statusCd)
                          .add("description", description)
                          .toString();
    }
}
