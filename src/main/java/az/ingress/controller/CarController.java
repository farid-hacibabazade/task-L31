package az.ingress.controller;


import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.request.CreateCarRequest;
import az.ingress.model.request.UpdatePriceRequest;
import az.ingress.model.response.CarResponse;
import az.ingress.model.response.PageableResponse;
import az.ingress.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("v1/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void saveCar(@RequestBody CreateCarRequest car) {
        carService.saveCar(car);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }

    @PatchMapping("/{id}/price")
    @ResponseStatus(NO_CONTENT)
    public void updateCarPrice(@PathVariable Long id,
                               @RequestBody UpdatePriceRequest request) {
        carService.updateCarPrice(id, request);
    }

    @GetMapping("/{id}")
    public CarResponse getCar(@PathVariable Long id) {
        return carService.getCar(id);
    }

    @GetMapping
    public PageableResponse<CarResponse> getAllCars(PageCriteria pageCriteria) {
        return carService.getAllCars(pageCriteria);
    }
}
