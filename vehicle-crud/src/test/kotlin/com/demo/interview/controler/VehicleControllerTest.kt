import com.demo.interview.controler.VehicleController
import com.demo.interview.dto.Vehicle
import com.demo.interview.service.VehicleService
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

class VehicleControllerTest {

    private val vehicleService: VehicleService = mock(VehicleService::class.java)
    private val vehicleController = VehicleController(vehicleService)

    @Test
    fun testGetVehicleWithValidId() {
        val vehicle = Vehicle(1, "Car", 10000, emptySet())
        `when`(vehicleService.getById(1)).thenReturn(Optional.of(vehicle))

        val result = vehicleController.getVehicle(1)

        assertEquals(vehicle, result)
    }

    @Test
    fun testGetVehicleWithInvalidId() {
        val exception = assertThrows<IllegalArgumentException> {
            vehicleController.getVehicle(0)
        }

        assertEquals("Invalid vehicle ID", exception.message)
    }

    @Test
    fun testGetVehicleWithNonExistentId() {
        `when`(vehicleService.getById(999)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            vehicleController.getVehicle(999)
        }

        assertEquals("Vehicle not found", exception.message)
    }
}