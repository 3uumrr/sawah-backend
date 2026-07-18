package com.sawah.sawah_backend.service.service;

import com.sawah.sawah_backend.dto.service.ServiceInputDto;
import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.models.Service;

import java.util.List;

public interface ServiceService {
    Service getById(Long id);
    List<Service> getServices();
    Service getByCode(ServiceCode code);

    // For Admin
    void deleteById(Long id);
    void addServices(ServiceInputDto language);
    void updateServices(ServiceInputDto language , Long id);

    void initServices();
}
