package com.sawah.sawah_backend.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
                String message,
                T data,
                LocalDateTime timestamp) {
}
