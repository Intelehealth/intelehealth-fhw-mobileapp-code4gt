package org.intelehealth.app.ui.patient.data

import android.database.sqlite.SQLiteOpenHelper
import com.github.ajalt.timberkt.Timber
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.ImagesDAO
import org.intelehealth.app.database.dao.ImagesPushDAO
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.database.dao.SyncDAO
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.NetworkConnection
import org.intelehealth.config.presenter.fields.data.RegFieldRepository
import org.intelehealth.config.room.dao.PatientRegFieldDao
import java.util.UUID

/**
 * Created by Vaghela Mithun R. on 02-07-2024 - 13:45.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientRepository(
    private val patientsDao: PatientsDAO,
    private val sqlHelper: SQLiteOpenHelper,
    regFieldDao: PatientRegFieldDao
) : RegFieldRepository(regFieldDao) {
    fun createNewPatient(patient: PatientDTO): Boolean {
        bindPatientAttributes(patient).let {
            val flag = patientsDao.insertPatientToDB(it, it.uuid)
            val flag2 = ImagesDAO().insertPatientProfileImages(it.patientPhoto, it.uuid)
            syncOnServer()
            return flag && flag2
        }
    }

    fun updatePatient(patient: PatientDTO): Boolean {
        return bindPatientAttributes(patient).let {
            val flag = patientsDao.updatePatientToDB(it, it.uuid)
            val flag2 = ImagesDAO().updatePatientProfileImages(it.patientPhoto, it.uuid)
            syncOnServer()
            return@let flag && flag2
        }
    }

    private fun bindPatientAttributes(patient: PatientDTO) = patient.apply {
        patientAttributesDTOList = createPatientAttributes(patient)
        syncd = false
    }

    fun fetchPatient(uuid: String): PatientDTO {
        Timber.d { "uuid => $uuid" }
        PatientQueryBuilder().buildPatientDetailsQuery(uuid).apply {
            Timber.d { "Query => $this" }
            val cursor = sqlHelper.readableDatabase.rawQuery(this, null)
            return patientsDao.retrievePatientDetails(cursor)
        }
    }


    private fun createPatientAttributes(patient: PatientDTO) = arrayListOf<PatientAttributesDTO>()
        .apply {
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.TELEPHONE.value,
                    patient.phonenumber
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.SWD.value,
                    patient.son_dau_wife
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.NATIONAL_ID.value,
                    patient.nationalID
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.OCCUPATION.value,
                    patient.occupation
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.CAST.value,
                    patient.caste
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.EDUCATION.value,
                    patient.education
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.ECONOMIC_STATUS.value,
                    patient.economic
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.CREATED_DATE.value,
                    patient.createdDate
                )
            )
            add(
                createPatientAttribute(
                    patient.uuid,
                    PatientAttributesDTO.Column.PROVIDER_ID.value,
                    patient.providerUUID
                )
            )
        }

    private fun createPatientAttribute(
        patientId: String,
        attrName: String,
        value: String?
    ) = PatientAttributesDTO().apply {
        uuid = UUID.randomUUID().toString()
        patientuuid = patientId
        personAttributeTypeUuid = patientsDao.getUuidForAttribute(attrName)
        this.value = value
    }

    private fun updatePatientAttribute(
        patientId: String,
        attrName: String,
        value: String?
    ) = PatientAttributesDTO().apply {
        uuid = UUID.randomUUID().toString()
        patientuuid = patientId
        personAttributeTypeUuid = patientsDao.getUuidForAttribute(attrName)
        this.value = value
    }

    fun syncOnServer() {
        if (NetworkConnection.isOnline(IntelehealthApplication.getAppContext())) {
            val syncDAO = SyncDAO()
            val imagesPushDAO = ImagesPushDAO()
            syncDAO.pushDataApi()
            imagesPushDAO.patientProfileImagesPush()
        }
    }
}