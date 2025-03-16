package az.ingress.mapper

import az.ingress.dao.entity.CarEntity
import az.ingress.model.response.CarResponse
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.util.function.Function

import static az.ingress.mapper.CarMapper.CAR_MAPPER
import static az.ingress.mapper.PageableMapper.PAGEABLE_MAPPER

class PageableMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    PageableMapper pageableMapper

    def setup() {
        pageableMapper = PAGEABLE_MAPPER
    }

    def "TestPageableMapper"() {
        given:
        def page = 0
        def count = 10
        def total = 1L
        def pageable = PageRequest.of(page, count)
        def entity = random.nextObject(CarEntity)
        def pageOfCar = new PageImpl([entity], pageable, total)
        def mapper = {
            CarEntity car ->
                CarResponse.builder()
                        .id(car.getId())
                        .brand(car.getBrand())
                        .model(car.getModel())
                        .year(car.getYear())
                        .fuelType(car.getFuelType())
                        .price(car.getPrice())
                        .build()
        }

        when:
        def pageableResponse = PAGEABLE_MAPPER.buildPageableResponse(pageOfCar, mapper)

        then:
        verifyAll {
            entity.id == pageableResponse.content[page].id
            entity.brand == pageableResponse.content[page].brand
            entity.model == pageableResponse.content[page].model
            entity.year == pageableResponse.content[page].year
            entity.fuelType == pageableResponse.content[page].fuelType
            entity.price == pageableResponse.content[page].price
        }

    }
}
