@file:JvmName("ExtensionUtils")

package minhna.android.smsreceiver

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * Created by Minh on 2/20/2018.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

operator fun ViewGroup.get(pos: Int): View = getChildAt(pos)
//usage
//val view = viewGroup[2]

//view.snack("Welcome", Snackbar.LENGTH_SHORT, {Toast.makeText(getThis(), "Snack", Toast.LENGTH_SHORT).show()})
inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.getDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

fun AppCompatActivity.getAppCompatDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

