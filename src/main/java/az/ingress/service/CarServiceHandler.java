package az.ingress.service;


import az.ingress.dao.entity.CarEntity;
import az.ingress.dao.repository.CarRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.enums.CarStatus;
import az.ingress.model.request.CreateCarRequest;
import az.ingress.model.request.UpdatePriceRequest;
import az.ingress.model.response.CarResponse;
import az.ingress.model.response.PageableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static az.ingress.exception.ExceptionMessage.CAR_NOT_FOUND_CODE;
import static az.ingress.exception.ExceptionMessage.CAR_NOT_FOUND_MESSAGE;
import static az.ingress.mapper.CarMapper.CAR_MAPPER;
import static az.ingress.mapper.PageableMapper.PAGEABLE_MAPPER;


@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceHandler implements CarService {
    private final CarRepository carRepository;

    @Override
    public void saveCar(CreateCarRequest car) {
        carRepository.save(CAR_MAPPER.mapRequestToEntity(car));
    }

    @Override
    public void deleteCar(Long id) {
        var car = fetchCarIfExits(id);
        car.setStatus(CarStatus.SOLD);
        carRepository.save(car);
    }

    @Override
    public void updateCarPrice(Long id, UpdatePriceRequest request) {
        var car = fetchCarIfExits(id);
        car.setPrice(request.getPrice());
        carRepository.save(car);
    }

    @Override
    public CarResponse getCar(Long id) {
        var car = fetchCarIfExits(id);
        return CAR_MAPPER.mapEntityToResponse(car);
    }

    @Override
    public PageableResponse<CarResponse> getAllCars(PageCriteria pageCriteria) {
        var pageRequest = PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount());
        var carEntityPage = carRepository.findAll(pageRequest);
        return PAGEABLE_MAPPER.buildPageableResponse(carEntityPage, CAR_MAPPER::mapEntityToResponse);
    }

    private CarEntity fetchCarIfExits(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ActionLog.fetchCarIfExits.error post with id:{} not found", id);
                    return new NotFoundException(
                            String.format(CAR_NOT_FOUND_MESSAGE.getMessage(), id),
                            CAR_NOT_FOUND_CODE.getMessage());
                });
    }
}
