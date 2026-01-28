package com.sydorenko.vigvam.manager.interfaces;

import com.sydorenko.vigvam.manager.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Statusable {
    void setStatus(Status status);
    void setDisableDate(LocalDateTime disableDate);
}
