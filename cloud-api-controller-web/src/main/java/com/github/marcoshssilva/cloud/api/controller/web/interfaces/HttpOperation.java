package com.github.marcoshssilva.cloud.api.controller.web.interfaces;

import com.github.marcoshssilva.cloud.api.controller.web.data.HttpMethod;

public @interface HttpOperation {
    HttpMethod method();
    String path();
}
