package az.ingress.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class PageCriteria {

    @Min(0)
    private int page;

    @Min(1)
    private int count;
}
