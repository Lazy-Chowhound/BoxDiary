package example.diary.ui.memo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.widget.Toast;

import example.diary.R;


public class AlarmReceiver extends BroadcastReceiver {
    private static int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        count = count + 1;

        Toast toast = Toast.makeText(context, "您还有未完成的任务呦,快去备忘录里看看吧~", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1500);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notifyBuilder =
                new NotificationCompat.Builder(context).setContentTitle("盒子日记")
                        .setContentText("您还有未完成的任务呦,快去备忘录里看看吧~")
                        .setSmallIcon(R.drawable.alarm)
                        .setAutoCancel(false)
                        .setDefaults(Notification.DEFAULT_ALL | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        //发送提示消息
        notificationManager.notify(count, notifyBuilder.build());

    }
}
