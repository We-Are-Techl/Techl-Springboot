package com.umc.techl.src.model.postActivity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostActivityRes {
    private int participationCount;
    private int completionCount;
    private int announcementCount;
    private int commentCount;
    private List<GetMyPostParticipation> getMyPostParticipation;
    private List<GetMyPostCompletion> getMyPostCompletion;
    private List<GetMyPostAnnouncememt> getMyPostAnnouncememt;
    private List<GetMyPostComment> getMyPostComment;
}
