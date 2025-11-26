package org.example.impl.request;

import org.example.core.annotations.Request;
import org.example.core.request.IRequest;

@Request
public record BasicRequest() implements IRequest<BasicResponse> {
}
