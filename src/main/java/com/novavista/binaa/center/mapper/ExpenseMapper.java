package com.novavista.binaa.center.mapper;

import com.novavista.binaa.center.dto.request.ExpenseDTO;
import com.novavista.binaa.center.dto.response.ExpenseResponseDTO;
import com.novavista.binaa.center.entity.Expense;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpenseMapper implements EntityMapper<ExpenseDTO, Expense> {
    private final ModelMapper mapper;

    @Autowired
    public ExpenseMapper(ModelMapper mapper) {
        this.mapper = mapper;

        // Custom converter for staff mapping
        Converter<Expense, ExpenseResponseDTO> expenseToResponseDtoConverter = context -> {
            Expense source = context.getSource();
            ExpenseResponseDTO destination = new ExpenseResponseDTO();

            // Manually map all fields from source to destination
            destination.setExpenseId(source.getExpenseId());
            destination.setCategory(source.getCategory());
            destination.setAmount(source.getAmount());
            destination.setDate(source.getDate());
            destination.setDescription(source.getDescription());
            destination.setReceiptPath(source.getReceiptPath());
            destination.setPaymentMethod(source.getPaymentMethod());

            // Handle staff mapping
            if (source.getPaidBy() != null) {
                destination.setPaidBy(source.getPaidBy().getStaffId());
                destination.setStaffName(source.getPaidBy().getName());
            }

            return destination;
        };

        // Create type map with custom converter
        mapper.createTypeMap(Expense.class, ExpenseResponseDTO.class)
                .setConverter(expenseToResponseDtoConverter);
    }

    @Override
    public ExpenseDTO toDto(Expense entity) {
        return mapper.map(entity, ExpenseDTO.class);
    }

    @Override
    public Expense toEntity(ExpenseDTO dto) {
        return mapper.map(dto, Expense.class);
    }

    public ExpenseResponseDTO toResponseDto(Expense entity) {
        return mapper.map(entity, ExpenseResponseDTO.class);
    }

    @Override
    public List<ExpenseDTO> toDtoList(List<Expense> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponseDTO> toResponseDtoList(List<Expense> entityList) {
        return entityList.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> toEntityList(List<ExpenseDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}