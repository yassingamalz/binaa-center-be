package com.novavista.binaa.center.mapper;

import com.novavista.binaa.center.dto.request.PaymentDTO;
import com.novavista.binaa.center.dto.response.PaymentResponseDTO;
import com.novavista.binaa.center.entity.Payment;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper implements EntityMapper<PaymentDTO, Payment> {
    private final ModelMapper mapper;

    @Autowired
    public PaymentMapper(ModelMapper mapper) {
        this.mapper = mapper;

        // Custom converter for payment response mapping
        Converter<Payment, PaymentResponseDTO> paymentToResponseDtoConverter = context -> {
            Payment source = context.getSource();
            PaymentResponseDTO destination = new PaymentResponseDTO();

            // Map all base payment fields
            destination.setPaymentId(source.getPaymentId());
            destination.setCaseId(source.getCaseInfo().getCaseId());
            destination.setSessionId(source.getSession() != null ? source.getSession().getSessionId() : null);
            destination.setAmount(source.getAmount());
            destination.setPaymentDate(source.getPaymentDate());
            destination.setPaymentMethod(source.getPaymentMethod());
            destination.setInvoiceNumber(source.getInvoiceNumber());
            destination.setPaymentStatus(source.getPaymentStatus());

            // Add case name for response DTO
            if (source.getCaseInfo() != null) {
                destination.setCaseName(source.getCaseInfo().getName());
            }

            return destination;
        };

        // Create type map with custom converter
        mapper.createTypeMap(Payment.class, PaymentResponseDTO.class)
                .setConverter(paymentToResponseDtoConverter);
    }

    @Override
    public PaymentDTO toDto(Payment entity) {
        return mapper.map(entity, PaymentDTO.class);
    }

    @Override
    public Payment toEntity(PaymentDTO dto) {
        return mapper.map(dto, Payment.class);
    }

    public PaymentResponseDTO toResponseDto(Payment entity) {
        return mapper.map(entity, PaymentResponseDTO.class);
    }

    @Override
    public List<PaymentDTO> toDtoList(List<Payment> entityList) {
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PaymentResponseDTO> toResponseDtoList(List<Payment> entityList) {
        return entityList.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> toEntityList(List<PaymentDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}