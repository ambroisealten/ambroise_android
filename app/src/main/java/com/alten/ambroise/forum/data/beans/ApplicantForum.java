package com.alten.ambroise.forum.data.beans;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.alten.ambroise.forum.data.Nationality;

import java.util.List;

@Entity(tableName = "applicantForum_table",indices = {@Index(value = "mail", unique = true)})
public class ApplicantForum {

    @PrimaryKey(autoGenerate = true)
    private long _id;
    @NonNull
    private String mail;
    @NonNull
    private String surname;
    @NonNull
    private String name;
    @NonNull
    private String personInChargeMail;
    private String phoneNumber;
    private String cvPerson;
    private List<String> mobilities;
    private String contractType;
    private String contractDuration;
    private String startAt;
    private String highestDiploma;
    private String highestDiplomaYear;
    private List<String> skills;
    private boolean vehicule;
    private boolean driverLicense;
    private Nationality nationality;
    private String sign;
    private String grade;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    @NonNull
    public String getMail() {
        return mail;
    }

    public void setMail(@NonNull String mail) {
        this.mail = mail;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    public void setSurname(@NonNull String surname) {
        this.surname = surname;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getPersonInChargeMail() {
        return personInChargeMail;
    }

    public void setPersonInChargeMail(@NonNull String personInChargeMail) {
        this.personInChargeMail = personInChargeMail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCvPerson() {
        return cvPerson;
    }

    public void setCvPerson(String cvPerson) {
        this.cvPerson = cvPerson;
    }

    public List<String> getMobilities() {
        return mobilities;
    }

    public void setMobilities(List<String> mobilities) {
        this.mobilities = mobilities;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(String contractDuration) {
        this.contractDuration = contractDuration;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getHighestDiploma() {
        return highestDiploma;
    }

    public void setHighestDiploma(String highestDiploma) {
        this.highestDiploma = highestDiploma;
    }

    public String getHighestDiplomaYear() {
        return highestDiplomaYear;
    }

    public void setHighestDiplomaYear(String highestDiplomaYear) {
        this.highestDiplomaYear = highestDiplomaYear;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public boolean isVehicule() {
        return vehicule;
    }

    public void setVehicule(boolean vehicule) {
        this.vehicule = vehicule;
    }

    public boolean isDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(boolean driverLicense) {
        this.driverLicense = driverLicense;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
