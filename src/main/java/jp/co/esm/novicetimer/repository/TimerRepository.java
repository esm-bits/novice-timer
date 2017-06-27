package jp.co.esm.novicetimer.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import jp.co.esm.novicetimer.domain.MultipleTimeLimit;

@Repository
public class TimerRepository {
    private List<MultipleTimeLimit> multipleTimeLimits = new ArrayList<>();
    private int id = 1;

    public MultipleTimeLimit save(MultipleTimeLimit multipleTimeLimit) {
        multipleTimeLimit.setId(id);
        id++;
        multipleTimeLimits.add(multipleTimeLimit);
        return multipleTimeLimit;
    }
}
