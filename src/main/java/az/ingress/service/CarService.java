package az.ingress.service;




import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.request.CreateCarRequest;
import az.ingress.model.request.UpdatePriceRequest;
import az.ingress.model.response.CarResponse;
import az.ingress.model.response.PageableResponse;

public interface CarService {

    void saveCar(CreateCarRequest car);

    void deleteCar(Long id);

    void updateCarPrice(Long id, UpdatePriceRequest request);

    CarResponse getCar(Long id);

    PageableResponse<CarResponse> getAllCars(PageCriteria pageCriteria);
}
