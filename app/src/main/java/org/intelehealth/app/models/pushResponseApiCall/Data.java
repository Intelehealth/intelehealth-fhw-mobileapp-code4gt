
package org.intelehealth.app.models.pushResponseApiCall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("personList")
    @Expose
    private List<PersonList> personList = null;

    @SerializedName("patientlist")
    @Expose
    private List<Patientlist> patientlist = null;

    @SerializedName("visitlist")
    @Expose
    private List<Visitlist> visitlist = null;

    @SerializedName("encounterlist")
    @Expose
    private List<Encounterlist> encounterlist = null;

    @SerializedName("AppointmentList")
    @Expose
    private List<AppointmentList> appointmentList = null;

    public List<ProviderList> getProviderlist() {
        return providerlist;
    }

    public void setProviderlist(List<ProviderList> providerlist) {
        this.providerlist = providerlist;
    }

    @SerializedName("providerlist")
    @Expose
    private List<ProviderList> providerlist = null;

    public List<PersonList> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonList> personList) {
        this.personList = personList;
    }

    public List<Patientlist> getPatientlist() {
        return patientlist;
    }

    public void setPatientlist(List<Patientlist> patientlist) {
        this.patientlist = patientlist;
    }

    public List<Visitlist> getVisitlist() {
        return visitlist;
    }

    public void setVisitlist(List<Visitlist> visitlist) {
        this.visitlist = visitlist;
    }

    public List<Encounterlist> getEncounterlist() {
        return encounterlist;
    }

    public void setEncounterlist(List<Encounterlist> encounterlist) {
        this.encounterlist = encounterlist;
    }

    public List<AppointmentList> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<AppointmentList> appointmentList) {
        this.appointmentList = appointmentList;
    }
}
