

package com.example.FinalWorkDevelopmentOnSpringFramework.listener;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.Role;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.BookingRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.HotelRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;


@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseTaskCreator {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;


    @EventListener(ApplicationStartedEvent.class)
    public void createTaskData() {
        Hotel hotel = Hotel.builder()
                .title("Dreams Resort")
                .city("Siem Reap")
                .headingAdvertisements("the best place to stay")
                .address(" Siem Reap 17251 Cambodia")
                .ratings(5L)
                .distance(61L)
                .numberRatings(876L).build();
        hotel = hotelRepository.save(hotel);

        Hotel hotel2 = Hotel.builder()
                .title(" Golden Temple Hotel")
                .city("Siem Reap")
                .headingAdvertisements("the best place to stay2")
                .address("Lotus Blanc Resort National Road 6, Village, Siem Reap, Cambodia")
                .ratings(5L)
                .distance(51L)
                .numberRatings(890L).build();
        hotel2 = hotelRepository.save(hotel2);

        Room room = Room.builder()
                .unavailableBegin(LocalDate.of(2024, 8, 2))
                .unavailableEnd(LocalDate.of(2024, 8, 8))
                .description("best")
                .number(22L)
                .hotel(hotelRepository.findById(1L).orElseThrow(() -> new RuntimeException("hotel not found!")))
                .name("Red")
                .price(10L)
                .maximumPeople(2L)
                .build();
        room = roomRepository.save(room);

        Room room2 = Room.builder()
                .unavailableBegin(LocalDate.of(2024, 6, 22))
                .unavailableEnd(LocalDate.of(2024, 6, 30))
                .description("best2")
                .number(11L)
                .hotel(hotelRepository.findById(1L).orElseThrow(() -> new RuntimeException("hotel not found!")))
                .name("Red")
                .price(100L)
                .maximumPeople(1L)
                .build();
        room2 = roomRepository.save(room2);
        Room room3 = Room.builder()
                .unavailableBegin(LocalDate.of(2024, 7, 22))
                .unavailableEnd(LocalDate.of(2024, 7, 30))
                .description("best2")
                .number(11L)
                .hotel(hotelRepository.findById(1L).orElseThrow(() -> new RuntimeException("hotel not found!")))
                .name("Red")
                .price(1000L)
                .maximumPeople(1L)
                .build();
        room3 = roomRepository.save(room3);

        User user1 = User.builder()
                .name("Ivan")
                .emailAddress("ivan@mail.ru")
                .password("1234")
                .build();
        userService.create(user1, RoleType.ROLE_ADMIN);

        Role role2 = Role.from(RoleType.ROLE_USER);
        User user2 = User.builder()
                .roles(Collections.singletonList(role2))
                .name("Oleg")
                .emailAddress("oleg@mail.ru")
                .password("12")
                .build();
        userService.create(user2, RoleType.ROLE_USER);

        Booking booking = Booking.builder()
                .dateCheck_out(LocalDate.of(24, 12, 3))
                .dateCheck_in(LocalDate.of(24, 12, 17))
                .room(room)
                .user(userService.findById(2L))
                .build();
        bookingRepository.save(booking);
        Booking booking2 = Booking.builder()
                .dateCheck_out(LocalDate.of(24, 11, 3))
                .dateCheck_in(LocalDate.of(24, 11, 17))
                .room(room)
                .user(userService.findById(2L))
                .build();
        bookingRepository.save(booking2);


    }
}













