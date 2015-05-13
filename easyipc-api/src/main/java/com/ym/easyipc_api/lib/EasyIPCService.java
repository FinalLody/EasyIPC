/*
 * Copyright 2015 Yuriy Myronovych
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ym.easyipc_api.lib;

import android.app.Service;
import android.content.Intent;
import android.net.LocalServerSocket;
import android.os.Binder;
import android.os.IBinder;
import com.ym.easyipc_api.lib.gen.ResolverFactory;

import java.io.IOException;

/**
 * Created by Yuriy Myronovych on 21/04/2015.
 */
public class EasyIPCService extends Service {
    protected IResolver resolver = ResolverFactory.getResolver(this);

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread() {
            @Override
            public void run() {
                try {
                    LocalServerSocket socket = new LocalServerSocket(resolver.getAddress());
                    while (true) {
                        new LocalSocketServerHandler(resolver, socket.accept()).handle();
                        System.out.print("LocalSocketServerHandler created");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
}
