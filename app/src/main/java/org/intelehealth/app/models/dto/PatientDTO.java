
package org.intelehealth.app.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PatientDTO implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("openmrs_id")
    @Expose
    private String openmrsId;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("middlename")
    @Expose
    private String middlename;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("dateofbirth")
    @Expose
    private String dateofbirth;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("cityvillage")
    @Expose
    private String cityvillage;
    @SerializedName("stateprovince")
    @Expose
    private String stateprovince;
    @SerializedName("postalcode")
    @Expose
    private String postalcode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("economic")
    @Expose
    private String economic;
    @SerializedName("gender")
    @Expose
    private String gender;
    private String patientPhoto;
    private List<PatientAttributesDTO> patientAttributesDTOList;
    @SerializedName("dead")
    @Expose
    private Integer dead;
    @SerializedName("syncd")
    @Expose
    private Boolean syncd;
    private String son_dau_wife;
    private String occupation;
    private String nationalID;
    private String caste;
    private String createdDate;
    private String providerUUID;

    // for search tags...
    private boolean emergency = false;
    private String visit_startdate;
    private boolean prescription_exists = false;

    private String guardianName;
    private String guardianType;
    private String contactType;
    private String emContactName;
    private String emContactNumber;

    private VisitDTO visitDTO;

    private String profileTimestamp;

    private String district;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOpenmrsId() {
        return openmrsId;
    }

    public void setOpenmrsId(String openmrsId) {
        this.openmrsId = openmrsId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getMobileNumber() {
//        if (phonenumber != null && phonenumber.length() == 13) {
//            return phonenumber.substring(3);
//        }
        return phonenumber;
    }

    public String getCountryCode() {
        if (phonenumber != null && phonenumber.length() == 13) {
            return phonenumber.substring(0, 3);
        }
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCityvillage() {
        return cityvillage;
    }

    public void setCityvillage(String cityvillage) {
        this.cityvillage = cityvillage;
    }

    public String getStateprovince() {
        return stateprovince;
    }

    public void setStateprovince(String stateprovince) {
        this.stateprovince = stateprovince;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getDead() {
        return dead;
    }

    public void setDead(Integer dead) {
        this.dead = dead;
    }

    public Boolean getSyncd() {
        return syncd;
    }

    public void setSyncd(Boolean syncd) {
        this.syncd = syncd;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEconomic() {
        return economic;
    }

    public void setEconomic(String economic) {
        this.economic = economic;
    }

    public List<PatientAttributesDTO> getPatientAttributesDTOList() {
        return patientAttributesDTOList;
    }


    public void setPatientAttributesDTOList(List<PatientAttributesDTO> patientAttributesDTOList) {
        this.patientAttributesDTOList = patientAttributesDTOList;
    }

    public String getPatientPhoto() {
        return patientPhoto;
    }

    public void setPatientPhoto(String patientPhoto) {
        this.patientPhoto = patientPhoto;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public String getVisit_startdate() {
        return visit_startdate;
    }

    public void setVisit_startdate(String visit_startdate) {
        this.visit_startdate = visit_startdate;
    }

    public boolean isPrescription_exists() {
        return prescription_exists;
    }

    public void setPrescription_exists(boolean prescription_exists) {
        this.prescription_exists = prescription_exists;
    }

    public String getSon_dau_wife() {
        return son_dau_wife;
    }

    public void setSon_dau_wife(String son_dau_wife) {
        this.son_dau_wife = son_dau_wife;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProviderUUID() {
        return providerUUID;
    }

    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public VisitDTO getVisitDTO() {
        return visitDTO;
    }

    public void setVisitDTO(VisitDTO visitDTO) {
        this.visitDTO = visitDTO;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianType() {
        return guardianType;
    }

    public void setGuardianType(String guardianType) {
        this.guardianType = guardianType;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getEmContactName() {
        return emContactName;
    }

    public void setEmContactName(String emContactName) {
        this.emContactName = emContactName;
    }

    public String getEmContactNumber() {
        return emContactNumber;
    }

    public void setEmContactNumber(String emContactNumber) {
        this.emContactNumber = emContactNumber;
    }

    public String getEmMobileNumber() {
        if (emContactNumber != null && emContactNumber.length() == 13) {
            return emContactNumber.substring(3);
        }
        return emContactNumber;
    }

    public String getEmCountryCode() {
        if (emContactNumber != null && emContactNumber.length() == 13) {
            return emContactNumber.substring(0, 3);
        }
        return emContactNumber;
    }

    public String getProfileTimestamp() {
        return profileTimestamp;
    }

    public void setProfileTimestamp(String profileTimestamp) {
        this.profileTimestamp = profileTimestamp;
    }

    public String getDistrict() {
        if (district != null && !district.isEmpty()) return district;
        else return splitVillageAndDistrict(0);
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillageWithoutDistrict() {
        return splitVillageAndDistrict(1);
    }

    private String splitVillageAndDistrict(int index) {
        if (cityvillage != null && !cityvillage.isEmpty() && cityvillage.contains(":")) {
            return cityvillage.split(":")[index];
        }
        if (index == 1) return cityvillage;
        else return null;
    }

    @Override
    public String toString() {
        return "PatientDTO{" +
                "uuid='" + uuid + '\'' +
                ", openmrsId='" + openmrsId + '\'' +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", lastname='" + lastname + '\'' +
                ", dateofbirth='" + dateofbirth + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", address2='" + address2 + '\'' +
                ", address1='" + address1 + '\'' +
                ", cityvillage='" + cityvillage + '\'' +
                ", stateprovince='" + stateprovince + '\'' +
                ", postalcode='" + postalcode + '\'' +
                ", country='" + country + '\'' +
                ", education='" + education + '\'' +
                ", economic='" + economic + '\'' +
                ", gender='" + gender + '\'' +
                ", patientPhoto='" + patientPhoto + '\'' +
                ", patientAttributesDTOList=" + patientAttributesDTOList +
                ", dead=" + dead +
                ", syncd=" + syncd +
                ", emergency=" + emergency +
                ", visit_startdate='" + visit_startdate + '\'' +
                ", prescription_exists=" + prescription_exists +
                ", guardianType='" + guardianType + '\'' +
                ", guardianName='" + guardianName + '\'' +
                ", contactType='" + contactType + '\'' +
                ", emContactName='" + emContactName + '\'' +
                ", emContactNumber=" + emContactNumber +
                '}';
    }
}