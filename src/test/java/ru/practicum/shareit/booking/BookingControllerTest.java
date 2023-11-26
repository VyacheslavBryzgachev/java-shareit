package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController bookingController;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createBookingTest() throws Exception {
        BookingDtoOut bookingDtoOut = BookingDtoOut
                .builder()
                .id(1)
                .item(Item
                        .builder()
                        .id(1)
                        .build())
                .booker(User.
                        builder()
                        .id(1)
                        .build())
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();
        BookingDto bookingDto = BookingDto
                .builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();
        when(bookingService.createBooking(any(), anyLong()))
                .thenReturn(bookingDtoOut);
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItemId()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBookerId()))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).createBooking(any(), anyLong());
    }

    @Test
    void updateBookingTest() throws Exception {
        BookingDtoOut bookingDtoOut = BookingDtoOut
                .builder()
                .id(1)
                .item(Item
                        .builder()
                        .id(1)
                        .build())
                .booker(User.
                        builder()
                        .id(1)
                        .build())
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();

        BookingDto bookingDto = BookingDto
                .builder()
                .id(1)
                .itemId(1)
                .bookerId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();

        when(bookingService.updateBooking(1L, true, 1L))
                .thenReturn(bookingDtoOut);
        mockMvc.perform(patch("/bookings/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", String.valueOf(true)))
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItemId()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBookerId()))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).updateBooking(1L, true, 1L);
    }

    @Test
    void getBookingByIdTest() throws Exception {
        BookingDtoOut bookingDtoOut = BookingDtoOut
                .builder()
                .id(1)
                .item(Item
                        .builder()
                        .id(1)
                        .build())
                .booker(User.
                        builder()
                        .id(1)
                        .build())
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();

        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDtoOut);

        mockMvc.perform(get("/bookings/{id}", anyLong())
                        .header("X-Sharer-User-Id", anyLong()))
                .andExpect(jsonPath("$.id").value(bookingDtoOut.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingDtoOut.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(bookingDtoOut.getBooker().getId()))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).getBookingById(anyLong(), anyLong());
    }

    @Test
    void getUserBookingsTest() throws Exception {
        BookingDtoOut bookingDtoOut1 = BookingDtoOut
                .builder()
                .id(1)
                .item(Item
                        .builder()
                        .id(1)
                        .build())
                .booker(User.
                        builder()
                        .id(1)
                        .build())
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();
        BookingDtoOut bookingDtoOut2 = BookingDtoOut
                .builder()
                .id(2)
                .item(Item
                        .builder()
                        .id(2)
                        .build())
                .booker(User.
                        builder()
                        .id(2)
                        .build())
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();
        List<BookingDtoOut> bookingDtoOuts = Arrays.asList(bookingDtoOut1, bookingDtoOut2);
        when(bookingService.getUserBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(bookingDtoOuts);
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", anyLong())
                        .param("state", anyString())
                        .param("from", String.valueOf(anyInt()))
                        .param("size", String.valueOf(anyInt())))
                .andExpect(jsonPath("$.size()").value(bookingDtoOuts.size()))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).getUserBookings(anyString(), anyLong(), anyInt(), anyInt());
    }

    @Test
    void getUserItemBookingsTest() throws Exception {
        BookingDtoOut bookingDtoOut1 = BookingDtoOut
                .builder()
                .id(1)
                .item(Item
                        .builder()
                        .id(1)
                        .build())
                .booker(User.
                        builder()
                        .id(1)
                        .build())
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();
        BookingDtoOut bookingDtoOut2 = BookingDtoOut
                .builder()
                .id(2)
                .item(Item
                        .builder()
                        .id(2)
                        .build())
                .booker(User.
                        builder()
                        .id(2)
                        .build())
                .status(Status.WAITING)
                .start(LocalDateTime.of(2024, 11, 10, 10, 10, 10))
                .end(LocalDateTime.of(2024, 12, 10, 10, 10, 10))
                .build();
        List<BookingDtoOut> bookingDtoOuts = Arrays.asList(bookingDtoOut1, bookingDtoOut2);
        when(bookingService.getUserItemsBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(bookingDtoOuts);
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", anyLong())
                        .param("state", anyString())
                        .param("from", String.valueOf(anyInt()))
                        .param("size", String.valueOf(anyInt())))
                .andExpect(jsonPath("$.size()").value(bookingDtoOuts.size()))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).getUserItemsBookings(anyString(), anyLong(), anyInt(), anyInt());
    }
}
