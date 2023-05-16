package ch.zhaw.freelancer4u.model;

import com.mongodb.lang.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JobCreateDTO {

    @NonNull
    private String description;
    @NonNull
    private JobType jobType;
    @NonNull
    private Double earnings;
}
