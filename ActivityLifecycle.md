# Activity LifeCycle #

![http://developer.android.com/images/training/basics/basic-lifecycle.png](http://developer.android.com/images/training/basics/basic-lifecycle.png)

**onPause()** - still partially visible.

You should usually use the **onPause()** callback to:

  * Stop animations or other ongoing actions that could consume CPU.
  * Commit unsaved changes, but only if users expect such changes to be permanently saved when they leave (such as a draft email).
  * Release system resources, such as broadcast receivers, handles to sensors (like GPS), or any resources that may affect battery life while your activity is paused and the user does not need them.

```
@Override
public void onPause() {
    super.onPause();  // Always call the superclass method first

    // Release the Camera because we don't need it when paused
    // and other activities might need to use it.
    if (mCamera != null) {
        mCamera.release()
        mCamera = null;
    }
}
```

**onResume()** - resume activity from the Passed state.

Be aware that the system calls this method every time your activity comes into the foreground, including when it's created for the first time.

```
@Override
public void onResume() {
    super.onResume();  // Always call the superclass method first

    // Get the Camera instance as the activity achieves full user focus
    if (mCamera == null) {
        initializeCamera(); // Local method to handle camera init
    }
}
```

**onStop()** - there are a few of key scenarios in which your activity is stopped and restarted:
  * The user opens the Recent Apps window and switches from your app to another app. The activity in your app that's currently in the foreground is stopped. If the user returns to your app from the Home screen launcher icon or the Recent Apps window, the activity restarts.
  * The user performs an action in your app that starts a new activity. The current activity is stopped when the second activity is created. If the user then presses the Back button, the first activity is restarted.
  * The user receives a phone call while using your app on his or her phone.

![http://developer.android.com/images/training/basics/basic-lifecycle-stopped.png](http://developer.android.com/images/training/basics/basic-lifecycle-stopped.png)

So it's important you use **onStop()** to release resources that might _leak memory_.