package jp.co.esm.novicetimer.domain;

import java.util.List;

import lombok.Data;

@Data
public class Agenda {
    private int id;
    private List<Subject> subjects;
}
