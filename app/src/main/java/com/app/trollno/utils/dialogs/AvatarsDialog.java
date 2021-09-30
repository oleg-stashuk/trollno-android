 package com.app.trollno.utils.dialogs;

 import android.app.Dialog;
 import android.content.Context;
 import android.graphics.Point;
 import android.graphics.drawable.ColorDrawable;
 import android.util.TypedValue;
 import android.view.Display;
 import android.view.KeyEvent;
 import android.view.View;
 import android.view.ViewGroup;
 import android.view.Window;
 import android.view.WindowManager;
 import android.widget.ImageView;

 import androidx.recyclerview.widget.GridLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.app.trollno.R;
 import com.app.trollno.adapters.AvatarsAdapter;
 import com.app.trollno.data.model.profile.AvatarImageModel;
 import com.app.trollno.data.model.profile.RequestUpdateAvatarModel;
 import com.app.trollno.ui.base.BaseActivity;
 import com.app.trollno.utils.data.PrefUtils;
 import com.app.trollno.utils.networking.user.UpdateAvatar;

 import java.util.ArrayList;
 import java.util.List;

 public class AvatarsDialog {

     public void showDialog(Context context, PrefUtils prefUtils, List<AvatarImageModel> avatarList, ImageView imageView, View view){
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_avatar_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(width, height);

        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_avatar);

         AvatarsAdapter.OnItemClick<AvatarImageModel> avatarItemListener = (item, position) -> {
             List<AvatarImageModel> avatarUidList = new ArrayList<>();
             avatarUidList.add(new AvatarImageModel(item.getAvatarId()));
             RequestUpdateAvatarModel uidAvatar = new RequestUpdateAvatarModel(avatarUidList);

             new Thread(() -> UpdateAvatar.updateAvatar(context, prefUtils, uidAvatar, dialog, imageView, view)).start();
         };

        AvatarsAdapter adapter = new AvatarsAdapter((BaseActivity) context, avatarList, avatarItemListener);
         recyclerView.setLayoutManager(new GridLayoutManager(context, columnsCount(context)));
         recyclerView.setAdapter(adapter);
         recyclerView.setHasFixedSize(true);

        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog1.cancel();
                return true;
            }
            return false;
        });

        dialog.show();
    }

     // вычесление количества столбцов для recyclerView
     private int columnsCount(Context context){
         WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); //Получаем размер экрана
         Display display = wm.getDefaultDisplay();
         Point point = new Point();
         display.getSize(point);
         int screenWidth = point.x; //Ширина экрана
         int photoWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, context.getResources().getDisplayMetrics()); //Переводим в точки
         int columnsCount = 2; //Число столбцов

         return columnsCount;
     }


 }
