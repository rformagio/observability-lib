package com.github.rformagio.observability.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CorrelationIdFilterProp {

    private String headerName;
    private boolean enabled = false;
    private int order = 2;
    private List<String> resources = new ArrayList<>();
}
