package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.utils.BeanUtils;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.mapper.RoomMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<Room> findAll(int pageNumber, int pageSize) {
        validatePageParams(pageNumber, pageSize);
        log.info("Fetching all rooms, page: {}, size: {}", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return roomRepository.findAll(page).getContent();
    }

    @Override
    public RoomResponse findById(Long id) {
        log.info("Finding room with ID {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Room with ID {0} not found", id)));
        return RoomMapper.toResponse(room);
    }

    @Override
    public String save(Room room) {
        log.info("Saving room with name {}", room.getName());
        roomRepository.save(room);
        return MessageFormat.format("Room with name - {0} saved", room.getName());
    }

    @Transactional
    @Override
    public String update(Room room) {
        log.info("Updating room with ID {}", room.getId());
        Room existingRoom = roomRepository.findById(room.getId())
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Room with ID {0} not found", room.getId())));
        BeanUtils.copyNonNullProperties(room, existingRoom);
        roomRepository.save(existingRoom);
        return MessageFormat.format("Room with ID {0} updated", room.getId());
    }

    @Transactional
    @Override
    public String deleteById(Long id) {
        log.info("Deleting room with ID {}", id);
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException(MessageFormat.format("Room with ID {0} not found", id));
        }
        roomRepository.deleteById(id);
        return MessageFormat.format("Room with ID {0} deleted", id);
    }

    @Override
    public List<Room> findFilter(int pageNumber, int pageSize, FilterRoom request) {
        validatePageParams(pageNumber, pageSize);
        log.info("Filtering rooms, page: {}, size: {}, filter: {}", pageNumber, pageSize, request);

        Optional<LocalDate> maybeDate;
        if (request != null && request.dateCheckIn() != null) {
            maybeDate = Optional.of(localDateOfString(request.dateCheckIn()));
        } else {
            maybeDate = Optional.empty();
        }

        List<Room> roomList = roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                .filter(room -> maybeDate.map(date -> isDateAvailable(date, room)).orElse(true))
                .filter(room -> request == null || request.maxPrice() == null || request.maxPrice().compareTo(room.getPrice()) >= 0)
                .filter(room -> request == null || request.minPrice() == null || request.minPrice().compareTo(room.getPrice()) <= 0)
                .filter(room -> request == null || request.description() == null || Objects.equals(room.getDescription(), request.description()))
                .filter(room -> request == null || request.roomId() == null || Objects.equals(room.getId(), request.roomId()))
                .filter(room -> request == null || request.maximumPeople() == null || Objects.equals(room.getMaximumPeople(), request.maximumPeople()))
                .collect(Collectors.toList());

        if (roomList.isEmpty()) {
            log.info("No room with these parameters was found");
            throw new NotFoundException("No room with these parameters was found");
        }

        return roomList;
    }

    /**
     * Возвращает true, если комната доступна на указанную дату.
     * Если unavailableBegin и unavailableEnd оба null => доступна всегда.
     * Если есть только одно из значений — применяются соответствующие проверки.
     */
    private boolean isDateAvailable(LocalDate date, Room room) {
        LocalDate begin = room.getUnavailableBegin();
        LocalDate end = room.getUnavailableEnd();

        if (begin == null && end == null) {
            return true;
        }
        if (begin != null && end != null) {
            // комната занята от begin до end включительно -> она НЕ доступна в этом диапазоне
            return date.isBefore(begin) || date.isAfter(end);
        }
        if (begin != null) {
            // занята с begin и далее
            return date.isBefore(begin);
        }
        // end != null
        return date.isAfter(end);
    }

    /**
     * Парсит дату из строки. Поддерживает форматы ddMMyy и ddMMyyyy.
     * Бросает BusinessLogicException при ошибке парсинга.
     */
    public LocalDate localDateOfString(String date) {
        if (date == null) {
            throw new BusinessLogicException("Date string is null");
        }

        DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("ddMMyy"),
                DateTimeFormatter.ofPattern("ddMMyyyy")
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {

            }
        }

        log.error("Error parsing date string: {}", date);
        throw new BusinessLogicException("Invalid date format: " + date);
    }

    private void validatePageParams(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new BusinessLogicException("Invalid page parameters");
        }
    }
}
