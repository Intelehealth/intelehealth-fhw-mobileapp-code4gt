package org.intelehealth.app.appointmentNew.MyAppointmentNew

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.intelehealth.app.R
import org.intelehealth.app.app.AppConstants
import org.intelehealth.app.appointment.model.AppointmentInfo
import org.intelehealth.app.appointmentNew.AppointmentDetailsActivity
import org.intelehealth.app.database.dao.ImagesDAO
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.utilities.CustomLog
import org.intelehealth.app.utilities.DateAndTimeUtils
import org.intelehealth.app.utilities.DownloadFilesUtils
import org.intelehealth.app.utilities.Logger
import org.intelehealth.app.utilities.NetworkConnection
import org.intelehealth.app.utilities.SessionManager
import org.intelehealth.app.utilities.StringUtils
import org.intelehealth.app.utilities.UrlModifiers
import org.intelehealth.app.utilities.exception.DAOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PastMyAppointmentsAdapter(
    var context: Context,
    private var appointmentInfoList: List<AppointmentInfo>,
) : RecyclerView.Adapter<PastMyAppointmentsAdapter.MyViewHolder?>() {
    var sessionManager: SessionManager = SessionManager(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_past_appointments_ui2_new, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        CustomLog.d(TAG, "onBindViewHolder: appointmentInfoList : " + appointmentInfoList.size)
        try {
            val appointmentInfoModel = appointmentInfoList[position]
            if (appointmentInfoModel.patientProfilePhoto == null || appointmentInfoModel.patientProfilePhoto.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                if (NetworkConnection.isOnline(context)) {
                    profilePicDownloaded(appointmentInfoModel, holder)
                }
            }
            StringUtils.setGenderAgeLocalByCommaContact(
                context, holder.search_gender, appointmentInfoModel.patientDob,
                appointmentInfoModel.patientGender, sessionManager
            )
            if (appointmentInfoModel.patientProfilePhoto != null && !appointmentInfoModel.patientProfilePhoto.isEmpty()) {
                val requestBuilder = Glide.with(holder.itemView.context)
                    .asDrawable().sizeMultiplier(0.3f)
                Glide.with(context)
                    .load(appointmentInfoModel.patientProfilePhoto)
                    .thumbnail(requestBuilder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.ivProfileImage)
            } else {
                holder.ivProfileImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.avatar1
                    )
                )
            }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
            val currentDateTime = dateFormat.format(Date())
            val slottime = appointmentInfoModel.slotDate + " " + appointmentInfoModel.slotTime
            var diff: Long = 0
            try {
                diff = (dateFormat.parse(slottime)?.time
                    ?: 0) - (dateFormat.parse(currentDateTime)?.time
                    ?: 0)
                val second = diff / 1000
                val minutes = second / 60
                Log.v("AppointmentInfo", "Diff minutes - $minutes")
                var timeText: String? = ""
                //check for appointmet but presc not given and visit not completed
                holder.tvPatientName.text = appointmentInfoModel.patientName
                timeText =
                    DateAndTimeUtils.convertDateToDdMmYyyyHhMmFormat(
                        appointmentInfoModel.slotDate,
                        appointmentInfoModel.slotTime
                    )

                holder.tvDate.text = timeText
                holder.tvDate.setTextColor(context.getColor(R.color.iconTintGray))
                holder.tvDate.setCompoundDrawables(null, null, null, null)
                if (appointmentInfoModel.status.equals("completed", ignoreCase = true)) {
                    holder.status_tv.text = ContextCompat.getString(
                        context, R.string.completed
                    )
                    holder.status_tv.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.colorPrimary1
                        )
                    )
                    if (appointmentInfoModel.isPrescription_exists) {
                        holder.cvPrescRx.visibility = View.VISIBLE
                        holder.cvPrescPending.visibility = View.GONE
                    } else {
                        holder.cvPrescPending.visibility = View.VISIBLE
                        holder.cvPrescRx.visibility = View.GONE
                    }
                } else if (appointmentInfoModel.status.equals("cancelled", ignoreCase = true)) {
                    holder.status_tv.text = ContextCompat.getString(
                        context, R.string.cancelled
                    )
                    holder.status_tv.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.red
                        )
                    )
                } else {
                    holder.status_tv.text = ContextCompat.getString(
                        context, R.string.missed
                    )
                    holder.status_tv.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.red
                        )
                    )
                }
            } catch (e: ParseException) {
                CustomLog.d(TAG, "onBindViewHolder: date exce : " + e.localizedMessage)
                e.printStackTrace()
            }
        } catch (e: Exception) {
            CustomLog.d(TAG, "onBindViewHolder: e main : " + e.localizedMessage)
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return appointmentInfoList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardParent: CardView
        var cvPrescPending: CardView
        var cvPrescRx: CardView
        var tvPatientName: TextView
        var tvDate: TextView
        var search_gender: TextView
        var status_tv: TextView
        var ivProfileImage: ImageView

        init {
            cardParent = itemView.findViewById(R.id.card_todays_appointments1)
            tvPatientName = itemView.findViewById(R.id.tv_patient_name_todays)
            ivProfileImage = itemView.findViewById(R.id.profile_image_todays)
            tvDate = itemView.findViewById(R.id.tv_date_appointment_todays)
            cvPrescPending = itemView.findViewById(R.id.cvPrescPendingTodayAppointment)
            cvPrescRx = itemView.findViewById(R.id.cvPrescRxTodayAppointment)
            search_gender = itemView.findViewById(R.id.search_gender)
            status_tv = itemView.findViewById(R.id.status_tv)

            cardParent.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val appointmentInfoModel = appointmentInfoList[position]
                    val intent = Intent(context, AppointmentDetailsActivity::class.java).apply {
                        putExtra("patientname", appointmentInfoModel.patientName)
                        putExtra("patientUuid", appointmentInfoModel.patientId)
                        putExtra("gender", "")
                        putExtra("dob", appointmentInfoModel.patientDob)
                        putExtra("age", "")
                        putExtra("priority_tag", "")
                        putExtra("hasPrescription", appointmentInfoModel.isPrescription_exists)
                        putExtra("openmrsID", appointmentInfoModel.openMrsId)
                        putExtra("visit_ID", appointmentInfoModel.visitUuid)
                        putExtra("visit_startDate", "")
                        putExtra("patient_photo", appointmentInfoModel.patientProfilePhoto)
                        putExtra("app_start_date", appointmentInfoModel.slotDate)
                        putExtra("app_start_time", appointmentInfoModel.slotTime)
                        putExtra("visit_speciality", appointmentInfoModel.speciality)
                        putExtra("appointment_id", appointmentInfoModel.id)
                        putExtra("app_start_day", appointmentInfoModel.slotDay)
                        putExtra(
                            "prescription_received_time",
                            DateAndTimeUtils.getDisplayDateAndTime(
                                appointmentInfoModel.presc_received_time,
                                context
                            )
                        )
                        putExtra("status", appointmentInfoModel.status)
                    }
                    context.startActivity(intent)
                }
            }
        }

    }

    fun profilePicDownloaded(model: AppointmentInfo, holder: MyViewHolder) {
        val urlModifiers = UrlModifiers()
        val url = urlModifiers.patientProfileImageUrl(model.uuid)
        val profilePicDownload = AppConstants.apiInterface.PERSON_PROFILE_PIC_DOWNLOAD(
            url,
            "Basic " + sessionManager.encoded
        )
        profilePicDownload.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(file: ResponseBody) {
                    val downloadFilesUtils = DownloadFilesUtils()
                    downloadFilesUtils.saveToDisk(file, model.uuid)
                    Logger.logD("TAG", file.toString())
                }

                override fun onError(e: Throwable) {
                    Logger.logD("TAG", e.message)
                }

                override fun onComplete() {
                    Logger.logD("TAG", "complete" + model.patientProfilePhoto)
                    val patientsDAO = PatientsDAO()
                    var updated = false
                    try {
                        updated = patientsDAO.updatePatientPhoto(
                            model.uuid,
                            AppConstants.IMAGE_PATH + model.uuid + ".jpg"
                        )
                    } catch (e: DAOException) {
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                    if (updated) {
                        val requestBuilder = Glide.with(holder.itemView.context)
                            .asDrawable().sizeMultiplier(0.3f)
                        Glide.with(context)
                            .load(AppConstants.IMAGE_PATH + model.uuid + ".jpg")
                            .thumbnail(requestBuilder)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(holder.ivProfileImage)
                    }
                }
            })
    }

    companion object {
        private const val TAG = "TodaysMyAppointmentsAda"
    }
}