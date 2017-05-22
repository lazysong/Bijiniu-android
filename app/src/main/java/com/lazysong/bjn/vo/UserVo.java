package com.lazysong.bjn.vo;

public class UserVo {

    private int userId;
    private String nickName;
    private String headUrl;
    private String school;
    private int schoolId;
    private int facultyId;
    private String faculty;//院系
    private String academic;//学位
    private String academiclevel;//入学年份
    
    private int fans;// 粉丝数量
    private int downloadNumber;// 下载量
    private int viewNumber;// 浏览量

    private int materials;// 资料数量
    private int stars;// 关注数量
    private double noteScore;// 文档得分

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getAcademic() {
		return academic;
	}

	public void setAcademic(String academic) {
		this.academic = academic;
	}

	public String getAcademiclevel() {
		return academiclevel;
	}

	public void setAcademiclevel(String academiclevel) {
		this.academiclevel = academiclevel;
	}

	public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getDownloadNumber() {
        return downloadNumber;
    }

    public void setDownloadNumber(int downloadNumber) {
        this.downloadNumber = downloadNumber;
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public int getMaterials() {
        return materials;
    }

    public void setMaterials(int materials) {
        this.materials = materials;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public double getNoteScore() {
        return noteScore;
    }

    public void setNoteScore(double noteScore) {
        this.noteScore = noteScore;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

}
