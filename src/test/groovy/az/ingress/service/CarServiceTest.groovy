package az.ingress.service

import az.ingress.dao.entity.CarEntity
import az.ingress.dao.repository.CarRepository
import az.ingress.exception.NotFoundException
import az.ingress.model.criteria.PageCriteria
import az.ingress.model.request.CreateCarRequest
import az.ingress.model.request.UpdatePriceRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import static az.ingress.model.enums.CarStatus.SOLD

class CarServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    CarRepository carRepository
    CarServiceHandler carService

    def setup() {
        carRepository = Mock()
        carService = new CarServiceHandler(carRepository)
    }

    def "TestSaveCar success case"() {
        given:
        def request = random.nextObject(CreateCarRequest)

        when:
        carService.saveCar(request)

        then:
        1 * carRepository.save(_ as CarEntity)
    }

    def "TestDeleteCar success case"() {
        given:
        def id = random.nextLong()
        def entity = random.nextObject(CarEntity)

        when:
        carService.deleteCar(id)

        then:
        1 * carRepository.findById(id) >> Optional.of(entity)
        entity.status == SOLD
        1 * carRepository.save(entity)
    }

    def "TestDeleteCar CarNotFound case"() {
        given:
        def id = random.nextLong()

        when:
        carService.deleteCar(id)

        then:
        1 * carRepository.findById(id) >> Optional.empty()

        NotFoundException ex = thrown()
        ex.message == "Car with id:$id not found"
        ex.code == "CAR_NOT_FOUND"
    }

    def "TestUpdateCarPrice success case"() {
        given:
        def id = random.nextLong()
        def request = random.nextObject(UpdatePriceRequest)
        def entity = random.nextObject(CarEntity)

        when:
        carService.updateCarPrice(id, request)

        then:
        1 * carRepository.findById(id) >> Optional.of(entity)
        entity.price == request.price
        1 * carRepository.save(entity)
    }

    def "TestUpdateCarPrice CarNotFound case"() {
        given:
        def id = random.nextLong()
        def request = random.nextObject(UpdatePriceRequest)

        when:
        carService.updateCarPrice(id, request)

        then:
        1 * carRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "Car with id:$id not found"
        ex.code == "CAR_NOT_FOUND"
    }

    def "TestGetCar success case"() {
        given:
        def id = random.nextLong()
        def entity = random.nextObject(CarEntity)

        when:
        def response = carService.getCar(id)

        then:
        1 * carRepository.findById(id) >> Optional.of(entity)
        verifyAll {
            response.id == entity.id
            response.brand == entity.brand
            response.model == entity.model
            response.year == entity.year
            response.fuelType == entity.fuelType
            response.price == entity.price
        }
    }

    def "TestGetCar CarNotFound case"() {
        given:
        def id = random.nextLong()

        when:
        carService.getCar(id)

        then:
        1 * carRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "Car with id:$id not found"
        ex.code == "CAR_NOT_FOUND"
    }

    def "TestGetAllCars success case"() {
        given:
        def pageNumber = 0
        def pageSize = 10
        def totalElements = 1L
        def pageCriteria = PageCriteria.of(pageNumber, pageSize)
        def pageRequest = PageRequest.of(pageNumber, pageSize)
        def entity = random.nextObject(CarEntity)
        def carEntityPage = new PageImpl([entity], pageRequest, totalElements)

        when:
        def pageableResponse = carService.getAllCars(pageCriteria)

        then:
        1 * carRepository.findAll(pageRequest) >> carEntityPage
        verifyAll {
            entity.id == pageableResponse.content[pageNumber].id
            entity.brand == pageableResponse.content[pageNumber].brand
            entity.model == pageableResponse.content[pageNumber].model
            entity.price == pageableResponse.content[pageNumber].price
            ++pageNumber == pageableResponse.pageCount
            totalElements == pageableResponse.totalElements
            !pageableResponse.hasNextPage
        }
    }
}
