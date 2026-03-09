package com.sawah.sawah_backend.service.language;

import com.sawah.sawah_backend.dto.LanguageInputDto;
import com.sawah.sawah_backend.exceptions.ResourceNotFoundException;
import com.sawah.sawah_backend.mapper.LanguageMapper;
import com.sawah.sawah_backend.models.Language;
import com.sawah.sawah_backend.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper mapper;

    @Override
    @Transactional(readOnly = true) // تحسين أداء للـ Queries
    public Language getByCode(String code) {
        return languageRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found"));
    }

    @Override
    public Language getById(Long id) {
        return  languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found"));
    }

    @Override
    public List<Language> getLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Language language = getById(id);
        languageRepository.delete(language);
    }

    @Override
    public void addLanguage(LanguageInputDto language) {
        languageRepository.save(mapper.toEntity(language));
    }

    @Override
    public void updateLanguage(LanguageInputDto language, Long id) {
        Language languageFromDb = getById(id);

        languageFromDb.setCode(language.code());
        languageFromDb.setNameAr(language.nameAr());
        languageFromDb.setNameEn(language.nameEn());

        languageRepository.save(languageFromDb);
    }

    @Override
    public void initLanguages() {
        if (languageRepository.count() > 0) return;

        Map<String, String[]> topLanguages = new LinkedHashMap<>();

        topLanguages.put("ar", new String[]{"العربية", "Arabic"});
        topLanguages.put("en", new String[]{"الإنجليزية", "English"});
        topLanguages.put("fr", new String[]{"الفرنسية", "French"});
        topLanguages.put("de", new String[]{"الألمانية", "German"});
        topLanguages.put("it", new String[]{"الإيطالية", "Italian"});
        topLanguages.put("es", new String[]{"الإسبانية", "Spanish"});
        topLanguages.put("ru", new String[]{"الروسية", "Russian"});
        topLanguages.put("zh", new String[]{"الصينية", "Chinese"});
        topLanguages.put("ja", new String[]{"اليابانية", "Japanese"});
        topLanguages.put("tr", new String[]{"التركية", "Turkish"});
        topLanguages.put("pt", new String[]{"البرتغالية", "Portuguese"});
        topLanguages.put("ko", new String[]{"الكورية", "Korean"});
        topLanguages.put("hi", new String[]{"الهندية", "Hindi"});
        topLanguages.put("nl", new String[]{"الهولندية", "Dutch"});
        topLanguages.put("pl", new String[]{"البولندية", "Polish"});
        topLanguages.put("uk", new String[]{"الأوكرانية", "Ukrainian"});
        topLanguages.put("el", new String[]{"اليونانية", "Greek"});
        topLanguages.put("id", new String[]{"الإندونيسية", "Indonesian"});
        topLanguages.put("fa", new String[]{"الفارسية", "Persian"});
        topLanguages.put("he", new String[]{"العبرية", "Hebrew"});

        List<Language> languages = topLanguages.entrySet().stream()
                .map(entry -> Language.builder()
                        .code(entry.getKey())
                        .nameAr(entry.getValue()[0])
                        .nameEn(entry.getValue()[1])
                        .build())
                .collect(Collectors.toList());

        languageRepository.saveAll(languages);
    }
}
