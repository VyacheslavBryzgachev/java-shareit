package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.enums.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BookingDto {
    private long id;
    private long bookerId;
    private long itemId;
    @FutureOrPresent()
    @NotNull()
    private LocalDateTime start;
    @Future()
    @NotNull()
    private LocalDateTime end;
    private Status status;
}
