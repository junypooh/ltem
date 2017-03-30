package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.HistoryInfo;
import com.kt.giga.home.b2b.entity.PrivacyAuditHistory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by DaDa on 2017-03-24.
 */
public abstract class HistoryMapperDecorator implements HistoryMapper {

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public HistoryInfo toHistoryInfo(PrivacyAuditHistory privacyAuditHistory) {
        HistoryInfo historyInfo = historyMapper.toHistoryInfo(privacyAuditHistory);

        if(StringUtils.trimToNull(historyInfo.getBizContSerial()) == null) {
            historyInfo.setBizContSerial("-");
        }

        if (historyInfo.getUnmasked()) {
            historyInfo.setWorkContext("마스킹 해제");
            if (StringUtils.trimToNull(historyInfo.getReason()) != null) {
                historyInfo.setReason("해제사유: " + historyInfo.getReason());
            }
        } else if(historyInfo.getDownloaded()) {
            historyInfo.setWorkContext("다운로드");
        }

        historyInfo.setWorkHistoryDate(historyInfo.getAudited().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return historyInfo;
    }
}
