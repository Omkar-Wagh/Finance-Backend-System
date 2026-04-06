package com.finance.mapper;

import com.finance.dto.RecordDto;
import com.finance.dto.RecordRequestDto;
import com.finance.entity.FinancialRecord;
import com.finance.enums.Category;
import com.finance.enums.RecordType;
import com.finance.exception.BadRequestException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RecordMapper {
    @Mapping(target = "type", source = "type", qualifiedByName = "typeStringToEnum")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryStringToEnum")
    FinancialRecord toEntity(RecordRequestDto dto);

    @Mapping(target = "type", source = "type", qualifiedByName = "typeEnumToString")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryEnumToString")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerName", source = "owner.name")

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.name")

    @Mapping(target = "deleted", source = "deleted")
    RecordDto toDto(FinancialRecord record);

    List<RecordDto> toDtoList(List<FinancialRecord> records);

    @Named("typeStringToEnum")
    static RecordType mapTypeToEnum(String type) {
        if (type == null || type.isBlank()) {
            throw new BadRequestException("Record type is required");
        }
        try {
            return RecordType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid record type: " + type);
        }
    }
    @Named("categoryStringToEnum")
    static Category mapCategoryToEnum(String category) {
        if (category == null || category.isBlank()) {
            throw new BadRequestException("Category is required");
        }
        try {
            return Category.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid category: " + category);
        }
    }

    @Named("typeEnumToString")
    static String mapTypeToString(RecordType type) {
        return (type == null) ? null : type.name();
    }
    @Named("categoryEnumToString")
    static String mapCategoryToString(Category category) {
        return (category == null) ? null : category.name();
    }
}
