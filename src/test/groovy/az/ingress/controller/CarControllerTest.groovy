package az.ingress.controller

import az.ingress.exception.ErrorHandler
import az.ingress.model.criteria.PageCriteria
import az.ingress.model.request.CreateCarRequest
import az.ingress.model.request.UpdatePriceRequest
import az.ingress.model.response.CarResponse
import az.ingress.model.response.PageableResponse
import az.ingress.service.CarService
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class CarControllerTest extends Specification {
    CarService carService
    CarController carController
    MockMvc mockMvc
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def setup() {
        carService = Mock()
        carController = new CarController(carService)
        mockMvc = MockMvcBuilders.standaloneSetup(carController)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def "TestSaveCar success case"() {
        given:
        def request = random.nextObject(CreateCarRequest)
        def url = "/v1/cars"

        def requestBody = """
        {
                "brand": "$request.brand",
                "model": "$request.model",
                "year":   $request.year,
                "fuelType": "$request.fuelType",
                "price":  $request.price
        }
        """

        when:
        def result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andReturn()

        then:
        1 * carService.saveCar(request)

        result.response.status == HttpStatus.CREATED.value()
    }

    def "TestDeleteCar success case"() {
        given:
        def id = random.nextLong()
        def url = "/v1/cars/$id"

        when:
        def result = mockMvc.perform(
                delete(url).contentType(MediaType.APPLICATION_JSON)
        ).andReturn()

        then:
        1 * carService.deleteCar(id)

        result.response.status == HttpStatus.NO_CONTENT.value()
    }

    def "TestUpdateCarPrice success case"() {
        given:
        def id = random.nextLong()
        def url = "/v1/cars/$id/price"
        def request = random.nextObject(UpdatePriceRequest)

        def requestBody = """
            {
                 "price": $request.price
            }
        """

        when:
        def result = mockMvc.perform(patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).andReturn()

        then:
        1 * carService.updateCarPrice(id, request)

        result.response.status == HttpStatus.NO_CONTENT.value()
    }

    def "TestGetCar success case"() {
        given:
        def id = random.nextLong()
        def url = "/v1/cars/$id"
        def response = random.nextObject(CarResponse)

        def expectedResponse = """
        {
            "id": $response.id,
            "brand": "$response.brand",
            "model": "$response.model",
            "year": $response.year, 
            "fuelType": "$response.fuelType",
            "price": $response.price
        }
        """

        when:
        def jsonResponse = mockMvc.perform(
                get(url).contentType(MediaType.APPLICATION_JSON)
        ).andReturn()

        then:
        1 * carService.getCar(id) >> response

        jsonResponse.response.status == HttpStatus.OK.value()
        JSONAssert.assertEquals(expectedResponse.toString(), jsonResponse.response.getContentAsString(), true)
    }

    def "TestGetAllCars success case"() {
        given:
        def url = "/v1/cars"
        def pageNumber = 0
        def pageSize = 10
        def pageCriteria = PageCriteria.of(pageNumber, pageSize)
        def response = random.nextObject(CarResponse)
        def pageabResponse = PageableResponse.<CarResponse> builder()
                .content([response])
                .pageCount(1)
                .totalElements(1)
                .hasNextPage(false)
                .build()

        def expectedResponse =
                """
                    {
                        "content": [
                            {
                                "id": $response.id,
                                "brand": "$response.brand",
                                "model": "$response.model",
                                "year": $response.year,
                                "fuelType": "$response.fuelType",
                                "price": $response.price
                            }
                        ],
                        "totalElements": 1,
                        "pageCount": 1,
                        "hasNextPage": false
                    }
                """

        when:
        def jsonResponse = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", pageNumber as String)
                .param("count", pageSize as String)
        ).andReturn()

        then:
        1 * carService.getAllCars(pageCriteria) >> pageabResponse
        jsonResponse.response.status == HttpStatus.OK.value()
        JSONAssert.assertEquals(expectedResponse.toString(), jsonResponse.response.getContentAsString(), true)

    }
}
