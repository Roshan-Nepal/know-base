package com.roshan.know_base.common.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUserId();
    String getCurrentUsername();
}
