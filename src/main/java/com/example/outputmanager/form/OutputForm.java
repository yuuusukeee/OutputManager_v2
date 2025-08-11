package com.example.outputmanager.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.outputmanager.domain.Output;

import lombok.Data;

@Data
public class OutputForm {
	private Integer id;
    @NotBlank @Size(max=50)
    private String title;
    @Size(max=50)
    private String summary;
    @Size(max=2000)
    private String detail;
    @NotNull
    private Integer categoryId;
    @Size(max=100)
    private String icon;
    @Size(max=200)
    private String videoUrl;

    public Output toEntity(Integer userId) {
        Output o = new Output();
        if (this.id != null) o.setId(this.id);
        o.setUserId(userId);
        o.setCategoryId(this.categoryId);
        o.setTitle(this.title);
        o.setSummary(this.summary);
        o.setDetail(this.detail);
        o.setIcon(this.icon);
        o.setVideoUrl(this.videoUrl);
        return o;
    }

    public static OutputForm fromEntity(Output o) {
        OutputForm f = new OutputForm();
        f.setId(o.getId());  
        f.setTitle(o.getTitle());
        f.setSummary(o.getSummary());
        f.setDetail(o.getDetail());
        f.setCategoryId(o.getCategoryId());
        f.setIcon(o.getIcon());
        f.setVideoUrl(o.getVideoUrl());
        return f;
    }
}
