package az.ingress.mapper

import az.ingress.dao.entity.CarEntity
import az.ingress.model.enums.CarStatus
import az.ingress.model.request.CreateCarRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.CarMapper.CAR_MAPPER

class CarMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    CarMapper carMapper

    def setup() {
        carMapper = CAR_MAPPER
    }

    def "TestMapEntityToResponse"() {
        given:
        def entity = random.nextObject(CarEntity)

        when:
        def response = CAR_MAPPER.mapEntityToResponse(entity)

        then:
        verifyAll(response) {
            id == entity.id
            brand == entity.brand
            model == entity.model
            year == entity.year
            fuelType == entity.fuelType
            price == entity.price
        }
    }

    def "TestMapRequestToEntity"() {
        given:
        def request = random.nextObject(CreateCarRequest)

        when:
        def entity = CAR_MAPPER.mapRequestToEntity(request)

        then:
        verifyAll(entity) {
            brand == request.brand
            model == request.model
            year == request.year
            fuelType == request.fuelType
            price == request.price
            status == CarStatus.ACTIVE
        }
    }

}
