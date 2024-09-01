import com.demo.interview.dao.VehicleDao
import com.demo.interview.entity.OptionEntity
import com.demo.interview.entity.VehicleEntity
import com.demo.interview.service.VehicleService
import com.demo.interview.service.VehicleServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class VehicleServiceImplTest {

    private val vehicleDao: VehicleDao = mock(VehicleDao::class.java)
    private val vehicleService: VehicleService = VehicleServiceImpl(vehicleDao)

    @Test
    fun testGetByIdReturnsVehicleDto() {
        val vehicleEntity = VehicleEntity(1, "Car", 10000, listOf())
        `when`(vehicleDao.findById(1)).thenReturn(Optional.of(vehicleEntity))

        val result = vehicleService.getById(1)

        assertTrue(result.isPresent)
        assertEquals(1, result.get().id)
        assertEquals("Car", result.get().name)
        assertEquals(10000, result.get().price)
    }

    @Test
    fun testGetByIdWithDiscountAppliesDiscount() {
        val optionEntity = OptionEntity(1, "Sunroof", 2000)
        val vehicleEntity = VehicleEntity(1, "Car", 10000, listOf(optionEntity))
        `when`(vehicleDao.findById(1)).thenReturn(Optional.of(vehicleEntity))

        val result = vehicleService.getByIdWithDiscount(1, 10)

        assertTrue(result.isPresent)
        assertEquals(1, result.get().id)
        assertEquals("Car", result.get().name)
        assertEquals(10000, result.get().price)
        assertEquals(1800, result.get().optionDto[0].price) // 10% discount on 2000
    }


    @Test
    fun testGetByIdWithDiscountAppliesDiscountToMaxPrice() {
        val optionEntity1 = OptionEntity(1, "Sunroof", 2000)
        val optionEntity2 = OptionEntity(1, "CD player", 4000)
        val optionEntity3 = OptionEntity(1, "Pink painting", 4000)
        val vehicleEntity = VehicleEntity(1, "Car", 10000, listOf(optionEntity1, optionEntity2, optionEntity3))
        `when`(vehicleDao.findById(1)).thenReturn(Optional.of(vehicleEntity))

        val result = vehicleService.getByIdWithDiscount(1, 10)

        assertTrue(result.isPresent)
        assertEquals(1, result.get().id)
        assertEquals("Car", result.get().name)
        assertEquals(10000, result.get().price)
        assertEquals(2000, result.get().optionDto[0].price) // same price
        assertEquals(3600, result.get().optionDto[1].price) // 10% discount on 4000
    }


    @Test
    fun testGetByIdWithDiscountAppliesDiscountWithFloorPrice() {
        val optionEntity = OptionEntity(1, "Sunroof", 10)
        val vehicleEntity = VehicleEntity(1, "Car", 10000, listOf(optionEntity))
        `when`(vehicleDao.findById(1)).thenReturn(Optional.of(vehicleEntity))

        val result = vehicleService.getByIdWithDiscount(1, 11)

        assertTrue(result.isPresent)
        assertEquals(1, result.get().id)
        assertEquals("Car", result.get().name)
        assertEquals(10000, result.get().price)
        assertEquals(8, result.get().optionDto[0].price) // 11% 9% discount should be 8.9 round to 8
    }


    @Test
    fun testGetByIdWithDiscountAppliesDiscountWithCeilRound() {
        val optionEntity = OptionEntity(1, "Sunroof", 10)
        val vehicleEntity = VehicleEntity(1, "Car", 10000, listOf(optionEntity))
        `when`(vehicleDao.findById(1)).thenReturn(Optional.of(vehicleEntity))

        val result = vehicleService.getByIdWithDiscount(1, 9)

        assertTrue(result.isPresent)
        assertEquals(1, result.get().id)
        assertEquals("Car", result.get().name)
        assertEquals(10000, result.get().price)
        assertEquals(9, result.get().optionDto[0].price) // 9% discount should be 9.1 round to 9
    }


    @Test
    fun testGetByIdWithDiscountNoOptions() {
        val vehicleEntity = VehicleEntity(1, "Car", 10000, listOf())
        `when`(vehicleDao.findById(1)).thenReturn(Optional.of(vehicleEntity))

        val result = vehicleService.getByIdWithDiscount(1, 10)

        assertTrue(result.isPresent)
        assertEquals(1, result.get().id)
        assertEquals("Car", result.get().name)
        assertEquals(10000, result.get().price)
        assertTrue(result.get().optionDto.isEmpty())
    }
}