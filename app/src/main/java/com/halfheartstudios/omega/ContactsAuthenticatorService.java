package com.halfheartstudios.omega;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by jacob on 2017-07-11.
 */

public class ContactsAuthenticatorService extends Service {
  @Override
  public IBinder onBind(Intent intent) {
    ContactsAuthenticator authenticator = new ContactsAuthenticator(this);
    return authenticator.getIBinder();
  }
}