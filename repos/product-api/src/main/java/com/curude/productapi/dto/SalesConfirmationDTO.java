package com.curude.productapi.dto;

import com.curude.productapi.dto.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SalesConfirmationDTO {

    private String salesId;

    private SalesStatus status;


}
