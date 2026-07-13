package com.sawah.sawah_backend.mapper;

import com.sawah.sawah_backend.dto.issue.IssueAdminResponseDto;
import com.sawah.sawah_backend.dto.issue.IssueResponseDto;
import com.sawah.sawah_backend.models.Issue;
import com.sawah.sawah_backend.models.Place;
import com.sawah.sawah_backend.models.Provider;
import com.sawah.sawah_backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IssueMapper {

    @Mapping(source = "booking.id" , target = "bookingId")
    @Mapping(source = "createdAt" , target = "issueDate")
    @Mapping(source = "booking.place.nameAr" , target = "placeNameAr")
    @Mapping(source = "booking.place.nameEn" , target = "placeNameEn")
    @Mapping(target = "placeImageUrl", source = "booking.place", qualifiedByName = "toFullUrl")
    @Mapping(source = "status" , target = "issueStatus")
    IssueResponseDto issueToIssueDto(Issue issue);

    List<IssueResponseDto> issueToListIssueDto(List<Issue> issue);

    @Mapping(source = "createdAt", target = "issueDate")
    @Mapping(source = "booking.id", target = "bookingId")
    @Mapping(source = "booking.status", target = "bookingStatus")
    @Mapping(source = "booking.tourist.id", target = "touristId")
    @Mapping(source = "booking.tourist", target = "touristName", qualifiedByName = "toFullName")
    @Mapping(source = "booking.tourist.email", target = "touristEmail")
    @Mapping(source = "booking.tourist.phoneNumber", target = "touristPhoneNumber")
    @Mapping(source = "booking.provider.id", target = "providerId")
    @Mapping(source = "booking.provider", target = "providerName", qualifiedByName = "providerToFullName")    @Mapping(source = "booking.place.id", target = "placeId")
    @Mapping(source = "booking.place.nameAr", target = "placeNameAr")
    @Mapping(source = "booking.place.nameEn", target = "placeNameEn")
    @Mapping(target = "placeImageUrl", source = "booking.place", qualifiedByName = "toFullUrl")
    IssueAdminResponseDto issueToIssueAdminDto(Issue issue);

    List<IssueAdminResponseDto> issueToListIssueAdminDto(List<Issue> issues);



    @Named("toFullName")
    default String toFullName(User provider) {
        String firstName = provider.getFirstName() == null ? "" : provider.getFirstName();
        String lastName = provider.getLastName() == null ? "" : provider.getLastName();
        String fullName = (firstName + " " + lastName).trim();
        return fullName.isEmpty() ? null : fullName;
    }

    // 2. في الدالة: استقبل الـ Provider واعمل فحص حماية (Null Check) صريح
    @Named("providerToFullName")
    default String providerToFullName(Provider provider) {
        // إذا كان البروفايدر null أو لم ينجح الهيبيرنيت في جلب الـ user الخاص به، اخرج بأمان بـ null
        if (provider == null || provider.getUser() == null) {
            return null;
        }
        User user = provider.getUser();
        String firstName = user.getFirstName() == null ? "" : user.getFirstName();
        String lastName = user.getLastName() == null ? "" : user.getLastName();
        String fullName = (firstName + " " + lastName).trim();
        return fullName.isEmpty() ? null : fullName;
    }


    @Named("toFullUrl")
    default String toFullUrl(Place place) {
        if (place == null || place.getMainImageUrl() == null)
            return null;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/place_photos/")
                .path(place.getMainImageUrl())
                .toUriString();
    }
}
