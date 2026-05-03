package com.github.marcoshssilva.cloud.api.controller.web.data;

import java.util.Set;

public record HttpHeader(String name, Set<String> value) { }
