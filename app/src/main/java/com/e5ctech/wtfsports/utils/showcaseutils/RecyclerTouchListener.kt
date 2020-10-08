import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.e5ctech.wtfsports.utils.showcaseutils.ClickListener

internal class RecyclerTouchListener(context: Context, recycleView:RecyclerView, clicklistener: ClickListener):RecyclerView.OnItemTouchListener {
    private val clicklistener:ClickListener
    private val gestureDetector: GestureDetector
    init{
        this.clicklistener = clicklistener
        gestureDetector = GestureDetector(context, object:GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e:MotionEvent):Boolean {
                return true
            }
            override fun onLongPress(e:MotionEvent) {
                val child = recycleView.findChildViewUnder(e.getX(), e.getY())
                if (child != null && clicklistener != null)
                {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child))
                }
            }
        })
    }
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent):Boolean {
        val child = rv.findChildViewUnder(e.getX(), e.getY())
        if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e))
        {
            clicklistener.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }
    override fun onTouchEvent(rv:RecyclerView, e:MotionEvent) {
    }
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept:Boolean) {
    }
}