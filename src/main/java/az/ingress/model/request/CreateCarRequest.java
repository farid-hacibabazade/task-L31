package az.ingress.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarRequest {
    private String brand;
    private String model;
    private Integer year;
    private String fuelType;
    private BigDecimal price;
}
