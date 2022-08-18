package com.umc.techl.src.service;

import com.umc.techl.config.BaseException;
import com.umc.techl.src.repository.ScheduledRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.umc.techl.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledService {

    private final ScheduledRepository scheduledRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updatePostStatus() throws Exception {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatedNow = now.format(formatter);

        try {
            scheduledRepository.changeToOngoing(formatedNow);
            scheduledRepository.changeToFinished(formatedNow);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
