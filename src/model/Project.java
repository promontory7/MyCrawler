package model;

public class Project {

	public String id;
	public String url;
	public String time;
	public String websiteType; // 网站类型
	public String projectName; // 项目名
	public String projectNo; // 编号
	public String projectBrief; // 项目简介
	public String projectAddress; // 地址
	public String projectType; // 项目类型
	public String projectStage; // 项目阶段
	public String projectPrice; // 造价
	public String tenDerWay; // 招标方式
	public String planCycle; // 计划建设周期
	public String publicStart; // 公告开始时间
	public String publicEnd; // 公告结束时间
	public String finalEnd; // 交标截止时间
	public String owners; // 建设单位
	public String ownerpeopleName; // 建设单位经办人
	public String ownerpeoplePhone; // 经办人电话
	public String agency; // 招标代理机构
	public String agencyName; // 代理机构联系人
	public String agecyPhone; // 代理机构联系人电话
	public String bond; // 保证金
	public String bonder; // 收取保证金单位
	public String isAcceptUnion; // 是否接受联合体投标
	public String qualification_requir; // 投标人资质要求
	public String manager_require; // 项目负责人要求
	public String attach; // 附件地址
	public String article; // 全文
	public String rawHtml;   //html原文
	public int state;// 是否已处理

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWebsiteType() {
		return websiteType;
	}

	public void setWebsiteType(String websiteType) {
		this.websiteType = websiteType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getProjectBrief() {
		return projectBrief;
	}

	public void setProjectBrief(String projectBrief) {
		this.projectBrief = projectBrief;
	}

	public String getProjectAddress() {
		return projectAddress;
	}

	public void setProjectAddress(String projectAddress) {
		this.projectAddress = projectAddress;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectStage() {
		return projectStage;
	}

	public void setProjectStage(String projectStage) {
		this.projectStage = projectStage;
	}

	public String getProjectPrice() {
		return projectPrice;
	}

	public void setProjectPrice(String projectPrice) {
		this.projectPrice = projectPrice;
	}

	public String getTenDerWay() {
		return tenDerWay;
	}

	public void setTenDerWay(String tenDerWay) {
		this.tenDerWay = tenDerWay;
	}

	public String getPlanCycle() {
		return planCycle;
	}

	public void setPlanCycle(String planCycle) {
		this.planCycle = planCycle;
	}

	public String getPublicStart() {
		return publicStart;
	}

	public void setPublicStart(String publicStart) {
		this.publicStart = publicStart;
	}

	public String getPublicEnd() {
		return publicEnd;
	}

	public void setPublicEnd(String publicEnd) {
		this.publicEnd = publicEnd;
	}

	public String getFinalEnd() {
		return finalEnd;
	}

	public void setFinalEnd(String finalEnd) {
		this.finalEnd = finalEnd;
	}

	public String getOwners() {
		return owners;
	}

	public void setOwners(String owners) {
		this.owners = owners;
	}

	public String getOwnerpeopleName() {
		return ownerpeopleName;
	}

	public void setOwnerpeopleName(String ownerpeopleName) {
		this.ownerpeopleName = ownerpeopleName;
	}

	public String getOwnerpeoplePhone() {
		return ownerpeoplePhone;
	}

	public void setOwnerpeoplePhone(String ownerpeoplePhone) {
		this.ownerpeoplePhone = ownerpeoplePhone;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgecyPhone() {
		return agecyPhone;
	}

	public void setAgecyPhone(String agecyPhone) {
		this.agecyPhone = agecyPhone;
	}

	public String getBond() {
		return bond;
	}

	public void setBond(String bond) {
		this.bond = bond;
	}

	public String getBonder() {
		return bonder;
	}

	public void setBonder(String bonder) {
		this.bonder = bonder;
	}

	public String getIsAcceptUnion() {
		return isAcceptUnion;
	}

	public void setIsAcceptUnion(String isAcceptUnion) {
		this.isAcceptUnion = isAcceptUnion;
	}

	public String getQualification_requir() {
		return qualification_requir;
	}

	public void setQualification_requir(String qualification_requir) {
		this.qualification_requir = qualification_requir;
	}

	public String getManager_require() {
		return manager_require;
	}

	public void setManager_require(String manager_require) {
		this.manager_require = manager_require;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	
	public String getRawHtml() {
		return rawHtml;
	}

	public void setRawHtml(String rawHtml) {
		this.rawHtml = rawHtml;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", url=" + url + ", time=" + time + ", websiteType=" + websiteType
				+ ", projectName=" + projectName + ", projectNo=" + projectNo + ", projectBrief=" + projectBrief
				+ ", projectAddress=" + projectAddress + ", projectType=" + projectType + ", projectStage="
				+ projectStage + ", projectPrice=" + projectPrice + ", tenDerWay=" + tenDerWay + ", planCycle="
				+ planCycle + ", publicStart=" + publicStart + ", publicEnd=" + publicEnd + ", finalEnd=" + finalEnd
				+ ", owners=" + owners + ", ownerpeopleName=" + ownerpeopleName + ", ownerpeoplePhone="
				+ ownerpeoplePhone + ", agency=" + agency + ", agencyName=" + agencyName + ", agecyPhone=" + agecyPhone
				+ ", bond=" + bond + ", bonder=" + bonder + ", isAcceptUnion=" + isAcceptUnion
				+ ", qualification_requir=" + qualification_requir + ", manager_require=" + manager_require
				+ ", attach=" + attach + ", article=" + article + ", state=" + state + "]";
	}

}
