package com.inretailpharma.digital.ordertracker.firebase.model;

import lombok.Data;

@Data
public class PickerSettingCanonical implements SettingCanonical {
    private Boolean scanCommand;
    private Boolean scanTray;
    public PickerSettingCanonical(Boolean scanCommand, Boolean scanTray) {
        this.scanCommand = scanCommand;
        this.scanTray = scanTray;
    }

}
