package sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Paul's on 13-Jan-15.
 */
public class AuthenticatorService extends Service{

    private Authenticator mAuthenticator;

    public void onCreate()
    {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
