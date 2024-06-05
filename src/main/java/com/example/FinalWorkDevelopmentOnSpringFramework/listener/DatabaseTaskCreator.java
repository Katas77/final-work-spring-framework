/*




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
    private final HotelRepository hotelService;
    private final RoomRepository roomService;
    private final UserService userService;
    private final BookingRepository bookingService;


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
        hotel = hotelService.save(hotel);

        Hotel hotel2 = Hotel.builder()
                .title(" Golden Temple Hotel")
                .city("Siem Reap")
                .headingAdvertisements("the best place to stay2")
                .address("Lotus Blanc Resort National Road 6, Village, Siem Reap, Cambodia")
                .ratings(5L)
                .distance(51L)
                .numberRatings(890L).build();
        hotel2 = hotelService.save(hotel2);

        Room room = Room.builder()
                .unavailableBegin(LocalDate.of(2024, 6, 2))
                .unavailableEnd(LocalDate.of(2024, 6, 8))
                .description("best")
                .number(22L)
                .hotel(hotel)
                .name("Red")
                .price(100L)
                .maximumPeople(2L)
                .build();
        room = roomService.save(room);

        Room room2 = Room.builder()
                .unavailableBegin(LocalDate.of(2024, 6, 22))
                .unavailableEnd(LocalDate.of(2024, 6, 30))
                .description("best2")
                .number(11L)
                .hotel(hotel)
                .name("Red")
                .price(1000L)
                .maximumPeople(1L)
                .build();
        room2 = roomService.save(room2);


        User user1 = User.builder()
                .name("Ivan")
                .emailAddress("ivan@mail.ru")
                .password("1234")
                .build();
         userService.create(user1,RoleType.ROLE_ADMIN);

        Role role2 = Role.from(RoleType.ROLE_USER);
        User user2 = User.builder()
                .roles(Collections.singletonList(role2))
                .name("Oleg")
                .emailAddress("oleg@mail.ru")
                .password("12")
                .build();
        userService.create(user2,RoleType.ROLE_USER);

        Booking booking = Booking.builder()
                .dateCheck_out(LocalDate.of(24, 11, 3))
                .dateCheck_in(LocalDate.of(24, 11, 17))
                .room(room)
                .user(userService.findById(1L))
                .build();
        bookingService.save(booking);
        Booking booking2 = Booking.builder()
                .dateCheck_out(LocalDate.of(24, 12, 3))
                .dateCheck_in(LocalDate.of(24, 12, 17))
                .room(room)
                .user(userService.findById(1L))
                .build();
        bookingService.save(booking2);


    }
}










*/
