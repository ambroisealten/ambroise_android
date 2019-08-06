package com.alten.ambroise.forum.data.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.utils.Nationality;
import com.alten.ambroise.forum.utils.converter.Converters;

import java.util.List;

@Entity(tableName = "applicantForum_table", indices = {@Index(value = "mail", unique = true)})
public class ApplicantForum implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long _id;
    @NonNull
    private String mail;
    @NonNull
    private String surname;
    @NonNull
    private String name;
    private String personInChargeMail;
    private String phoneNumber;
    private String cvPerson;
    @TypeConverters(Converters.class)
    private List<Mobility> mobilities;
    private String contractType;
    private String contractDuration;
    private String startAt;
    private String highestDiploma;
    private String highestDiplomaYear;
    @TypeConverters(Converters.class)
    private List<String> skills;
    private boolean vehicule;
    private boolean driverLicense;
    @TypeConverters(Converters.class)
    private Nationality nationality;
    private String sign;
    private String grade;

    public ApplicantForum() {
    }


    protected ApplicantForum(Parcel in) {
        _id = in.readLong();
        mail = in.readString();
        surname = in.readString();
        name = in.readString();
        personInChargeMail = in.readString();
        phoneNumber = in.readString();
        cvPerson = in.readString();
        mobilities = in.createTypedArrayList(Mobility.CREATOR);
        contractType = in.readString();
        contractDuration = in.readString();
        startAt = in.readString();
        highestDiploma = in.readString();
        highestDiplomaYear = in.readString();
        skills = in.createStringArrayList();
        vehicule = in.readByte() != 0;
        driverLicense = in.readByte() != 0;
        sign = in.readString();
        grade = in.readString();
    }

    public static final Creator<ApplicantForum> CREATOR = new Creator<ApplicantForum>() {
        @Override
        public ApplicantForum createFromParcel(Parcel in) {
            return new ApplicantForum(in);
        }

        @Override
        public ApplicantForum[] newArray(int size) {
            return new ApplicantForum[size];
        }
    };

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

    public List<Mobility> getMobilities() {
        return mobilities;
    }

    public void setMobilities(List<Mobility> mobilities) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "ApplicantForum{" +
                "_id=" + _id +
                ", mail='" + mail + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", personInChargeMail='" + personInChargeMail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", cvPerson='" + cvPerson + '\'' +
                ", mobilities=" + mobilities +
                ", contractType='" + contractType + '\'' +
                ", contractDuration='" + contractDuration + '\'' +
                ", startAt='" + startAt + '\'' +
                ", highestDiploma='" + highestDiploma + '\'' +
                ", highestDiplomaYear='" + highestDiplomaYear + '\'' +
                ", skills=" + skills +
                ", vehicule=" + vehicule +
                ", driverLicense=" + driverLicense +
                ", nationality=" + nationality +
                ", sign='" + sign + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(mail);
        dest.writeString(surname);
        dest.writeString(name);
        dest.writeString(personInChargeMail);
        dest.writeString(phoneNumber);
        dest.writeString(cvPerson);
        dest.writeTypedList(mobilities);
        dest.writeString(contractType);
        dest.writeString(contractDuration);
        dest.writeString(startAt);
        dest.writeString(highestDiploma);
        dest.writeString(highestDiplomaYear);
        dest.writeStringList(skills);
        dest.writeByte((byte) (vehicule ? 1 : 0));
        dest.writeByte((byte) (driverLicense ? 1 : 0));
        dest.writeString(sign);
        dest.writeString(grade);
    }
}
