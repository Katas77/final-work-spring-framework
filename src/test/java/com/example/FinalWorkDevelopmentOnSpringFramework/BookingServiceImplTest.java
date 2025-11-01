package com.example.FinalWorkDevelopmentOnSpringFramework;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BadRequestException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.BookingRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.impl.BookingServiceImpl;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer.ServiceProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private ServiceProducer producer;

	@InjectMocks
	private BookingServiceImpl bookingService;

	private User user;
	private Room freeRoom;
	private Room unavailableRoom;

	@BeforeEach
	void setUp() {
		user = User.builder().id(10L).build();

		freeRoom = Room.builder()
				.id(1L)
				.unavailableBegin(null)
				.unavailableEnd(null)
				.build();

		// комната недоступна 2025-01-10 .. 2025-01-20
		unavailableRoom = Room.builder()
				.id(2L)
				.unavailableBegin(LocalDate.of(2025, 1, 10))
				.unavailableEnd(LocalDate.of(2025, 1, 20))
				.build();
	}

	@Test
	void findAll_invalidPageParams_throws() {
		assertThatThrownBy(() -> bookingService.findAll(-1, 10)).isInstanceOf(BadRequestException.class);
		assertThatThrownBy(() -> bookingService.findAll(0, 0)).isInstanceOf(BadRequestException.class);
	}

	@Test
	void findById_notFound_throws() {
		when(bookingRepository.findById(5L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> bookingService.findById(5L)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void save_success_sendsEvent_andReturnsMessage() {
		Booking booking = Booking.builder()
				.id(null)
				.user(user)
				.room(freeRoom)
				.dateCheck_in(LocalDate.now().plusDays(1))
				.dateCheck_out(LocalDate.now().plusDays(3))
				.build();

		Booking saved = Booking.builder()
				.id(100L)
				.user(user)
				.room(freeRoom)
				.dateCheck_in(booking.getDateCheck_in())
				.dateCheck_out(booking.getDateCheck_out())
				.build();

		when(bookingRepository.save(booking)).thenReturn(saved);

		String res = bookingService.save(booking);

		assertThat(res).contains("100");
		verify(bookingRepository).save(booking);
		ArgumentCaptor<com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model.BookingEvent> captor =
				ArgumentCaptor.forClass(com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model.BookingEvent.class);
		verify(producer).sendBookingEvent(captor.capture());
		assertThat(captor.getValue().getUserId()).isEqualTo(user.getId());
	}

	@Test
	void save_invalidDates_throws() {
		Booking booking = Booking.builder()
				.id(null)
				.user(user)
				.room(freeRoom)
				.dateCheck_in(LocalDate.now().minusDays(2))
				.dateCheck_out(LocalDate.now().plusDays(1))
				.build();

		assertThatThrownBy(() -> bookingService.save(booking)).isInstanceOf(BadRequestException.class);
		verify(bookingRepository, never()).save(any());
		verify(producer, never()).sendBookingEvent(any());
	}

	@Test
	void save_overlapsUnavailable_throws() {
		// Бронирование полностью охватывает период недоступности (10..20) => должно считаться перекрытием
		Booking booking = Booking.builder()
				.id(null)
				.user(user)
				.room(unavailableRoom)
				.dateCheck_in(LocalDate.of(2025, 1, 5))
				.dateCheck_out(LocalDate.of(2025, 1, 25))
				.build();

		assertThatThrownBy(() -> bookingService.save(booking)).isInstanceOf(BadRequestException.class);
		verify(bookingRepository, never()).save(any());
	}

	@Test
	void save_overlapsUnavailable_edgeCases_throws() {
		// Бронирование начинается в середине unavailable
		Booking b1 = Booking.builder()
				.user(user).room(unavailableRoom)
				.dateCheck_in(LocalDate.of(2025, 1, 12))
				.dateCheck_out(LocalDate.of(2025, 1, 22)).build();
		assertThatThrownBy(() -> bookingService.save(b1)).isInstanceOf(BadRequestException.class);

		// Бронирование заканчивается в середине unavailable
		Booking b2 = Booking.builder()
				.user(user).room(unavailableRoom)
				.dateCheck_in(LocalDate.of(2025, 1, 5))
				.dateCheck_out(LocalDate.of(2025, 1, 12)).build();
		assertThatThrownBy(() -> bookingService.save(b2)).isInstanceOf(BadRequestException.class);


		Booking b3 = Booking.builder()
				.user(user).room(unavailableRoom)
				.dateCheck_in(LocalDate.now().plusDays(7))
				.dateCheck_out(LocalDate.now().plusDays(17)).build();
		when(bookingRepository.save(b3)).thenReturn(b3);
		assertThatNoException().isThrownBy(() -> bookingService.save(b3));
		verify(bookingRepository).save(b3);
	}

	@Test
	void update_existing_updatesAndValidates() {
		Booking existing = Booking.builder()
				.id(50L)
				.user(user)
				.room(freeRoom)
				.dateCheck_in(LocalDate.now().plusDays(2))
				.dateCheck_out(LocalDate.now().plusDays(4))
				.build();

		Booking update = Booking.builder()
				.id(50L)
				.dateCheck_in(LocalDate.now().plusDays(3)) // смещаем
				.build();

		when(bookingRepository.findById(50L)).thenReturn(Optional.of(existing));
		when(bookingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		String res = bookingService.update(update);
		assertThat(res).contains("50");
		verify(bookingRepository).save(any(Booking.class));
	}
}
