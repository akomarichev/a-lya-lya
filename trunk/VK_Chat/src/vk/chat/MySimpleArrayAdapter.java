package vk.chat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import vk.api.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<User> {	
    private final Context context;
    private final User[] values;
    public static TextView text;
    
    static class ViewHolder {
		public TextView text;
		//public ImageView image;
	}

    public MySimpleArrayAdapter(Context context, User[] objects) {
        super(context, R.layout.friends_list, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	//View rowView = convertView;
    	//if (rowView == null) {
    		LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View rowView = inflater.inflate(R.layout.friends_list, parent, false);
			//ViewHolder viewHolder = new ViewHolder();
			text = (TextView) rowView.findViewById(R.id.tvNameFriend);
			//viewHolder.image = (ImageView) rowView.findViewById(R.id.ivAvatarFriend);
			//rowView.setTag(viewHolder);
		//}
    	//ViewHolder holder = (ViewHolder) rowView.getTag();
    	//User u = values[position];
    	text.setText(values[position].first_name+" "+values[position].last_name);
    	/*URL img_value = null;
        Bitmap mIcon1 = null;
        try {
			img_value = new URL(values[position].photo_medium);
			mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			mIcon1 = Bitmap.createScaledBitmap(mIcon1, 46, 46, true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		      
        holder.image.setImageBitmap((Bitmap) mIcon1);*/
        /*LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.friends_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.tvNameFriend);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.ivAvatarFriend);
        textView.setText(values[position].first_name + " " + values[position].last_name);*/
        /*URL img_value = null;
        Bitmap mIcon1 = null;
        try {
			img_value = new URL(values[position].photo_medium);
			mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
			mIcon1 = Bitmap.createScaledBitmap(mIcon1, 46, 46, true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		      
		imageView.setImageBitmap((Bitmap) mIcon1);*/
        return rowView;
    }
}