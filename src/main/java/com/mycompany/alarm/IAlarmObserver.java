/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.alarm;

import java.time.LocalDateTime;

/**
 *
 * @author kirill
 */
public interface IAlarmObserver {
    public void timerTick(LocalDateTime newTime);
}
