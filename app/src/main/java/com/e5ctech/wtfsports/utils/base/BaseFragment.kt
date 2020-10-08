package com.e5ctech.wtfsports.utils.base;

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.utils.showcaseutils.CShowProgress


abstract class BaseFragment : Fragment() {

    private var parentActivity: BaseActivity? = null

    private lateinit var sPref: SharedPreferences
    private lateinit var sEditor: SharedPreferences.Editor
    private lateinit var preferenceName: String
    internal lateinit var cShowProgress: CShowProgress

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.parentActivity = activity
            activity?.onAttachFragment(this)
        }
    }

    init {
        if (context != null) {
            sPref = requireContext().getSharedPreferences("com.e5ctech.wtfsports.sharedpref", Context.MODE_PRIVATE)
            sEditor = sPref.edit()

        }
    }

    fun PreferenceStore(name: String){
        this.preferenceName = name
        this.sPref = this.requireContext().getSharedPreferences(this.preferenceName, Context.MODE_PRIVATE)
    }

    fun get(): SharedPreferences{
        return this.sPref
    }

    fun edit(): SharedPreferences.Editor {
        return this.sPref.edit()
    }

    fun save(editor: SharedPreferences.Editor): Boolean {
        return editor.commit()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    fun setToolbar(toolbar: Toolbar, resId: Int) {
            toolbar.setNavigationIcon(resId)
            toolbar.title = getString(R.string.app_name)
            toolbar.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
    }

    fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    fun showProgressDialog() {
        cShowProgress = CShowProgress()
        cShowProgress.getInstance(getBaseActivity()!!)
        cShowProgress.showProgress()
    }

    fun dismissProgressDialog() {
        cShowProgress.hideProgress()
    }

    fun getBaseActivity() = parentActivity

    interface CallBack {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

    abstract fun setUp()
}