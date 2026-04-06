package com.finance.service.record.impl;

import com.finance.dto.CategoryStats;
import com.finance.dto.DashboardResponse;
import com.finance.dto.RecordDto;
import com.finance.dto.RecordRequestDto;
import com.finance.entity.FinancialRecord;
import com.finance.entity.User;
import com.finance.enums.Category;
import com.finance.enums.RecordType;
import com.finance.enums.Role;
import com.finance.exception.BadRequestException;
import com.finance.exception.InternalServerErrorException;
import com.finance.exception.InvalidIdException;
import com.finance.exception.UserNotFoundException;
import com.finance.mapper.RecordMapper;
import com.finance.repo.FinanceRecordRepository;
import com.finance.repo.UserRepository;
import com.finance.service.record.RecordService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordServiceImpl implements RecordService {

    private final FinanceRecordRepository financeRecordRepository;
    private final UserRepository userRepository;
    private final RecordMapper recordMapper;

    public RecordServiceImpl(FinanceRecordRepository financeRecordRepository, UserRepository userRepository, RecordMapper recordMapper) {
        this.financeRecordRepository = financeRecordRepository;
        this.userRepository = userRepository;
        this.recordMapper = recordMapper;
    }

    @Override
    public List<RecordDto> getRecords(int page , int size, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(
                ()-> new UserNotFoundException("user not found"));

        Pageable pageable = PageRequest.of(page,size);

        List<FinancialRecord> records;

        if(user.getRole().equals(Role.ADMIN)){
            records = financeRecordRepository.findAll(pageable).getContent();
        }else{
            records = financeRecordRepository.findByOwner(user,pageable).getContent();
        }
        List<FinancialRecord> filterRecords = records.stream().filter(record -> !record.isDeleted()).toList();

        return recordMapper.toDtoList(filterRecords);

    }

    @Override
    public RecordDto createRecord(RecordRequestDto dto, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(
                () -> new UserNotFoundException("user not found"));

        FinancialRecord record = recordMapper.toEntity(dto);

        User owner = userRepository.findById(dto.getOwnerId()).orElseThrow(
                () -> new UserNotFoundException("owner not found with Id : " + dto.getOwnerId()));

        record.setOwner(owner);
        record.setCreatedBy(user);
        FinancialRecord savedRecord = financeRecordRepository.save(record);
        return recordMapper.toDto(savedRecord);
    }

    @Override
    public RecordDto updateRecord(RecordRequestDto dto, Authentication auth) {
        FinancialRecord record = financeRecordRepository.findById(dto.getId()).orElseThrow(
                () -> new UserNotFoundException("record not found with Id : " + dto.getId()));

        if(record.isDeleted()){
            throw new InvalidIdException("record for Id: " + record.getId() + "is Deleted");
        }

        User user = userRepository.findByEmail(auth.getName()).orElseThrow(
                () -> new UserNotFoundException("user not found"));

        User owner = userRepository.findById(dto.getOwnerId()).orElseThrow(
                () -> new UserNotFoundException("owner not found with Id : " + dto.getOwnerId()));
        FinancialRecord savedRecord;

        try{
            if(user.getRole().equals(Role.ADMIN)){
                record.setAmount(dto.getAmount());
                record.setCategory(Category.valueOf(dto.getCategory().toUpperCase()));
                record.setType(RecordType.valueOf(dto.getType().toUpperCase()));
                record.setNotes(dto.getNotes());
                record.setOwner(owner);
                record.setCreatedBy(user);
                savedRecord = financeRecordRepository.save(record);
            }else{
                throw new BadRequestException("Only Admin Can Modify Records");
            }
        }catch (Exception e){
            throw new InternalServerErrorException("Something went wrong while updating records");
        }
        return recordMapper.toDto(savedRecord);
    }

    @Override
    public String deleteRecord(int recordId, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(
                () -> new UserNotFoundException("user not found"));

        Long id = (long) recordId;
        FinancialRecord record = financeRecordRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("record not found with Id : " + recordId));
        if(record.isDeleted()){
            throw new InvalidIdException("Invalid Id, Record for Id is deleted ");
        }
        record.setDeleted(true);
        financeRecordRepository.save(record);
        return "Record deleted successfully";
    }

    @Override
    public DashboardResponse getDashBoardData(int userId, Authentication auth) {

        User user = userRepository.findByEmail(auth.getName()).orElseThrow(()-> new UserNotFoundException("user not found"));
        User userById = userRepository.findById((long)userId).orElseThrow(()-> new UserNotFoundException("user not found with Id: " + userId));

        if(userById.getId() != user.getId() && !user.getRole().equals(Role.ADMIN)){
            throw new BadRequestException("Only Admin can view Others Records");
        }

        double totalIncome = 0;
        double totalExpense = 0;
        double netBalance = 0;

        List<FinancialRecord> records = financeRecordRepository.getActiveRecordsByOwner(userById.getId());

        Map<String, CategoryStats> result = new HashMap<>();

        for (FinancialRecord r : records) {

            String category = r.getCategory().name();

            result.putIfAbsent(category, new CategoryStats());
            CategoryStats stats = result.get(category);

            if (r.getType() == RecordType.INCOME) {
                stats.setIncome(stats.getIncome() + r.getAmount());
                totalIncome += r.getAmount();
            } else {
                stats.setExpense(stats.getExpense() + r.getAmount());
                totalExpense += r.getAmount();
            }

            stats.setNet(stats.getIncome() - stats.getExpense());
        }
        netBalance = totalIncome - totalExpense;

        if(user.getRole().equals(Role.VIEWER)){
            return new DashboardResponse(totalIncome,totalExpense,netBalance,new HashMap<>());
        }else{
            return new DashboardResponse(totalIncome,totalExpense,netBalance,result);
        }
    }

    @Override
    public DashboardResponse getGlobalData(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(()-> new UserNotFoundException("user not found"));

        if(!user.getRole().equals(Role.ADMIN)){
            throw new BadRequestException("Only Admin can view Others Records and Global Stats");
        }

        double totalIncome = 0;
        double totalExpense = 0;
        double netBalance = 0;

        List<FinancialRecord> records = financeRecordRepository.getAllActiveRecords();

        Map<String, CategoryStats> result = new HashMap<>();

        for (FinancialRecord r : records) {

            String category = r.getCategory().name();

            result.putIfAbsent(category, new CategoryStats());
            CategoryStats stats = result.get(category);

            if (r.getType() == RecordType.INCOME) {
                stats.setIncome(stats.getIncome() + r.getAmount());
                totalIncome += r.getAmount();
            } else {
                stats.setExpense(stats.getExpense() + r.getAmount());
                totalExpense += r.getAmount();
            }

            stats.setNet(stats.getIncome() - stats.getExpense());
        }
        netBalance = totalIncome - totalExpense;

        return new DashboardResponse(totalIncome,totalExpense,netBalance,result);
    }

    @Override
    public List<RecordDto> searchRecords(String type, String category, Double minAmount,Double maxAmount,Authentication auth) {

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() == Role.VIEWER) {
            throw new BadRequestException("Only Admin and Analyst can search");
        }

        Specification<FinancialRecord> spec =
                FinancialRecordSpecification.search(type, category, minAmount, maxAmount);

        if (user.getRole() == Role.ANALYST) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("owner").get("id"), user.getId()));
        }

        return financeRecordRepository.findAll(spec)
                .stream()
                .map(recordMapper::toDto)
                .toList();
    }

}
