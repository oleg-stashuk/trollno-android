 package com.apps.trollino.utils.dialogs;

 import android.app.Dialog;
 import android.content.Context;
 import android.graphics.Point;
 import android.graphics.drawable.ColorDrawable;
 import android.util.TypedValue;
 import android.view.Display;
 import android.view.KeyEvent;
 import android.view.ViewGroup;
 import android.view.Window;
 import android.view.WindowManager;
 import android.widget.ImageView;

 import androidx.recyclerview.widget.GridLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.apps.trollino.R;
 import com.apps.trollino.adapters.AvatarsAdapter;
 import com.apps.trollino.data.model.profile.AvatarImageModel;
 import com.apps.trollino.data.model.profile.RequestUpdateAvatarModel;
 import com.apps.trollino.ui.base.BaseActivity;
 import com.apps.trollino.utils.data.PrefUtils;
 import com.apps.trollino.utils.networking.user.UpdateAvatar;

 import java.util.ArrayList;
 import java.util.List;

 public class AvatarsDialog {

     public void showDialog(Context context, PrefUtils prefUtils, List<AvatarImageModel> avatarList, ImageView imageView){
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

             new Thread(() -> UpdateAvatar.updateAvatar(context, prefUtils, uidAvatar, dialog, imageView, dialog.findViewById(R.id.custom_dialog_avatar_list))).start();
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
         int columnsCount = screenWidth/photoWidth; //Число столбцов

         return columnsCount;
     }


 }
