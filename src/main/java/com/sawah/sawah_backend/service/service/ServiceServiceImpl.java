package com.sawah.sawah_backend.service.service;

import com.sawah.sawah_backend.dto.service.ServiceInputDto;
import com.sawah.sawah_backend.enums.ServiceCode;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.mapper.ServiceMapper;
import com.sawah.sawah_backend.models.Service;
import com.sawah.sawah_backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceServiceImpl implements ServiceService{
    private final ServiceRepository serviceRepository;
    private final ServiceMapper mapper;

    @Override
    public Service getById(Long id) {
       return serviceRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("service.not.found"));
    }

    @Override
    public List<Service> getServices() {
        return serviceRepository.findAll();
    }

    @Override
    public Service getByCode(ServiceCode code) {
        return serviceRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("service.not.found"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Service service = getById(id);
        serviceRepository.delete(service);
    }

    @Override
    @Transactional
    public void addServices(ServiceInputDto serviceInputDto) {

        Service service = mapper.toEntity(serviceInputDto);

        service.setCode(Enum.valueOf(ServiceCode.class,serviceInputDto.code()));

        serviceRepository.save(service);
    }

    @Override
    @Transactional
    public void updateServices(ServiceInputDto serviceInputDto, Long id) {
        Service serviceFromDb = getById(id);

        mapper.updateEntityFromDto(serviceInputDto, serviceFromDb);

        serviceFromDb.setCode(Enum.valueOf(ServiceCode.class,serviceInputDto.code()));

        serviceRepository.save(serviceFromDb);

    }

    @Override
    @Transactional
    public void initServices() {
        List<Service> services = List.of(
                Service.builder()
                        .nameEn("Tour Guide")
                        .nameAr("مرشد سياحي")
                        .code(ServiceCode.GUIDE)
                        .build(),

                Service.builder()
                        .nameEn("Driver")
                        .nameAr("سائق")
                        .code(ServiceCode.DRIVER)
                        .build(),

                Service.builder()
                        .nameEn("Translator")
                        .nameAr("مترجم")
                        .code(ServiceCode.TRANSLATOR)
                        .build()
        );
        if (serviceRepository.count() == 0) {
            serviceRepository.saveAll(services);
        }
    }
}
