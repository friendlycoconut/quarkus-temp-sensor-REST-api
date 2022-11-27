package org.kost.device;

import javax.validation.constraints.NotEmpty;

public class Device {
    private Integer deviceId;

    @NotEmpty
    private String deviceName;


}
