package com.qut.cab302_project_pomodora;

import com.qut.cab302_project_pomodora.model.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    private Timer timer;

    @BeforeEach
    void setUp() {
        timer = new Timer(25 * 60, 5 * 60, 15 * 60, 4); // Initialize with default values
    }

    @Test
    void testSetAndGetId() {
        timer.setId(1); // Set the ID
        assertEquals(1, timer.getId(), "Timer ID should be set to 1.");
    }

    @Test
    void testGetWorkDuration() {
        assertEquals(25 * 60, timer.getWorkDuration(), "Work duration should be 25 minutes in seconds.");
    }

    @Test
    void testSetWorkDuration() {
        timer.setWorkDuration(30 * 60); // Set to 30 minutes
        assertEquals(30 * 60, timer.getWorkDuration(), "Work duration should be updated to 30 minutes in seconds.");
    }

    @Test
    void testGetShortBreakDuration() {
        assertEquals(5 * 60, timer.getShortBreakDuration(), "Short break duration should be 5 minutes in seconds.");
    }

    @Test
    void testSetShortBreakDuration() {
        timer.setShortBreakDuration(10 * 60); // Set to 10 minutes
        assertEquals(10 * 60, timer.getShortBreakDuration(), "Short break duration should be updated to 10 minutes in seconds.");
    }

    @Test
    void testGetLongBreakDuration() {
        assertEquals(15 * 60, timer.getLongBreakDuration(), "Long break duration should be 15 minutes in seconds.");
    }

    @Test
    void testSetLongBreakDuration() {
        timer.setLongBreakDuration(20 * 60); // Set to 20 minutes
        assertEquals(20 * 60, timer.getLongBreakDuration(), "Long break duration should be updated to 20 minutes in seconds.");
    }

    @Test
    void testGetLongBreakAfter() {
        assertEquals(4, timer.getLongBreakAfter(), "Long break should occur after 4 cycles.");
    }

    @Test
    void testSetLongBreakAfter() {
        timer.setLongBreakAfter(5); // Set to 5 cycles
        assertEquals(5, timer.getLongBreakAfter(), "Long break should occur after 5 cycles.");
    }
}