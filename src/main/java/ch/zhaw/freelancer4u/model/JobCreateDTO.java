package ch.zhaw.freelancer4u.model;

import com.mongodb.lang.NonNull;

import lombok.Getter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class JobCreateDTO {


    @NonNull
    private String description;
    @NonNull
    private Double earnings;
    @NonNull
    private JobType jobType;
}
