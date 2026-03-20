package com.kandaovr.mvplibrary.base

import android.app.Activity
import android.os.Bundle

// ===================== 第一步：定义通用MVP契约接口 =====================
/**
 * 通用MVP - View层基接口（所有View都需实现）
 */
interface IBaseView

/**
 * 通用MVP - Presenter层基接口（所有Presenter都需实现）
 */
interface IBasePresenter<in V : IBaseView> {
    /**
     * 绑定View（建议传入弱引用）
     */
    fun attachView(view: V)

    /**
     * 解绑View（必须在Activity/Fragment销毁时调用）
     */
    fun detachView()
}

/**
 * 通用MVP - Model层基接口（所有Model都需实现）
 */
interface IBaseModel


/**
 * MVP架构Activity基类
 * @param V View层接口（需继承IBaseView）
 * @param P Presenter层实现类（需继承IBasePresenter<V>）
 * @param M Model层实现类（需继承IBaseModel）
 */
abstract class BaseMvpActivity<V : IBaseView, P : IBasePresenter<V>> : Activity(),
    IBaseView {

    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = getLayoutResId()
        if (layoutId != 0) {
            setContentView(layoutId)
        }
        presenter = createPresenter()
        presenter.attachView(this as V)
        initView()
    }

    protected abstract fun createPresenter(): P

    protected abstract fun getLayoutResId(): Int
    protected abstract fun initView();

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


}