package example.com.hvacawards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FirebaseAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(FirebaseBackgroundService.class.getName()));
    }
}
