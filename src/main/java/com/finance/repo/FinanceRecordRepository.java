package com.finance.repo;

import com.finance.entity.FinancialRecord;
import com.finance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRecordRepository extends JpaRepository<FinancialRecord,Long>, JpaSpecificationExecutor<FinancialRecord> {
    Page<FinancialRecord> findByOwner(User user, Pageable pageable);

    @Query("""
    SELECT r 
    FROM FinancialRecord r 
    WHERE r.owner.id = :ownerId 
    AND r.isDeleted = false
    """)
    List<FinancialRecord> getActiveRecordsByOwner(Long ownerId);

    @Query("""
    SELECT r 
    FROM FinancialRecord r 
    WHERE r.isDeleted = false
    """)
    List<FinancialRecord> getAllActiveRecords();

}
