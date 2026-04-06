package com.finance.service.record;

import com.finance.dto.CategoryStats;
import com.finance.dto.DashboardResponse;
import com.finance.dto.RecordDto;
import com.finance.dto.RecordRequestDto;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface RecordService {
    List<RecordDto> getRecords(int page, int size, Authentication auth);

    RecordDto createRecord(RecordRequestDto dto, Authentication auth);

    RecordDto updateRecord(RecordRequestDto dto, Authentication auth);

    String deleteRecord(int recordId, Authentication auth);

    DashboardResponse getDashBoardData(int userId, Authentication auth);

    DashboardResponse getGlobalData(Authentication auth);

    List<RecordDto> searchRecords(String type, String category, Double minAmount, Double maxAmount, Authentication auth);
}
