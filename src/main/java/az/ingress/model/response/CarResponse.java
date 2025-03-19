package az.ingress.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String fuelType;
    private BigDecimal price;
}
