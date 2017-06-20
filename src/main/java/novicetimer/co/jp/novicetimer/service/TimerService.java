package novicetimer.co.jp.novicetimer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import novicetimer.co.jp.novicetimer.domain.TimerDomain;
import novicetimer.co.jp.novicetimer.repository.TimerRepository;

@Service
public class TimerService {
    @Autowired
    TimerRepository timerRepository;

    public String create(TimerDomain timerDomain) {
        return timerRepository.timerStart(timerDomain);
    }
}
