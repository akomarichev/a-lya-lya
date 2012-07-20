package vk.chat;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Point;
//import android.graphics.drawable.Drawable;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.widget.Button;
//import android.widget.Toast;
//import android.widget.ZoomControls;
//
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.ItemizedOverlay;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapController;
//import com.google.android.maps.MapView;
//import com.google.android.maps.Overlay;
//import com.google.android.maps.OverlayItem;
//
//public class GMapsActivity extends MapActivity
//{    
//     GeoPoint p;
//     List<Overlay> listOfOverlays;
//     //MapOverlay mapOverlay;
//     
//     private Button takeLocation;
//     private String path;
//
//     private MapView mapView;
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) 
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map2);
//
//        takeLocation = (Button) findViewById(R.id.b_take_location);
//        takeLocation.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				try {
//					saveMapImage();
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				Intent intent = new Intent();
//				intent.putExtra("path", path);
//			    setResult(RESULT_OK, intent);
//			    finish();
//			}
//		});
//        
//        mapView = (MapView) findViewById(R.id.mapview);
//        mapView.setBuiltInZoomControls(true);
//
//        MapOverlay mapOverlay = new MapOverlay();
//        List<Overlay> listOfOverlays = mapView.getOverlays();
//        listOfOverlays.add(mapOverlay);
//    }
//
//
//    private class MapOverlay extends Overlay
//    {
//        @Override
//        public boolean onTouchEvent(MotionEvent event, MapView mapView)
//        {
//            if (event.getAction() == 1) {
//                GeoPoint p = mapView.getProjection().fromPixels(
//                    (int) event.getX(),
//                    (int) event.getY());
//                    Toast.makeText(getBaseContext(),
//                        "Location: "+
//                        p.getLatitudeE6() / 1E6 + "," +
//                        p.getLongitudeE6() /1E6 ,
//                        Toast.LENGTH_SHORT).show();
//                    List<Overlay> listOfOverlays = mapView.getOverlays();
//                    Drawable drawable =
//                        getResources().getDrawable(R.drawable.attach_location_lable);
//            }
//            return false;
//        }
//    }


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vk.utils.WorkWithTimeAndDate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
public class GMapsActivity extends MapActivity {
  private MapView map=null;
  private MyLocationOverlay me=null;
  private String path = "";
  private Button takeLocation;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map2);
    
    takeLocation = (Button) findViewById(R.id.b_take_location);
    takeLocation.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			try {
				saveMapImage();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent();
			intent.putExtra("path", path);
		    setResult(RESULT_OK, intent);
		    finish();
		}
	});
    
    map=(MapView)findViewById(R.id.mapview);
    
    map.getController().setCenter(getPoint(21.0000169992044,
                                            78.0000484771729));
    map.setBuiltInZoomControls(true);
   
    
    Drawable marker=getResources().getDrawable(R.drawable.map_location);
    
    marker.setBounds(0, 0, marker.getIntrinsicWidth(),marker.getIntrinsicHeight());
    
    map.getOverlays().add(new SitesOverlay(marker));
    
    me=new MyLocationOverlay(this, map);
    map.getOverlays().add(me);
  }
  
  @Override
  public void onResume() {
    super.onResume();
    
    me.enableCompass();
  }   
  
  @Override
  public void onPause() {
    super.onPause();
    
    me.disableCompass();
  }   
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_S) {
      map.setSatellite(!map.isSatellite());
      return(true);
    }
    else if (keyCode == KeyEvent.KEYCODE_Z) {
      map.displayZoomControls(true);
      return(true);
    }
    
    return(super.onKeyDown(keyCode, event));
  }
  private GeoPoint getPoint(double lat, double lon) {
    return(new GeoPoint((int)(lat*1000000.0),
                          (int)(lon*1000000.0)));
  }
    
  private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
    private List<OverlayItem> items=new ArrayList<OverlayItem>();
    private Drawable marker=null;
    private OverlayItem inDrag=null;
    private ImageView dragImage=null;
    private int xDragImageOffset=0;
    private int yDragImageOffset=0;
    private int xDragTouchOffset=0;
    private int yDragTouchOffset=0;
    
    public SitesOverlay(Drawable marker) {
      super(marker);
      this.marker=marker;
      
      dragImage=(ImageView)findViewById(R.id.drag);
      xDragImageOffset=dragImage.getDrawable().getIntrinsicWidth()/2;
      yDragImageOffset=dragImage.getDrawable().getIntrinsicHeight();
      
      items.add(new OverlayItem(getPoint(21.169992044,
                                                                78.484771729),
                                "Mumbai",
                                "Maharashtra, India"));
      populate();
    }
    
    @Override
    protected OverlayItem createItem(int i) {
      return(items.get(i));
    }
    
    @Override
    public void draw(Canvas canvas, MapView mapView,
                      boolean shadow) {
      super.draw(canvas, mapView, shadow);
      
      boundCenterBottom(marker);
    }
    
    @Override
    public int size() {
      return(items.size());
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
      final int action=event.getAction();
      final int x=(int)event.getX();
      final int y=(int)event.getY();
      boolean result=false;
      
      if (action==MotionEvent.ACTION_DOWN) {
        for (OverlayItem item : items) {
          Point p=new Point(0,0);
          
          map.getProjection().toPixels(item.getPoint(), p);
          
          if (hitTest(item, marker, x-p.x, y-p.y)) {
            result=true;
            inDrag=item;
            items.remove(inDrag);
            populate();
            xDragTouchOffset=0;
            yDragTouchOffset=0;
            
            setDragImagePosition(p.x, p.y);
            dragImage.setVisibility(View.VISIBLE);
            xDragTouchOffset=x-p.x;
            yDragTouchOffset=y-p.y;
            
            break;
          }
        }
      }
      else if (action==MotionEvent.ACTION_MOVE && inDrag!=null) {
        setDragImagePosition(x, y);
        result=true;
      }
      else if (action==MotionEvent.ACTION_UP && inDrag!=null) {
        dragImage.setVisibility(View.GONE);
        
        GeoPoint pt=map.getProjection().fromPixels(x-xDragTouchOffset,
                                                   y-yDragTouchOffset);
        OverlayItem toDrop=new OverlayItem(pt, inDrag.getTitle(),
                                           inDrag.getSnippet());
        //Toast.makeText(GMapsActivity.this, pt.getLatitudeE6()+" "+pt.getLongitudeE6(), Toast.LENGTH_SHORT).show();
        items.add(toDrop);
        populate();
        
        inDrag=null;
        result=true;
      }
      
      return(result || super.onTouchEvent(event, mapView));
    }
    
    private void setDragImagePosition(int x, int y) {
      RelativeLayout.LayoutParams lp=
        (RelativeLayout.LayoutParams)dragImage.getLayoutParams();
            
      lp.setMargins(x-xDragImageOffset-xDragTouchOffset,
                      y-yDragImageOffset-yDragTouchOffset, 0, 0);
      dragImage.setLayoutParams(lp);
    }
  }
  
   private Bitmap getMapImage() {    
       MapController mc = map.getController();  
       //mc.setCenter(SOME_POINT);  
       //mc.setZoom(16);  

       map.setDrawingCacheEnabled(true);  
       Bitmap bmp = Bitmap.createBitmap(map.getDrawingCache());  
       map.setDrawingCacheEnabled(false);  

       return bmp;  
   }  

   private void saveMapImage() throws IOException {  
       String filename = "location_" + WorkWithTimeAndDate.getCurrentTime()+".png";  
       File f = new File(getExternalFilesDir(null), filename);  
       FileOutputStream out = new FileOutputStream(f);
       path = f.getAbsolutePath();

       Log.d("map",getExternalFilesDir(null).toString());
       Bitmap bmp = getMapImage();  

       bmp.compress(Bitmap.CompressFormat.PNG, 100, out);  

       out.close();  
   }  
   
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	} 
}
