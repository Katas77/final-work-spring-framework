package com.example.FinalWorkDevelopmentOnSpringFramework;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BadRequestException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.BookingRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.impl.BookingServiceImpl;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer.ServiceProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

	@Mock
	private BookingRepository bookRepository;

	@Mock
	private ServiceProducer producer;

	@Mock
	private UserService userService;

	@Mock
	private RoomService roomService;

	@InjectMocks
	private BookingServiceImpl bookingService;

	private Booking sampleBooking;
	private User sampleUser;
	private Room sampleRoom;

	@BeforeEach
	void setUp() {
		sampleUser = new User();
		sampleUser.setId(10L);

		sampleRoom = new Room();
		sampleRoom.setId(20L);

		sampleBooking = new Booking();
		sampleBooking.setId(1L);
		sampleBooking.setUser(sampleUser);
		sampleBooking.setRoom(sampleRoom);
		sampleBooking.setDateCheck_in(LocalDate.now().plusDays(1));
		sampleBooking.setDateCheck_out(LocalDate.now().plusDays(3));
	}

	@Test
	void findAll_validParams_returnsList() {
		List<Booking> pageContent = List.of(sampleBooking);
		when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(pageContent));

		List<Booking> result = bookingService.findAll(0, 10);

		assertEquals(1, result.size());
		assertEquals(sampleBooking, result.get(0));
		verify(bookRepository).findAll(PageRequest.of(0, 10));
	}

	@Test
	void findAll_invalidParams_throwsBadRequest() {
		assertThrows(BadRequestException.class, () -> bookingService.findAll(-1, 10));
		assertThrows(BadRequestException.class, () -> bookingService.findAll(0, 0));
	}

	@Test
	void findById_nullId_throwsIllegalArgument() {
		assertThrows(IllegalArgumentException.class, () -> bookingService.findById(null));
	}

	@Test
	void findById_notFound_throwsNotFound() {
		when(bookRepository.findById(5L)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> bookingService.findById(5L));
	}

	@Test
	void findById_found_returnsBooking() {
		when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBooking));
		Booking result = bookingService.findById(1L);
		assertEquals(sampleBooking, result);
	}

	@Test
	void save_validBooking_savesAndSendsEvent() {
		when(roomService.findById(1L)).thenReturn(sampleRoom);
		Booking toSave = new Booking();
		toSave.setId(1L);
		toSave.setUser(sampleUser);
		toSave.setDateCheck_in(LocalDate.now().plusDays(2));
		toSave.setDateCheck_out(LocalDate.now().plusDays(4));

		Booking saved = new Booking();
		saved.setId(100L);
		saved.setUser(sampleUser);
		saved.setRoom(sampleRoom);
		saved.setDateCheck_in(toSave.getDateCheck_in());
		saved.setDateCheck_out(toSave.getDateCheck_out());

		when(bookRepository.save(any(Booking.class))).thenReturn(saved);

		String message = bookingService.save(toSave);

		assertTrue(message.contains("100"));
		verify(bookRepository).save(any(Booking.class));
		verify(producer).sendBookingEvent(saved);
	}

	@Test
	void save_overlappingUnavailable_throwsBadRequest() {
		Room unavailableRoom = new Room();
		unavailableRoom.setId(20L);
		unavailableRoom.setUnavailableBegin(LocalDate.now().plusDays(2));
		unavailableRoom.setUnavailableEnd(LocalDate.now().plusDays(5));
		Booking toSave = new Booking();
		toSave.setId(1L);
		toSave.setUser(sampleUser);
		toSave.setDateCheck_in(LocalDate.now().plusDays(3));
		toSave.setDateCheck_out(LocalDate.now().plusDays(4));

		when(roomService.findById(1L)).thenReturn(unavailableRoom);

		assertThrows(BadRequestException.class, () -> bookingService.save(toSave));
		verify(bookRepository, never()).save(any());
		verify(producer, never()).sendBookingEvent(any());
	}

	@Test
	void update_nonexistent_throwsNotFound() {
		Booking toUpdate = new Booking();
		toUpdate.setId(999L);

		when(bookRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> bookingService.update(toUpdate));
	}

	@Test
	void update_overlappingUnavailable_throwsBadRequest() {
		Booking existing = new Booking();
		existing.setId(1L);
		existing.setUser(sampleUser);
		existing.setRoom(sampleRoom);
		existing.setDateCheck_in(LocalDate.now().plusDays(1));
		existing.setDateCheck_out(LocalDate.now().plusDays(2));

		when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));

		Booking source = new Booking();
		source.setId(1L);
		source.setDateCheck_in(LocalDate.now().plusDays(3));
		source.setDateCheck_out(LocalDate.now().plusDays(4));
		Room unavailableRoom = new Room();
		unavailableRoom.setId(20L);
		unavailableRoom.setUnavailableBegin(LocalDate.now().plusDays(2));
		unavailableRoom.setUnavailableEnd(LocalDate.now().plusDays(10));
		source.setRoom(unavailableRoom);

		when(roomService.findById(20L)).thenReturn(unavailableRoom);

		assertThrows(BadRequestException.class, () -> bookingService.update(source));

		verify(bookRepository, never()).save(any());
	}


	@Test
	void update_success_savesAndReturnsMessage() {
		Booking existing = new Booking();
		existing.setId(1L);
		existing.setUser(sampleUser);
		existing.setRoom(sampleRoom);
		existing.setDateCheck_in(LocalDate.now().plusDays(1));
		existing.setDateCheck_out(LocalDate.now().plusDays(2));

		when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));

		Booking source = new Booking();
		source.setId(1L);
		source.setDateCheck_in(LocalDate.now().plusDays(3));
		source.setDateCheck_out(LocalDate.now().plusDays(5));
		source.setRoom(sampleRoom);

		when(roomService.findById(sampleRoom.getId())).thenReturn(sampleRoom);

		Booking saved = new Booking();
		saved.setId(1L);
		when(bookRepository.save(any(Booking.class))).thenReturn(saved);

		String msg = bookingService.update(source);
		assertTrue(msg.contains("1"));
		verify(bookRepository).save(any(Booking.class));
	}



	@Test
	void deleteById_notFound_throwsNotFound() {
		when(bookRepository.existsById(50L)).thenReturn(false);
		assertThrows(NotFoundException.class, () -> bookingService.deleteById(50L));
	}

	@Test
	void deleteById_exists_deletesAndReturnsMessage() {
		when(bookRepository.existsById(1L)).thenReturn(true);
		String result = bookingService.deleteById(1L);
		verify(bookRepository).deleteById(1L);
		assertTrue(result.contains("1"));
	}

	@Test
	void validateBookingInput_nullBooking_throwsBadRequest() {
		assertThrows(BadRequestException.class, () -> bookingService.validateBookingInput(null));
	}

	@Test
	void validateBookingInput_missingUser_throwsBadRequest() {
		Booking b = new Booking();
		b.setRoom(sampleRoom);
		b.setDateCheck_in(LocalDate.now().plusDays(1));
		b.setDateCheck_out(LocalDate.now().plusDays(2));
		assertThrows(BadRequestException.class, () -> bookingService.validateBookingInput(b));
	}

	@Test
	void validateBookingInput_missingRoom_throwsBadRequest() {
		Booking b = new Booking();
		b.setUser(sampleUser);
		b.setDateCheck_in(LocalDate.now().plusDays(1));
		b.setDateCheck_out(LocalDate.now().plusDays(2));
		assertThrows(BadRequestException.class, () -> bookingService.validateBookingInput(b));
	}

	@Test
	void validateBookingInput_datesNull_throwsBadRequest() {
		Booking b = new Booking();
		b.setUser(sampleUser);
		b.setRoom(sampleRoom);
		assertThrows(BadRequestException.class, () -> bookingService.validateBookingInput(b));
	}

	@Test
	void validateBookingInput_pastDates_throwsBadRequest() {
		Booking b = new Booking();
		b.setUser(sampleUser);
		b.setRoom(sampleRoom);
		b.setDateCheck_in(LocalDate.now().minusDays(5));
		b.setDateCheck_out(LocalDate.now().minusDays(1));
		assertThrows(BadRequestException.class, () -> bookingService.validateBookingInput(b));
	}

	@Test
	void validateBookingInput_startAfterEnd_throwsBadRequest() {
		Booking b = new Booking();
		b.setUser(sampleUser);
		b.setRoom(sampleRoom);
		b.setDateCheck_in(LocalDate.now().plusDays(5));
		b.setDateCheck_out(LocalDate.now().plusDays(1));
		assertThrows(BadRequestException.class, () -> bookingService.validateBookingInput(b));
	}

	@Test
	void validateBookingInput_sameDates_throwsBadRequest() {
		Booking b = new Booking();
		b.setUser(sampleUser);
		b.setRoom(sampleRoom);
		LocalDate d = LocalDate.now().plusDays(3);
		b.setDateCheck_in(d);
		b.setDateCheck_out(d);
		assertThrows(BadRequestException.class, () -> bookingService.validateBookingInput(b));
	}


	@Test
	void isBookingOverlappingUnavailable_roomNull_returnsFalse() {
		boolean res = bookingService.isBookingOverlappingUnavailable(LocalDate.now(), LocalDate.now().plusDays(1), null);
		assertFalse(res);
	}

	@Test
	void isBookingOverlappingUnavailable_noUnavailable_returnsFalse() {
		Room r = new Room();
		r.setUnavailableBegin(null);
		r.setUnavailableEnd(null);
		boolean res = bookingService.isBookingOverlappingUnavailable(LocalDate.now(), LocalDate.now().plusDays(1), r);
		assertFalse(res);
	}

	@Test
	void isBookingOverlappingUnavailable_beforeUnavailable_returnsFalse() {
		Room r = new Room();
		r.setUnavailableBegin(LocalDate.now().plusDays(10));
		r.setUnavailableEnd(LocalDate.now().plusDays(20));
		boolean res = bookingService.isBookingOverlappingUnavailable(LocalDate.now(), LocalDate.now().plusDays(1), r);
		assertFalse(res);
	}

	@Test
	void isBookingOverlappingUnavailable_overlap_returnsTrue() {
		Room r = new Room();
		r.setUnavailableBegin(LocalDate.now().plusDays(2));
		r.setUnavailableEnd(LocalDate.now().plusDays(5));
		boolean res = bookingService.isBookingOverlappingUnavailable(LocalDate.now().plusDays(3), LocalDate.now().plusDays(4), r);
		assertTrue(res);
	}

	@Test
	void isBookingOverlappingUnavailable_openEndedBegin_returnsTrueWhenOverlap() {
		Room r = new Room();
		r.setUnavailableBegin(null);
		r.setUnavailableEnd(LocalDate.now().plusDays(2));

		boolean res = bookingService.isBookingOverlappingUnavailable(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), r);
		assertTrue(res);
	}

	@Test
	void isBookingOverlappingUnavailable_openEndedEnd_returnsTrueWhenOverlap() {
		Room r = new Room();
		r.setUnavailableBegin(LocalDate.now().plusDays(2));
		r.setUnavailableEnd(null);

		boolean res = bookingService.isBookingOverlappingUnavailable(LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), r);
		assertTrue(res);
	}
}
