package com.roshan.know_base.document.event;

import java.util.UUID;

public record DocumentCreatedEvent(
        UUID documentId
) {
}
