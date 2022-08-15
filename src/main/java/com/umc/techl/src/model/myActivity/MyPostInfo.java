package com.umc.techl.src.model.myActivity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPostInfo {
    private int postParticipationCount;
    private int postCompletionCount;
    private int postAnnouncementCount;
    private int postCommentCount;
}
