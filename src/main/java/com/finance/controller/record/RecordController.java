package com.finance.controller.record;

import com.finance.dto.DashboardResponse;
import com.finance.dto.RecordDto;
import com.finance.dto.RecordRequestDto;
import com.finance.service.record.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Record APIs", description = "Financial record operations")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/record")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    /**
     * Fetch paginated financial records.
     *
     * Access:
     * - ADMIN: All records
     * - ANALYST / VIEWER: Only their own records
     *
     * Notes:
     * - Soft-deleted records are excluded
     *
     * @param page Page number (default = 0)
     * @param size Page size (default = 5)
     */
    @Operation(summary = "Get paginated records")
    @GetMapping("/get-records")
    public ResponseEntity<List<RecordDto>> getRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication auth){
        return ResponseEntity.ok(recordService.getRecords(page, size, auth));
    }

    /**
     * Creates a new financial record.
     *
     * Access:
     * - ADMIN only
     *
     * Notes:
     * - Record can be assigned to any user (owner)
     * - createdBy is set automatically from authenticated user
     */
    @Operation(summary = "Create new financial record (Admin only)")
    @PostMapping("/create-record")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecordDto> createRecord(
            @RequestBody RecordRequestDto dto,
            Authentication auth){
        return ResponseEntity.ok(recordService.createRecord(dto,auth));
    }

    /**
     * Updates an existing record.
     *
     * Access:
     * - ADMIN only
     *
     * Constraints:
     * - Cannot update soft-deleted records
     */
    @Operation(summary = "Update Records")
    @PutMapping("/update-record")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecordDto> updateRecord(
            @RequestBody RecordRequestDto dto,
            Authentication auth){
        return ResponseEntity.ok(recordService.updateRecord(dto, auth));
    }

    /**
     * Soft deletes a record.
     *
     * Access:
     * - ADMIN only
     *
     * Behavior:
     * - Sets isDeleted = true instead of removing record
     */
    @Operation(summary = "Soft Delete Record By Id")
    @PutMapping("/delete-record/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRecord(
            @PathVariable("id") int recordId,
            Authentication auth){
        return ResponseEntity.ok(recordService.deleteRecord(recordId,auth));
    }

    /**
     * Fetch dashboard data for a specific user.
     *
     * Access:
     * - ADMIN: Any user
     * - Others: Only their own data
     */
    @Operation(summary = "Get User Specific Stats")
    @GetMapping("/dashboard-data/{userId}")
    public ResponseEntity<DashboardResponse> getDashBoardData(
            @PathVariable("userId") int userId,
            Authentication auth){
        return ResponseEntity.ok(recordService.getDashBoardData(userId,auth));
    }

    /**
     * Fetch global dashboard data.
     *
     * Access:
     * - ADMIN only
     */
    @Operation(summary = "Get Global Stats (Only Admin)")
    @GetMapping("/dashboard-global-data")
    public ResponseEntity<DashboardResponse> getDashBoardGlobalData(Authentication auth){
        return ResponseEntity.ok(recordService.getGlobalData(auth));
    }

    /**
     * Dynamic search API for financial records.
     *
     * Features:
     * - Supports optional filters
     * - Works even if parameters are missing
     *
     * Access:
     * - ADMIN: All records
     * - ANALYST: Own records only
     * - VIEWER: Not allowed
     */
    @Operation(summary = "Search records with filters")
    @GetMapping("/records/search")
    public ResponseEntity<List<RecordDto>> searchRecords(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            Authentication auth) {

        return ResponseEntity.ok(
                recordService.searchRecords(type, category, minAmount, maxAmount, auth)
        );
    }
}
