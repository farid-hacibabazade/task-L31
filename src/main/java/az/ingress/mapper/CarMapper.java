package az.ingress.mapper;


import az.ingress.dao.entity.CarEntity;
import az.ingress.model.enums.CarStatus;
import az.ingress.model.request.CreateCarRequest;
import az.ingress.model.response.CarResponse;

public enum CarMapper {
    CAR_MAPPER;

    public CarResponse mapEntityToResponse(CarEntity car) {
        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .year(car.getYear())
                .fuelType(car.getFuelType())
                .price(car.getPrice())
                .build();
    }

    public CarEntity mapRequestToEntity(CreateCarRequest car) {
        return CarEntity.builder()
                .brand(car.getBrand())
                .model(car.getModel())
                .year(car.getYear())
                .fuelType(car.getFuelType())
                .price(car.getPrice())
                .status(CarStatus.ACTIVE)
                .build();
    }
}
