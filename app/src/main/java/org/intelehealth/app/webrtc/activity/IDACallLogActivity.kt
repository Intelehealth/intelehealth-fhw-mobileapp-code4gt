package org.intelehealth.app.webrtc.activity

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.intelehealth.app.R
import org.intelehealth.app.database.dao.VisitsDAO
import org.intelehealth.app.databinding.ActivityCallLogBinding
import org.intelehealth.app.webrtc.adapter.CallLogAdapter
import org.intelehealth.klivekit.call.model.RtcCallLog
import org.intelehealth.klivekit.call.ui.activity.CoreCallLogActivity
import org.intelehealth.klivekit.chat.ui.adapter.viewholder.BaseViewHolder
import org.intelehealth.klivekit.model.RtcArgs

/**
 * Created by Vaghela Mithun R. on 23-10-2023 - 14:44.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class IDACallLogActivity : CoreCallLogActivity(), BaseViewHolder.ViewHolderClickListener {

    private lateinit var binding: ActivityCallLogBinding
    private lateinit var adapter: CallLogAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCallLogBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        adapter = CallLogAdapter(this, arrayListOf())
        adapter.clickListener = this
    }

    override fun onLogs(logs: List<RtcCallLog>) {
        binding.callLogContent.tvCallLogEmptyMessage.isVisible = false
        binding.callLogContent.rvCallLogs.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    this@IDACallLogActivity, DividerItemDecoration.VERTICAL
                )
            )
            this@IDACallLogActivity.adapter.updateItems(logs.toMutableList())
            adapter = this@IDACallLogActivity.adapter
        }
    }

    override fun setupActionBar() {
        setSupportActionBar(binding.callLogAppBar.toolbar)
        super.setupActionBar()
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.call_logs)
        }
        binding.callLogAppBar.toolbar.setNavigationOnClickListener { finishAfterTransition() }
    }

    override fun onViewHolderViewClicked(view: View?, position: Int) {
        view ?: return
        if (view.id == R.id.btnCallLogChat) {
            startChatActivity(adapter.getItem(position))
        }
    }

    private fun startChatActivity(callLog: RtcCallLog) {
        val args = RtcArgs()
        args.doctorUuid = callLog.callerId
        args.patientId = callLog.roomId
        args.patientName = callLog.roomName
        args.visitId = VisitsDAO().getVisitIdByPatientId(args.patientId)
        args.nurseId = callLog.calleeId
        IDAChatActivity.startChatActivity(this, args)
    }
}